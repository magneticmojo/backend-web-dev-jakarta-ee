package com.example.sessioncookieservlet;

import java.io.IOException;
import java.io.File;
import java.util.UUID;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Generates a unique session ID, sets it in a cookie, and embeds it into an HTML template,
 * which is sent back to the client in response to an HTTP GET request.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet("/")
public class SessionIDCookieServlet extends HttpServlet {

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
     * Handles GET requests. Generates a unique session ID, sets it in a cookie, and embeds it into the HTML response.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Cookie cookie = setCookie();
        response.addCookie(cookie);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(generateHTML(getSessionID(cookie)));
    }

    /**
     * Creates a new cookie with a unique session ID and sets it to expire in 3 hours.
     *
     * @return the created Cookie object
     */
    private Cookie setCookie() {
        String sessionId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("sessionID", sessionId);
        cookie.setMaxAge(60 * 60 * 3);
        return cookie;
    }

    /**
     * Retrieves the session ID from the given cookie.
     *
     * @param cookie the Cookie object
     * @return the session ID from the cookie
     */
    private String getSessionID(Cookie cookie) {
        return cookie.getValue();
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
