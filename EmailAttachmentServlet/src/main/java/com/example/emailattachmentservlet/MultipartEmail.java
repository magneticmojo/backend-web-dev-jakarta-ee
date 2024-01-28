package com.example.emailattachmentservlet;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.Part;

import java.io.IOException;

/**
 * Creates and handles multipart emails by using the Jakarta Mail API
 * to build an email with both text and file attachments. The email content
 * is treated as a multipart message where each part is either the text content or
 * an attachment.
 *
 * @author Björn Forsberg
 */
public class MultipartEmail {

    private final Multipart multipart;

    /**
     * Constructor for creating a new MultipartEmail. This constructor takes the text content
     * of the email and creates a multipart email with a single body part for the text.
     *
     * @param messageContent The text content of the email.
     * @throws MessagingException if there are any issues with the creation of the text part.
     */
    public MultipartEmail(String messageContent) throws MessagingException {
        this.multipart = new MimeMultipart();
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(messageContent);
        multipart.addBodyPart(textPart);
    }

    /**
     * This method adds a file attachment to the multipart email. It first checks if
     * the provided file part is valid and if it is, a new body part is created for the
     * attachment and added to the multipart email.
     *
     * @param filePart The file part to be attached to the email.
     * @throws IOException if there are any issues with reading the file part.
     * @throws MessagingException if there are any issues with the creation of the attachment part.
     */
    public void addAttachment(Part filePart) throws IOException, MessagingException {
        if (filePart != null && filePart.getSize() > 0) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(filePart.getInputStream(), filePart.getContentType());
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName(filePart.getSubmittedFileName());
            multipart.addBodyPart(attachmentPart);
        }
    }

    /**
     * This method returns the Multipart object which represents the email content.
     *
     * @return The Multipart object of the email.
     */
    public Multipart getMultipart() {
        return multipart;
    }
}
