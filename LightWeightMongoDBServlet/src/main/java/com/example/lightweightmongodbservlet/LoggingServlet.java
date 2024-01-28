package com.example.lightweightmongodbservlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.Document;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Handles the logging of incoming requests and displaying of the log data.
 * It logs details of each visitor and displays the entire logs when a GET request is made.
 * It interacts with MongoDB via {@link DatabaseManager} to perform database operations.
 *
 * @author Björn Forsberg
 */
@WebServlet(name = "LoggingServlet", urlPatterns = "/")
public class LoggingServlet extends HttpServlet {

    private DatabaseManager databaseManager;
    private LogDocumentCreator logDocumentCreator;

    /**
     * Initializes DatabaseManager and LogDocumentCreator. This method is called
     * once when the servlet is instantiated.
     */
    @Override
    public void init() {
        databaseManager = new DatabaseManager();
        logDocumentCreator = new LogDocumentCreator();
    }

    /**
     * Handles the HTTP GET request. It logs the details of the visitor and then
     * sends the entire logs as the response.
     *
     * @param request  HttpServletRequest that encapsulates all request information
     * @param response HttpServletResponse that encapsulates all response information
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logVisitor(request);
        displayLogs(response);
    }

    /**
     * Logs the details of a visitor. It creates a document from the request and
     * inserts it into the database.
     *
     * @param request HttpServletRequest that encapsulates all request information
     */
    private void logVisitor(HttpServletRequest request) {
        Document document = logDocumentCreator.create(request);
        databaseManager.insertDocument(document);
    }

    /**
     * Writes all log entries to the response. It fetches all documents from the
     * database and writes them to the response.
     *
     * @param response HttpServletResponse that encapsulates all response information
     * @throws IOException if an I/O error occurs
     */
    private void displayLogs(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        for (Document document : databaseManager.findAllDocuments()) {
            writer.println(document.toJson());
        }
    }
}
