package com.example.emailservlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Processes an HTTP POST requests to send an email.
 * <p>
 * It is mapped to the "/emailServlet" URL pattern and uses an {@link EmailProcessor} to perform the actual
 * email sending logic. Form data is expected to be sent in the request parameters. After processing
 * the request, it responds with a text message indicating whether the email was sent successfully.
 *
 * @author Björn Forsberg
 * @see EmailProcessor
 */
@WebServlet("/emailServlet")
public class EmailServlet extends HttpServlet {

    /**
     * Handles HTTP POST requests by delegating to processRequest.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Processes an HTTP request to send an email. It extracts the email details from the request parameters,
     * uses an EmailProcessor to send the email, and responds with a simple message indicating success.
     *
     * @param request  the servlet request
     * @param response the servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String cc = request.getParameter("cc");
        String bcc = request.getParameter("bcc");
        String subject = request.getParameter("subject");
        String messageContent = request.getParameter("message");

        messageContent += "\n\nObservera! Detta meddelande är sänt från ett formulär på Internet och avsändaren kan vara felaktig!";

        EmailProcessor emailProcessor = new EmailProcessor();
        emailProcessor.processEmail(from, to, cc, bcc, subject, messageContent);
        response.setContentType("text/html");
        response.getWriter().println("Email sent successfully.");
    }
}