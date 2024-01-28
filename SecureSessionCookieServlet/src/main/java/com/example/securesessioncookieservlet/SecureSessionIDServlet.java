package com.example.securesessioncookieservlet;

import java.io.IOException;
import java.io.File;
import java.util.UUID;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

/**
 * Generate secure session IDs and setting them in secure session cookies.
 * The secure session cookies are secure (transmitted only over HTTPS), HTTP-only,
 * and they have a limited lifetime.
 * <p>
 * The HTML page returned by this servlet includes the session ID in its body. The HTML template for the page is read
 * from a file named "index.html" in the web application's root directory.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet("/")
public class SecureSessionIDServlet extends HttpServlet {

    private static String htmlTemplate = null;

    /**
     * Initializes the servlet. Reads the HTML template from a file.
     */
    @Override
    public void init() {
        if (htmlTemplate == null) {
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("index.html")));
        }
    }

    /**
     * Handles GET requests. Generates a secure session ID, sets it in a secure session cookie, and sends an HTML page
     * that includes the session ID to the client.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs while handling the request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Cookie cookie = getSecureCookie();
        response.addCookie(cookie);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(generateHTML(getSessionID(cookie)));
    }

    /**
     * Creates and sets a new secure session cookie.
     *
     * @return the new secure session cookie
     */
    private Cookie getSecureCookie() {
        String sessionId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("sessionID", sessionId);
        cookie.setMaxAge(60 * 60 * 3);

        cookie.setSecure(true);
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * Returns the session ID from the given secure session cookie.
     *
     * @param cookie the secure session cookie
     * @return the session ID
     */
    private String getSessionID(Cookie cookie) {
        return cookie.getValue();
    }

    /**
     * Generates an HTML page that includes the given session ID by substituting the session ID into the HTML template.
     *
     * @param sessionId the session ID
     * @return the generated HTML page
     */
    private String generateHTML(String sessionId) {
        Mixer mixer = new Mixer(htmlTemplate);
        mixer.add("---session-id---", sessionId);
        return mixer.getMix();
    }
}

