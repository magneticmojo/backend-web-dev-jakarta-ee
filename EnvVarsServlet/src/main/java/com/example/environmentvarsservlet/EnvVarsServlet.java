package com.example.environmentvarsservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

/**
 * Handles the retrieval and presentation of various information, including header, server, request,
 * servlet, and environment variables. It responds to GET requests by printing the information in a plain text format.
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "EnvVarsServlet", urlPatterns = "/")
public class EnvVarsServlet extends HttpServlet {

    /**
     * Handles a GET request by responding with various information variables in plain text.
     *
     * @param request  the incoming HTTP request
     * @param response the outgoing HTTP response
     * @throws IOException if an error occurs while writing to the response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        printSection(out, "Header Information", getHeaderInformation(request));
        printSection(out, "Server Information", getServerInformation(request));
        printSection(out, "Request Information", getRequestInformation(request));
        printSection(out, "Servlet Information", getServletInformation(request));
        printSection(out, "Environment Variables", getEnvironmentVariables());
    }


    /**
     * Retrieves header information from the HTTP request.
     *
     * @param request the incoming HTTP request
     * @return a map containing header names and values
     */
    private Map<String, String> getHeaderInformation(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName.toUpperCase(), headerValue);
        }
        return headers;
    }

    /**
     * Retrieves server information from the HTTP request.
     *
     * @param request the incoming HTTP request
     * @return a map containing server details
     */
    private Map<String, String> getServerInformation(HttpServletRequest request) {
        Map<String, String> serverInfo = new HashMap<>();
        serverInfo.put("SERVER_NAME", request.getServerName());
        serverInfo.put("SERVER_ADDR", request.getLocalAddr());
        serverInfo.put("SERVER_PORT", String.valueOf(request.getServerPort()));
        serverInfo.put("SERVER_SIGNATURE", request.getServerName() + " Port " + request.getServerPort());
        return serverInfo;
    }

    /**
     * Retrieves request information from the HTTP request.
     *
     * @param request the incoming HTTP request
     * @return a map containing request details
     */
    private Map<String, String> getRequestInformation(HttpServletRequest request) {
        Map<String, String> requestInfo = new HashMap<>();
        requestInfo.put("REMOTE_ADDR", request.getRemoteAddr());
        requestInfo.put("REMOTE_PORT", String.valueOf(request.getRemotePort()));
        requestInfo.put("REQUEST_METHOD", request.getMethod());
        requestInfo.put("REQUEST_URI", request.getRequestURI());
        requestInfo.put("QUERY_STRING", request.getQueryString());
        requestInfo.put("REQUEST_SCHEME", request.getScheme());
        requestInfo.put("REQUEST_PROTOCOL", request.getProtocol());
        requestInfo.put("HTTPS", request.isSecure() ? "on" : "off");
        requestInfo.put("SSL_TLS_SNI", request.getServerName());
        return requestInfo;
    }

    /**
     * Retrieves servlet information from the HTTP request.
     *
     * @param request the incoming HTTP request
     * @return a map containing servlet details
     */
    private Map<String, String> getServletInformation(HttpServletRequest request) {
        Map<String, String> servletInfo = new HashMap<>();
        servletInfo.put("CONTEXT_PREFIX", request.getContextPath());
        servletInfo.put("CONTEXT_DOCUMENT_ROOT", getServletContext().getRealPath("/"));
        servletInfo.put("SCRIPT_FILENAME", getServletContext().getRealPath(request.getServletPath()));
        return servletInfo;
    }

    /**
     * Retrieves environment variables from the system.
     *
     * @return a map containing environment variable names and values
     */
    private Map<String, String> getEnvironmentVariables() {
        return System.getenv();
    }

    /**
     * Prints a section of information with a title and key-value pairs.
     *
     * @param out           the PrintWriter to write to
     * @param title         the title of the section
     * @param keyValuePairs the key-value pairs to print
     */
    private void printSection(PrintWriter out, String title, Map<String, String> keyValuePairs) {
        out.println("===== " + title + " =====");
        keyValuePairs.forEach((key, value) -> out.println(key + ": " + (value != null ? value : "N/A")));
        out.println();
    }
}
