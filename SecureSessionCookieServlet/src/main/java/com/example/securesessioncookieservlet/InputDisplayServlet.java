package com.example.securesessioncookieservlet;

import java.io.IOException;
import java.io.File;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

/**
 * Displays information from a request, including the session ID from a secure session cookie.
 * The information is displayed in an HTML page.
 * <p>
 * The displayed information includes whether the form in the HTML page was used, the value of the name field in the form
 * if it was used, and the value of the session ID from the secure session cookie, if one is present in the request.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet("/display")
public class InputDisplayServlet extends HttpServlet {

    private static String htmlTemplate = null;

    /**
     * Initializes the servlet. Reads the HTML template from a file.
     */
    @Override
    public void init() {
        if (htmlTemplate == null) {
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("display.html")));
        }
    }

    /**
     * Handles GET requests. Extracts information from the request, including the session ID from a secure session cookie
     * and sends an HTML page that displays the information to the client.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs while handling the request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Mixer mixer = new Mixer(htmlTemplate);
        configureMixerFromRequest(request, mixer);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(mixer.getMix());
    }

    /**
     * Configures a Mixer with information from the request.
     *
     * @param request the HTTP request
     * @param mixer the Mixer to configure
     */
    private void configureMixerFromRequest(HttpServletRequest request, Mixer mixer) {
        String sessionId = getSessionIdFromCookie(request);
        String name = request.getParameter("name");
        String button = request.getParameter("send-button");

        if (sessionId == null) {
            mixer.add("---session-id---", "Session ID not found.");
        } else {
            mixer.add("---session-id---", "session id = " + sessionId);
        }

        if (isFormUsed(button)) {
            mixer.add("<!--===form===-->", "");
            mixer.add("---button---", "button = " + button);
            if (isNameEntered(name)) {
                mixer.add("---name---", "name = " + name);
            } else {
                mixer.add("---name---", "name = " + "not entered");
            }
        } else {
            mixer.removeContext("<!--===form===-->");
        }
    }

    /**
     * Returns whether the form in the HTML page was used.
     *
     * @param button the value of the "send-button" parameter from the request
     * @return true if the form was used, false otherwise
     */
    private boolean isFormUsed(String button) {
        return button != null && button.equals("Send");
    }

    /**
     * Returns whether a name was entered in the form in the HTML page.
     *
     * @param name the value of the "name" parameter from the request
     * @return true if a name was entered, false otherwise
     */
    private boolean isNameEntered(String name) {
        return name != null && !name.isEmpty();
    }

    /**
     * Returns the session ID from the secure session cookie in the request, if one is present.
     *
     * @param request the HTTP request
     * @return the session ID, or null if no secure session cookie is present in the request
     */
    private String getSessionIdFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie sessionIDCookie;
        String sessionId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionID")) {
                    sessionIDCookie = cookie;
                    sessionId = sessionIDCookie.getValue();
                    break;
                }
            }
        }
        return sessionId;
    }
}
