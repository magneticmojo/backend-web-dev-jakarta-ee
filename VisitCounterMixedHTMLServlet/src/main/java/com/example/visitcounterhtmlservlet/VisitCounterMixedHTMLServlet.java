package com.example.visitcounterhtmlservlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A servlet that counts the number of visits to its URL pattern.
 * It responds to GET requests by reading a visit count from a file, incrementing the count, and then displaying the new count
 * in an HTML response. If an error occurs during this process, the servlet responds with an HTTP 500 status code (Internal Server Error).
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "VisitCounterHTMLServlet", urlPatterns = {"/"})
public class VisitCounterMixedHTMLServlet extends HttpServlet {

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
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("display-visit-count.html")));
        }
    }

    /**
     * Handles a GET request by responding with the current visit count.
     *
     * @param request  the incoming HTTP request
     * @param response the outgoing HTTP response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            writeHTMLResponse(response);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sends an HTML response to the client by setting the content type of the response,
     * obtaining a PrintWriter to write the response data, and then writing the generated HTML to the response.
     *
     * @param response The HttpServletResponse object representing the HTTP response to be sent to the client
     * @throws IOException If an error occurs during the writing of the response
     */
    private void writeHTMLResponse(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(getHTML());
    }

    /**
     * Generates an HTML response by reading the HTML template file and replacing the placeholder
     * with the current visit count.
     *
     * @return The generated HTML response
     * @throws IOException If an error occurs during the reading of the HTML template file
     */
    private String getHTML() throws IOException {
        Mixer generator = new Mixer(htmlTemplate);
        generator.add("---hits---", String.valueOf(getVisitCount()));
        return generator.getMix();
    }

    /**
     * Increments the visit count by reading the count from a file, incrementing it, and writing it back to the file.
     *
     * @return the incremented visit count
     * @throws IOException if an error occurs while reading from or writing to the visit count file
     */
    private int getVisitCount() throws IOException {
        String fileName = "visitcount.txt";
        String filePath = getServletContext().getRealPath(fileName);
        return FileUtil.incrementVisitCount(filePath);
    }
}
