package com.example.emailattachmentservlet;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Creates an email message using the Jakarta Mail API. This class handles
 * the creation of a MimeMessage object, setting the sender and recipient(s) of the email,
 * and defining the subject and body of the email.
 *
 * @author Björn Forsberg
 */
public class EmailMessage {

    private final MimeMessage message;

    /**
     * Constructor for creating a new EmailMessage. This constructor takes the necessary
     * details of the email and sets up a MimeMessage object accordingly.
     *
     * @param session The mail Session.
     * @param from The sender's email address.
     * @param to The recipient's email address.
     * @param cc The CC recipient's email address.
     * @param bcc The BCC recipient's email address.
     * @param subject The subject of the email.
     * @param messageContent The body of the email.
     * @throws MessagingException if there are any issues with the creation of the MimeMessage object.
     */
    public EmailMessage(Session session, String from, String to, String cc, String bcc, String subject, String messageContent) throws MessagingException {
        this.message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        if (cc != null && !cc.isEmpty()) {
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
        }
        if (bcc != null && !bcc.isEmpty()) {
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
        }
        message.setSubject(subject);
        message.setText(messageContent);
    }

    /**
     * This method returns the MimeMessage object which represents the email.
     *
     * @return The MimeMessage object of the email.
     */
    public MimeMessage getMessage() {
        return this.message;
    }
}

