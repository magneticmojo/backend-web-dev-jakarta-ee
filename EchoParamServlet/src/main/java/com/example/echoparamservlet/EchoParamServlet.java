package com.example.echoparamservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A servlet that echos parameters back to the client from a GET request.
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "echoParamServlet", urlPatterns = "/echo-param-servlet")
public class EchoParamServlet extends HttpServlet {

    /**
     * Handles a GET request. Echoes back all parameters in the request.
     *
     * @param request  the incoming HttpServletRequest
     * @param response the outgoing HttpServletResponse
     * @throws IOException if an I/O error occurred
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Map<String, String[]> parameterMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();

            for (String value : values) {
                out.println(name + ": " + value);
            }
        }
    }
}