package com.example.visitcounterservlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles the counting of visits to a web application, incrementing the count each time a GET request
 * is made to the root of the application ("/"). The count is persisted to a file which enables
 * server restarts without data loss.
 *
 * @author Björn Forsberg
 */

@WebServlet(name = "VisitCounterServlet", urlPatterns = "/")
public class VisitCounterServlet extends HttpServlet {

    /**
     * Handles a GET request by responding with the current visit count.
     *
     * @param request  the incoming HTTP request
     * @param response the outgoing HTTP response
     * @throws IOException if an error occurs while reading from or writing to the visit count file
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        int visitCount = incrementVisitCount();
        out.println(visitCount);
    }

    /**
     * Increments the visit count by reading the count from a file, incrementing it, and writing it back to the file.
     *
     * @return the incremented visit count
     * @throws IOException if an error occurs while reading from or writing to the visit count file
     */
    private int incrementVisitCount() throws IOException {
        Path visitCountFile = getVisitCountFilePath();
        ensureFileExists(visitCountFile);

        try (FileChannel fileChannel = FileChannel.open(visitCountFile, StandardOpenOption.READ, StandardOpenOption.WRITE);
             FileLock lock = fileChannel.lock()) {

            int visitCount = readVisitCount(visitCountFile);
            visitCount = incrementCount(visitCount);
            writeVisitCount(fileChannel, visitCount);
            return visitCount;
        }
    }

    /**
     * Gets the file path of the visit count file.
     *
     * @return the file path of the visit count file
     */
    private Path getVisitCountFilePath() {
        return Paths.get("visit_count.txt");
    }

    /**
     * Ensures the visit count file exists, creating it if it doesn't.
     *
     * @param visitCountFile the path to the visit count file
     * @throws IOException if an error occurs while creating the file
     */
    private void ensureFileExists(Path visitCountFile) throws IOException {
        if (!Files.exists(visitCountFile)) {
            Files.createFile(visitCountFile);
        }
    }

    /**
     * Reads the visit count from the visit count file. If the file is empty, 0 is returned.
     *
     * @param visitCountFile the path to the visit count file
     * @return the visit count
     * @throws IOException if an error occurs while reading from the file
     */
    private int readVisitCount(Path visitCountFile) throws IOException {
        String fileContent = new String(Files.readAllBytes(visitCountFile));
        return fileContent.isEmpty() ? 0 : Integer.parseInt(fileContent.trim());
    }

    /**
     * Increments a count by 1.
     *
     * @param count the count to increment
     * @return the incremented count
     */
    private int incrementCount(int count) {
        return count + 1;
    }

    /**
     * Writes the visit count to the visit count file.
     *
     * @param fileChannel the file channel of the visit count file
     * @param visitCount  the visit count to write
     * @throws IOException if an error occurs while writing to the file
     */
    private void writeVisitCount(FileChannel fileChannel, int visitCount) throws IOException {
        byte[] visitCountBytes = String.valueOf(visitCount).getBytes();
        int bytesToWrite = visitCountBytes.length;
        fileChannel.truncate(0);
        int bytesWritten = fileChannel.write(java.nio.ByteBuffer.wrap(visitCountBytes), 0);

        if (bytesWritten != bytesToWrite) {
            throw new IOException("Not all bytes were written to the file. Expected: " + bytesToWrite + " but wrote: " + bytesWritten);
        }
    }
}
