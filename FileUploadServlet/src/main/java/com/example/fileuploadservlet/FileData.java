package com.example.fileuploadservlet;

import java.io.Serializable;

/**
 * Represents a file's metadata and content used in file upload operations.
 * This class is responsible for encapsulating the file's name, MIME type, size,
 * and the actual byte content of the file. It's designed to be used together with {@link FileUploadServlet}.
 *
 * @author Björn Forsberg
 *
 */
public class FileData implements Serializable {
    private String fileName;
    private String mimeType;
    private long fileSize;
    private byte[] fileContent;

    public FileData(String fileName, String mimeType, long fileSize) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
    }

    public FileData(String fileName, String mimeType, long fileSize, byte[] fileContent) {
        this(fileName, mimeType, fileSize);
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public String toString() {
        return "FileData{" +
                "fileName='" + fileName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", fileSize=" + fileSize +
                "}";
    }
}

