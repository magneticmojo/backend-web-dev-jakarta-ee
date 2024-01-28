package com.example.sessioncookieservlet;

import java.io.IOException;
import java.io.File;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

/**
 * Displays the session ID, name, and state from the received HTTP GET request
 * by putting the values into an HTML template and sending the result to the client.
 * The session ID is retrieved from the cookie sent by the client.
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
     * This method is called once when the Servlet is initialized.
     * It reads an HTML template from a file which is later used in GET requests.
     */
    @Override
    public void init() {
        if (htmlTemplate == null) {
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("display.html")));
        }
    }

    /**
     * Handles GET requests. Extracts parameters from the request and the session ID from the cookie,
     * embeds them into the HTML response.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException if an error occurs while writing the response
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
     * Configures the Mixer instance by extracting parameters from the request and the session ID from the cookie,
     * and adding them to the mixer.
     *
     * @param request the HttpServletRequest
     * @param mixer   the Mixer instance
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
     * Checks if the form is used based on the Send button's state.
     *
     * @param button the Send button's state
     * @return true if the form is used, false otherwise
     */
    private boolean isFormUsed(String button) {
        return button != null && button.equals("Send");
    }

    /**
     * Checks if a name is entered in the form.
     *
     * @param name the name entered in the form
     * @return true if a name is entered, false otherwise
     */
    private boolean isNameEntered(String name) {
        return name != null && !name.equals("");
    }

    /**
     * Extracts the session ID from the cookies sent with the HTTP request.
     *
     * @param request the HttpServletRequest
     * @return the session ID from the cookie, or null if it's not found
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

