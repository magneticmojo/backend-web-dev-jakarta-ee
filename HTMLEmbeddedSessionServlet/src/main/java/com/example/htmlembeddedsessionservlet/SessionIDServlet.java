package com.example.htmlembeddedsessionservlet;

import java.io.IOException;
import java.io.File;
import java.util.UUID;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Generates a unique session ID using the UUID utility for each GET request,
 * and embed this session ID into an HTML template before sending the response.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet("/")
public class SessionIDServlet extends HttpServlet {

    private static String htmlTemplate = null;

    /**
     * This method is called once when the Servlet is initialized.
     * It reads an HTML template from a file which is later used in GET requests.
     */
    @Override
    public void init() {
        if (htmlTemplate == null) {
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("index.html")));
        }
    }

    /**
     * Handles GET requests. Generates a unique session ID and embeds it into the HTML response.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String sessionId = UUID.randomUUID().toString();
        Mixer mixer = new Mixer(htmlTemplate);
        mixer.add("---session-id---", sessionId);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(mixer.getMix());
    }
}
