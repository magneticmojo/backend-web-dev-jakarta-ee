package com.example.envvarshtmlservlet;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A utility class that collects various information about an HTTP request
 * and the client's environment, such as header details, server information, request details, servlet information,
 * and environment variables. This information is returned in a map.</p>
 *
 * @author Björn Forsberg
 */
public class RequestInfoCollector {

    private RequestInfoCollector() {
        throw new AssertionError("Cannot be instantiated");
    }

    /**
     * Collects all information related to the request, including headers, server info, request info,
     * servlet info, and environment variables.
     *
     * @param request the HttpServletRequest from which information is collected
     * @return a map containing all collected information
     */
    public static Map<String, String> collectAllInformation(HttpServletRequest request) {
        Map<String, String> allInfo = new LinkedHashMap<>();
        allInfo.putAll(collectHeaderInformation(request));
        allInfo.putAll(collectServerInformation(request));
        allInfo.putAll(collectRequestInformation(request));
        allInfo.putAll(collectServletInformation(request));
        allInfo.putAll(collectEnvironmentVariables());
        return allInfo;
    }

    /**
     * Collects header information from the HttpServletRequest.
     *
     * @param request the HttpServletRequest from which information is collected
     * @return a map containing header information
     */
    private static Map<String, String> collectHeaderInformation(HttpServletRequest request) {
        Map<String, String> headers = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName.toUpperCase(), headerValue);
        }
        return headers;
    }

    /**
     * Collects server information from the HttpServletRequest.
     *
     * @param request the HttpServletRequest from which information is collected
     * @return a map containing server information
     */
    private static Map<String, String> collectServerInformation(HttpServletRequest request) {
        Map<String, String> serverInfo = new LinkedHashMap<>();
        serverInfo.put("SERVER_NAME", request.getServerName());
        serverInfo.put("SERVER_ADDR", request.getLocalAddr());
        serverInfo.put("SERVER_PORT", String.valueOf(request.getServerPort()));
        serverInfo.put("SERVER_SIGNATURE", request.getServerName() + " Port " + request.getServerPort());
        return serverInfo;
    }

    /**
     * Collects request information from the HttpServletRequest.
     *
     * @param request the HttpServletRequest from which information is collected
     * @return a map containing request information
     */
    private static Map<String, String> collectRequestInformation(HttpServletRequest request) {
        Map<String, String> requestInfo = new LinkedHashMap<>();
        requestInfo.put("REMOTE_ADDR", request.getRemoteAddr());
        requestInfo.put("REMOTE_PORT", String.valueOf(request.getRemotePort()));
        requestInfo.put("REQUEST_METHOD", request.getMethod());
        requestInfo.put("REQUEST_URI", request.getRequestURI());
        requestInfo.put("QUERY_STRING", request.getQueryString());
        requestInfo.put("REQUEST_SCHEME", request.getScheme());
        requestInfo.put("REQUEST_PROTOCOL", request.getProtocol());
        requestInfo.put("HTTPS", "on");
        requestInfo.put("SSL_TLS_SNI", request.getServerName());
        return requestInfo;
    }

    /**
     * Collects servlet information from the HttpServletRequest.
     *
     * @param request the HttpServletRequest from which information is collected
     * @return a map containing servlet information
     */
    private static Map<String, String> collectServletInformation(HttpServletRequest request) {
        Map<String, String> servletInfo = new LinkedHashMap<>();
        servletInfo.put("CONTEXT_PREFIX", request.getContextPath());
        servletInfo.put("CONTEXT_DOCUMENT_ROOT", request.getServletContext().getRealPath("/"));
        servletInfo.put("SCRIPT_FILENAME", request.getServletContext().getRealPath(request.getServletPath()));
        return servletInfo;
    }

    /**
     * Collects environment variables from the system.
     *
     * @return a map containing environment variables
     */
    private static Map<String, String> collectEnvironmentVariables() {
        Map<String, String> envVariables = System.getenv();
        Map<String, String> envInfo = new LinkedHashMap<>();
        envInfo.put("PATH", envVariables.get("PATH"));
        return envInfo;
    }
}
