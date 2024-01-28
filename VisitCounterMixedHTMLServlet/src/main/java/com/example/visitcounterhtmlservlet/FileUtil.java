package com.example.visitcounterhtmlservlet;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Utility class providing static methods for managing a visit count stored in a file.
 * The class includes methods for file handling, read/write operations, file locking, and incrementing the visit count.
 * The class is used by {@link VisitCounterMixedHTMLServlet}.
 *
 * @author Björn Forsberg
 */
public final class FileUtil {

    private FileUtil() {
        throw new AssertionError("Cannot be instantiated");
    }

    /**
     * Increments the visit count by reading the count from a file, incrementing it, and writing it back to the file.
     *
     * @return the incremented visit count
     * @throws IOException if an error occurs while reading from or writing to the visit count file
     */
    public static int incrementVisitCount(String filePath) throws IOException {
        Path visitCountFile = getVisitCountFilePath(filePath);
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
    private static Path getVisitCountFilePath(String filePath) {
        return Paths.get(filePath);
    }

    /**
     * Ensures the visit count file exists, creating it if it doesn't.
     *
     * @param visitCountFile the path to the visit count file
     * @throws IOException if an error occurs while creating the file
     */
    private static void ensureFileExists(Path visitCountFile) throws IOException {
        if (!Files.exists(visitCountFile)) {
            Files.createFile(visitCountFile);
        }
    }

    /**
     * Reads the visit count from the visit count file.
     *
     * @param visitCountFile the path to the visit count file
     * @return the visit count
     * @throws IOException if an error occurs while reading from the file
     */
    private static int readVisitCount(Path visitCountFile) throws IOException {
        String fileContent = new String(Files.readAllBytes(visitCountFile));
        return fileContent.isEmpty() ? 0 : Integer.parseInt(fileContent.trim());
    }

    /**
     * Increments visit count by 1.
     *
     * @param count the count to increment
     * @return the incremented count
     */
    private static int incrementCount(int count) {
        return count + 1;
    }

    /**
     * Writes the visit count to the visit count file.
     *
     * @param fileChannel the file channel of the visit count file
     * @param visitCount  the visit count to write
     * @throws IOException if an error occurs while writing to the file
     */
    private static void writeVisitCount(FileChannel fileChannel, int visitCount) throws IOException {
        byte[] visitCountBytes = String.valueOf(visitCount).getBytes();
        fileChannel.truncate(0);
        fileChannel.write(java.nio.ByteBuffer.wrap(visitCountBytes), 0);
    }
}
