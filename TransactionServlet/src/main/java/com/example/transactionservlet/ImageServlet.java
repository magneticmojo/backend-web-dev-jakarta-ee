package com.example.transactionservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles GET requests for displaying an image by fetching the image data from the database, sets the correct content type in the response,
 * and writes the image data to the response's output stream.
 * <p>
 * Uses the {@link ImageDB} class to retrieve the image entity based on the image ID passed in the request.
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "ImageServlet", urlPatterns = "/image-servlet")
public class ImageServlet extends HttpServlet {

    /**
     * Handles GET requests to display an image.
     * <p>
     * Retrieves the image ID from the request parameter, and uses it to fetch the image data from the database.
     * If the image is found, it sets the appropriate MIME type in the response and writes the image data to the response's output stream.
     * If the image is not found, it sends a 404 error.
     *
     * @param request  the incoming HTTP request
     * @param response the outgoing HTTP response
     * @throws IOException if an input or output error is detected when the servlet handles the GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Long id = Long.valueOf(request.getParameter("id"));
        System.out.println(ImageDB.getById(id));
        Image image = ImageDB.getById(id);
        if (image == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            return;
        }
        response.setContentType(image.getMimeType());
        response.getOutputStream().write(image.getImageData());
    }
}
