package com.example.lightweightmongodbservlet;

import jakarta.servlet.http.HttpServletRequest;
import org.bson.Document;

import java.time.LocalDateTime;

/**
 * Creates a MongoDB Document object from the selected information of the HTTP request.
 * The created document includes the remote address, user agent, and the time of the request.
 *
 * @author Björn Forsberg
 */
public class LogDocumentCreator {

    /**
     * Creates a Document object from an HttpServletRequest object.
     * The created document includes details such as the remote address, user agent, and the current time.
     *
     * @param request HttpServletRequest object to create a Document from
     * @return Document object created from the HttpServletRequest
     */
    public Document create(HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        LocalDateTime currentTime = LocalDateTime.now();

        return new Document()
                .append("time", currentTime.toString())
                .append("REMOTE_ADDR", remoteAddress)
                .append("HTTP_USER_AGENT", userAgent);
    }
}
