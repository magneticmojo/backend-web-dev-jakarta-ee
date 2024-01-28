package com.example.envvarshtmlservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * A servlet that collects various information about such things as header details, server information,
 * request details, servlet information, and environment variables. This information is via the response object
 * presented to the client in HTML format.
 * <p>
 * {@link RequestInfoCollector} is a utility class used by EnvVarsHTMLServlet to gather the information.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "EnvVarsHTMLServlet", urlPatterns = "/")
public class EnvVarsHTMLServlet extends HttpServlet {

    private static String htmlTemplate = null;

    /**
     * This method is called once when the Servlet is initialized.
     * It reads an HTML template from a file which is later used in GET requests.
     */
    @Override
    public void init() {
        if (htmlTemplate == null) {
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("environment-variables-template.html")));
        }
    }

    /**
     * Handles GET requests. Collects various information about the request and its environment
     * and writes this information in HTML format to the response. If an error occurs, the response status is set to 500.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            writeHTMLResponse(request, response);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Writes the response for the client in HTML format.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException if an error occurs while writing the response
     */
    private void writeHTMLResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println(getHTML(request));
    }

    /**
     * Generates the HTML content for the response using the Mixer class and the collected information.
     *
     * @param request the HttpServletRequest
     * @return the HTML content
     */
    private String getHTML(HttpServletRequest request) {
        Mixer generator = new Mixer(htmlTemplate);
        Map<String, String> envVars = RequestInfoCollector.collectAllInformation(request);
        for (Map.Entry<String, String> entry : envVars.entrySet()) {
            if (entry.getKey() != null && !entry.getKey().isEmpty() && entry.getValue() != null && !entry.getValue().isEmpty()) {
                generator.add("<!--==xxx==-->", "---name---", entry.getKey() + ": ");
                generator.add("<!--==xxx==-->", "---value---", entry.getValue());
            }
        }
        return generator.getMix();
    }
}
