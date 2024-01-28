package com.example.formtordbmsservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Handles POST and GET requests to the '/' URL pattern. The servlet supports the functionality of a guestbook,
 * where users can submit their details and comments via a form (POST request), and these entries are displayed
 * when the page is loaded (GET request).
 * <p>
 * The servlet validates the input, escapes HTML special characters to prevent XSS attacks, and handles errors.
 * <p>
 * Entries are stored in a MySQL database via the {@link GuestDB} class and displayed in order of their timestamps.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "guestBookServlet", urlPatterns = "/")
public class GuestBookServlet extends HttpServlet {

    private static String htmlTemplate = null;

    /**
     * Initializes the servlet, loading the HTML template for the guest book page.
     */
    @Override
    public void init() {
        if (htmlTemplate == null) {
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("guest-book-template.html")));
        }
    }

    /**
     * Handles POST requests. Validates and sanitizes user input, creates a new Guest object,
     * and inserts it into the database. Redirects the user to the same URL to display the updated guest book.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = sanitizeInput(request.getParameter("name"));
        String email = sanitizeInput(request.getParameter("email"));
        String homepage = sanitizeInput(request.getParameter("homepage"));
        String comment = sanitizeInput(request.getParameter("comment"));

        if (isInvalidEmail(email)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email format");
            return;
        }

        try {
            createNewEntry(request, response, createGuest(name, email, homepage, comment));
        } catch (IOException e) {
            response.setContentType("text/plain");
            response.getWriter().println("Error : " + e.getMessage());
        }
    }

    /**
     * Sanitizes user input by trimming and escaping HTML special characters.
     *
     * @param input The raw input string.
     * @return The sanitized input string.
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        return StringEscapeUtils.escapeHtml4(input);
    }

    /**
     * Checks if an email address is in a valid format.
     *
     * @param email The email address to validate.
     * @return true if the email address is invalid, false otherwise.
     */
    private boolean isInvalidEmail(String email) {
        return !email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Creates a new entry in the guest book by inserting a Guest object into the database.
     * Handles any exceptions that occur during insertion.
     * Redirects the user to the same URL and calls doGet() to display the updated guest book.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @param guest    The Guest object to insert into the database.
     * @throws IOException If an I/O error occurs.
     */
    private void createNewEntry(HttpServletRequest request, HttpServletResponse response, Guest guest) throws IOException {
        try {
            GuestDB.insert(guest);
            response.sendRedirect(request.getRequestURI());
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    /**
     * Creates a new Guest object.
     *
     * @param name     The name of the guest.
     * @param email    The email address of the guest.
     * @param homepage The homepage of the guest.
     * @param comment  The comment of the guest.
     * @return The new Guest object.
     */
    private Guest createGuest(String name, String email, String homepage, String comment) {
        Date timestamp = new Date();
        return new Guest(name, email, homepage, comment, timestamp);
    }

    /**
     * Handles GET requests. Generates the HTML for the guest book page by use of getGuestBookEntriesHTML(),
     * and sends this HTML as the response.
     *
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print(getGuestBookEntriesHTML());
        } catch (IOException e) {
            response.setContentType("text/plain");
            response.getWriter().println("Error : " + e.getMessage());
        }
    }

    /**
     * Retrieves guests and generates the HTML for the guest book entries section of the page.
     *
     * @return The HTML for the guest book entries section of the page.
     */
    private String getGuestBookEntriesHTML() {
        List<Guest> guests = getSortedGuests();
        return getHTML(guests);
    }

    /**
     * Replaces placeholders in the HTML template with actual guest book entries.
     *
     * @param guests A list of Guest objects.
     * @return The resulting HTML string.
     */
    private String getHTML(List<Guest> guests) {
        Mixer mixer = new Mixer(htmlTemplate);

        int postCount = 1;
        if (guests.isEmpty()) {
            mixer.removeContext("<!--===entries===-->");
        } else {
            for (Guest guest : guests) {
                System.out.println("adding guest to htmltemplate");
                mixer.add("<!--===entries===-->", "---no---", Integer.toString(postCount)); // use counter instead of guest ID
                mixer.add("<!--===entries===-->", "---time---", guest.getTimestamp().toString());
                mixer.add("<!--===entries===-->", "---homepage---", guest.getHomepage());
                mixer.add("<!--===entries===-->", "---name---", guest.getName());
                mixer.add("<!--===entries===-->", "---email---", guest.getEmail());
                mixer.add("<!--===entries===-->", "---comment---", guest.getComment());

                postCount++;
            }
        }
        return mixer.getMix();
    }

    /**
     * Retrieves all guest book entries from the database and sorts them by their timestamps.
     *
     * @return A sorted list of Guest objects.
     */
    private List<Guest> getSortedGuests() {
        List<Guest> guests = GuestDB.getAll();
        guests.sort(Comparator.comparing(Guest::getTimestamp));
        return guests;
    }
}
