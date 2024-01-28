package com.example.transactionservlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Handles the guest book form and manages the entries in the guest book.
 * It receives form data and creates guest entries in the database and retrieves guest
 * entries to display them. It also handles multipart form data for image uploading.
 * <p>
 * The received data is sanitized before processing to avoid cross-site scripting (XSS) attacks.
 * <p>
 * The Mixer class used in this servlet was authored by Pierre Wijkman and Björn Nilsson. See {@link Mixer}.
 * Link to Mixer source code <a href="https://people.dsv.su.se/~pierre/os/mixer/">https://people.dsv.su.se/~pierre/os/mixer/</a>}
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "guestBookServlet", urlPatterns = "/")
@MultipartConfig
public class GuestBookServlet extends HttpServlet {

    private static String htmlTemplate = null;

    /**
     * Initializes the servlet. Loads the HTML template from a file.
     */
    @Override
    public void init() {
        if (htmlTemplate == null) {
            htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("index.html")));
        }
    }

    /**
     * Handles the HTTP POST request. Validates form data, creates a Guest object and an Image object if an image has been uploaded, and stores it into the database.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException If an input or output exception occurs
     * @throws ServletException If a servlet-specific error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String namePart = getValue(request.getPart("name"));
        String emailPart = getValue(request.getPart("email"));
        String homepagePart = getValue(request.getPart("homepage"));
        String commentPart = getValue(request.getPart("comment"));
        Part imagePart = request.getPart("image");

        if (isInvalidEmail(emailPart)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid email format");
            return;
        }

        Guest guest = createGuest(namePart, emailPart, homepagePart, commentPart);
        if (isImageUploaded(imagePart)) {
            Image image = getImage(imagePart);
            guest.setImage(image);
            image.setGuest(guest);
        }
        try {
            createNewEntry(request, response, guest);
        } catch (IOException e) {
            response.setContentType("text/plain");
            response.getWriter().println("Error : " + e.getMessage());
        }
    }

    /**
     * Retrieves and sanitizes the value from a part of the form data.
     *
     * @param part The part from the form data
     * @return A sanitized version of the value from the form data
     * @throws IOException If an input or output exception occurs
     */
    private String getValue(Part part) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(part.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[1024];
        for (int length = 0; (length = reader.read(buffer)) > 0; ) {
            value.append(buffer, 0, length);
        }
        String unsafeInput = value.toString();
        return sanitizeInput(unsafeInput);
    }

    /**
     * Sanitizes a string input to prevent XSS attacks by escaping HTML entities.
     *
     * @param input The string to sanitize
     * @return A sanitized version of the input
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        return StringEscapeUtils.escapeHtml4(input);
    }

    /**
     * Validates an email string by verifying that it matches a standard email pattern.
     *
     * @param value The email string to validate
     * @return true if the email string is invalid, otherwise false
     */
    private boolean isInvalidEmail(String value) {
        return !value.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    /**
     * Creates a Guest object from the form data.
     *
     * @param name The guest's name
     * @param email The guest's email
     * @param homepage The guest's homepage
     * @param comment The guest's comment
     * @return A new Guest object
     */
    private Guest createGuest(String name, String email, String homepage, String comment) {
        Date timestamp = new Date();
        return new Guest(name, email, homepage, comment, timestamp);
    }

    /**
     * Checks if an image was uploaded by verifying the size of the image part.
     *
     * @param imagePart The image part from the form data
     * @return true if an image is uploaded, otherwise false
     */
    private boolean isImageUploaded(Part imagePart) {
        return imagePart != null && imagePart.getSize() > 0;
    }

    /**
     * Creates an Image object from an image part of the form data.
     *
     * @param imagePart The image part from the form data
     * @return A new Image object
     * @throws IOException If an input or output exception occurs
     */
    private Image getImage(Part imagePart) throws IOException {
        byte[] imageData = imagePart.getInputStream().readAllBytes();
        String mimeType = imagePart.getContentType();
        Image image = new Image();
        image.setImageData(imageData);
        image.setMimeType(mimeType);
        return image;
    }

    /**
     * Inserts a new guest entry into the database and redirects the client to the original URL.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param guest The guest to insert into the database
     * @throws IOException If an input or output exception occurs
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
     * Handles the HTTP GET request. Retrieves all guest entries from the database and displays them.
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException If an input or output exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().print(getGuestBookEntriesHTML());
        } catch (IOException e) {
            response.setContentType("text/plain");
            response.getWriter().println("Error : " + e.getMessage());
        }
    }

    /**
     * Generates the HTML content for displaying all guest entries.
     *
     * @return The HTML content for all guest entries
     */
    private String getGuestBookEntriesHTML() {
        List<Guest> guests = getSortedGuests();
        return getHTML(guests);
    }

    /**
     * Retrieves all guest entries from the database and sorts them by timestamp.
     *
     * @return A sorted list of all guest entries
     */
    private List<Guest> getSortedGuests() {
        List<Guest> guests = GuestDB.getAll();
        guests.sort(Comparator.comparing(Guest::getTimestamp));
        return guests;
    }

    /**
     * Uses the Mixer to generate the HTML content for a list of guests.
     *
     * @param guests The list of guests
     * @return The HTML content for the list of guests
     */
    private String getHTML(List<Guest> guests) {
        Mixer mixer = new Mixer(htmlTemplate);
        int postCount = 1;
        if (guests.isEmpty()) {
            mixer.removeContext("<!--===entries===-->");
            System.out.println("GETHTML IF CLAUSE NO GUESTS");
        } else {
            for (Guest guest : guests) {
                mixer.add("<!--===entries===-->", "---no---", Integer.toString(postCount)); // use counter instead of guest ID
                mixer.add("<!--===entries===-->", "---time---", guest.getTimestamp().toString());
                mixer.add("<!--===entries===-->", "---homepage---", guest.getHomepage());
                mixer.add("<!--===entries===-->", "---name---", guest.getName());
                mixer.add("<!--===entries===-->", "---email---", guest.getEmail());
                mixer.add("<!--===entries===-->", "---comment---", guest.getComment());

                if (guest.getImage() != null) {
                    mixer.add("<!--===entries===-->", "---imageId---", Long.toString(guest.getImage().getId()));
                    mixer.add("<!--===entries===-->", "---altTag---", "Guest's image");
                } else {
                    mixer.add("<!--===entries===-->", "---imageId---", "");
                    mixer.add("<!--===entries===-->", "---altTag---", "");
                }
                postCount++;
            }
        }

        return mixer.getMix();
    }
}
