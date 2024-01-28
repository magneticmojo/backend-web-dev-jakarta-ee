package com.example.httpsessionservlet;

import java.io.IOException;
import java.io.File;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Receives an HTTP GET request, retrieves user input and session ID from the request,
 * embeds them into an HTML template, and sends this HTML back to the client.
 * <p>
 * For each GET request, it retrieves the session ID and the parameters "name" and "send-button" from the request.
 * The received data is put into the HTML template before sending the response.
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
     * Handles GET requests. Retrieves user input and session ID from the request,
     * embeds them into the HTML response.
     *
     * @param request  the HttpServletRequest
     * @param response the HttpServletResponse
     * @throws IOException if an error occurs while writing the response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(getHTML(request));
    }

    /**
     * Creates a Mixer with the HTML template, configures it with data from the request,
     * and returns the resulting HTML.
     *
     * @param request the HttpServletRequest
     * @return the generated HTML
     */
    private String getHTML(HttpServletRequest request) {
        Mixer mixer = new Mixer(htmlTemplate);
        configureMixerFromRequest(request, mixer);
        return mixer.getMix();
    }

    /**
     * Configures the Mixer with the session ID and user input retrieved from the request.
     *
     * @param request the HttpServletRequest
     * @param mixer   the Mixer
     */
    private void configureMixerFromRequest(HttpServletRequest request, Mixer mixer) {
        String sessionId = getSessionID(request);
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
     * Retrieves the session ID from the request.
     *
     * @param request the HttpServletRequest
     * @return the session ID
     */
    private String getSessionID(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session.getId();
    }

    /**
     * Checks if the form is used by checking if the button parameter is "Send".
     *
     * @param button the button parameter
     * @return true if the button parameter is "Send", false otherwise
     */
    private boolean isFormUsed(String button) {
        return button != null && button.equals("Send");
    }

    /**
     * Checks if a name was entered by checking if the name parameter is not null and not empty.
     *
     * @param name the name parameter
     * @return true if the name parameter is not null and not empty, false otherwise
     */
    private boolean isNameEntered(String name) {
        return name != null && !name.isEmpty();
    }
}
