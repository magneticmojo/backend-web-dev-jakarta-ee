package com.example.fileuploadservlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A servlet that allows for the uploading of files and then displays them if their mime type is supported.
 * The servlet supports text/plain, image/jpeg, image/png, and image/gif mime types. For unsupported mime types,
 * the file metadata is displayed. Displays an error message if no file is uploaded.
 * The servlet uses an HTML template in combination with the Mixer class to display the file data.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = "/file-upload-servlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10) // 10 MB max file size
public class FileUploadServlet extends HttpServlet {

    /**
     * HTML template for displaying file data or error messages.
     */
    private static String htmlTemplate = null;

    /**
     * Initialize servlet and load HTML template.
     */
    @Override
    public void init() {
        if (htmlTemplate == null) {
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("result.html")));
        }
    }

    /**
     * Handles POST request, processing uploaded file and sending it back if its mime type is supported. Or displaying
     * file metadata if the mime type is unsupported or an error message if no file was uploaded.
     *
     * @param request the servlet request
     * @param response the servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");

        if (filePart != null && filePart.getSize() > 0) {
            String mimeType = filePart.getContentType();

            FileData fileData = createFileDataObject(filePart, mimeType);
            if (isMimeTypeSupported(mimeType)) {
                displayFileContent(response, fileData);
            } else {
                displayHTML(response, getHTML("Unsupported mime-type", fileData));
            }
        } else {
            displayHTML(response, getHTML("No file was uploaded", null));
        }
    }

    /**
     * Creates a FileData object encapsulating the uploaded file's data and metadata.
     *
     * @param filePart the uploaded file part
     * @param mimeType the MIME type of the uploaded file
     * @return FileData object representing the uploaded file
     */
    private FileData createFileDataObject(Part filePart, String mimeType) throws IOException {
        String fileName = getFileName(filePart);
        long fileSize = filePart.getSize();
        byte[] fileContent = readFileContent(filePart);

        return new FileData(fileName, mimeType, fileSize, fileContent);
    }

    /**
     * Extracts the file name from the Content-Disposition header of the uploaded file part.
     *
     * @param filePart the uploaded file part
     * @return the file name
     */
    private String getFileName(Part filePart) {
        for (String contentDisposition : filePart.getHeader("content-disposition").split(";")) {
            if (contentDisposition.trim().startsWith("filename")) {
                return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Reads and returns the content of the uploaded file as a byte array.
     *
     * @param filePart the uploaded file part
     * @return a byte array representing the file content
     */
    private byte[] readFileContent(Part filePart) throws IOException {
        try (InputStream inputStream = filePart.getInputStream()) {
            return inputStream.readAllBytes();
        }
    }

    /**
     * Checks if the provided MIME type is supported by this servlet.
     *
     * @param mimeType the MIME type to check
     * @return true if the MIME type is supported, false otherwise
     */
    private boolean isMimeTypeSupported(String mimeType) {
        return "text/plain".equals(mimeType) ||
                "image/jpeg".equals(mimeType) ||
                "image/png".equals(mimeType) ||
                "image/gif".equals(mimeType);
    }

    /**
     * Writes the file content to the response, so that it can be displayed in the client's browser.
     *
     * @param response the servlet response
     * @param fileData the FileData object containing the file's content and metadata
     */
    private void displayFileContent(HttpServletResponse response, FileData fileData) throws IOException {
        response.setContentType(fileData.getMimeType());
        response.getOutputStream().write(fileData.getFileContent());
        response.getOutputStream().flush();
    }

    /**
     * Produces HTML for either an error message or file metadata, using a provided template.
     *
     * @param errorMessage the error message to display, or null if there's no error
     * @param fileData the FileData object containing the file's metadata, or null if there's an error
     * @return the produced HTML
     */
    private String getHTML(String errorMessage, FileData fileData) {
        Mixer htmlGenerator = new Mixer(htmlTemplate);

        if (errorMessage.equals("No file was uploaded")) {
            htmlGenerator.removeContext("<!--===file_information===-->");
            htmlGenerator.add("---error_message---", errorMessage);
        } else {
            htmlGenerator.removeContext("<!--===error===-->");
            htmlGenerator.add("---file_name---", fileData.getFileName());
            htmlGenerator.add("---mime_type---", fileData.getMimeType());
            htmlGenerator.add("---file_size---", Long.toString(fileData.getFileSize()));
        }
        return htmlGenerator.getMix();
    }

    /**
     * Writes the provided HTML to the response.
     *
     * @param response the servlet response
     * @param html the HTML to write
     */
    private void displayHTML(HttpServletResponse response, String html) throws IOException{
        response.getWriter().println(html);
    }
}


