package com.example.emailattachmentservlet;

import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
/**
 * Handles HTTP POST requests, extracting parameters and attachments from the request, and using the {@link EmailProcessor}
 * to send an email with those attachments. This servlet handles multipart/form-data requests, typically used for file uploads.
 *
 * @author Björn Forsberg
 * @see EmailProcessor
 */
@WebServlet("/emailAttachmentServlet")
@MultipartConfig
public class EmailAttachmentServlet extends HttpServlet {

    /**
     * Handles HTTP POST requests.
     *
     * @param request  an HttpServletRequest object that contains the request the client has made of the servlet
     * @param response an HttpServletResponse object that contains the response the servlet sends to the client
     * @throws ServletException if the request for the POST could not be handled
     * @throws IOException      if an input or output error is detected when the servlet handles the request
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    /**
     * Processes the request, extracting parameters and attachments and using them to send an email.
     *
     * @param request  an HttpServletRequest object that contains the client's request
     * @param response an HttpServletResponse object that contains the servlet's response
     * @throws IOException      if an I/O error occurs while reading from or writing to a file or network
     * @throws ServletException if a servlet-specific error occurs
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String from = getPartAsString(request.getPart("from"));
        String to = getPartAsString(request.getPart("to"));
        String cc = getPartAsString(request.getPart("cc"));
        String bcc = getPartAsString(request.getPart("bcc"));
        String subject = getPartAsString(request.getPart("subject"));
        String messageContent = getPartAsString(request.getPart("message"));
        Part filePart1 = request.getPart("file1");
        Part filePart2 = request.getPart("file2");
        messageContent += "\n\nObservera! Detta meddelande är sänt från ett formulär på Internet och avsändaren kan vara felaktig!";

        EmailProcessor emailProcessor = new EmailProcessor();
        try {
            emailProcessor.processEmail(from, to, cc, bcc, subject, messageContent, filePart1, filePart2);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html");
        response.getWriter().println("Email sent successfully.");
    }

    /**
     * Retrieves the content of a given part of the request as a string.
     *
     * @param part the part of the request to retrieve content from
     * @return the content of the part as a string, or an empty string if the part is null
     * @throws IOException if an I/O error occurs while reading from the part's input stream
     */
    private String getPartAsString(Part part) throws IOException {
        if (part == null) {
            return "";
        }
        try (InputStream inputStream = part.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toString(StandardCharsets.UTF_8.name());
        }
    }

}

