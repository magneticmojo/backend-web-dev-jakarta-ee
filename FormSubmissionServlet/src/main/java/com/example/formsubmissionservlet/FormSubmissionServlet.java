package com.example.formsubmissionservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Servlet that handles form submissions by processing form parameters from an HTTP request
 * and displaying them as plain text in the response.
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "FormSubmissionServlet", urlPatterns = "/form-submission-servlet")
public class FormSubmissionServlet extends HttpServlet {

    /**
     * Handles POST requests by processing the form parameters.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    /**
     * Processes the form parameters from the HTTP request and writes them to the HTTP response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @throws IOException if an I/O error occurs
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain; charset=UTF-8");

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            response.getWriter().println(paramName + ": " + paramValue);
        }
    }
}

