package com.example.httpsessionservlet;

import java.io.IOException;
import java.io.File;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Generates a unique session ID using HTTP sessions, embeds it into an HTML template,
 * and sends it back to the client in response to an HTTP GET request.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet("/")
public class HTTPSessionIDServlet extends HttpServlet {

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
     * Handles GET requests. Invalidates any existing session, creates a new session,
     * retrieves its unique ID, and embeds it into the HTML response.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(generateHTML(getSessionIDFrom(request)));
    }

    /**
     * Invalidates any existing session associated with the request, creates a new session,
     * and retrieves its unique ID.
     *
     * @param request the HttpServletRequest
     * @return the unique ID of the newly created session
     */
    private String getSessionIDFrom(HttpServletRequest request) {
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = request.getSession(true);
        return newSession.getId();
    }

    /**
     * Generates HTML by embedding the given session ID into the HTML template.
     *
     * @param sessionId the session ID
     * @return the generated HTML
     */
    private String generateHTML(String sessionId) {
        Mixer mixer = new Mixer(htmlTemplate);
        mixer.add("---session-id---", sessionId);
        return mixer.getMix();
    }
}
