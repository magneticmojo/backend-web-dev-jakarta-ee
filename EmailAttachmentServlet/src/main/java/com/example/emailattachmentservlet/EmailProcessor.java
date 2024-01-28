package com.example.emailattachmentservlet;

import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Prepares and sends an email by creating an instances of EmailMessage using the input parameters, configures the message
 * for sending (including attachments), and then sends the email using the Transport class from the Jakarta Mail API.
 * <p>
 * Creating the EmailMessage involves setting up a Session using properties loaded from a configuration file,
 * then creating the message with the necessary details and content.
 * <p>
 * Attachments are added using the MultipartEmail class. The Part instances that
 * represent the attachments are passed to the MultipartEmail's addAttachment method,
 * which adds them to the email content.
 *
 * @author  Björn Forsberg
 */
public class EmailProcessor {

    /**
     * This method receives the input parameters, creates an email message, configures it for
     * sending (including attachments), and sends the email.
     *
     * @param from the sender's email address
     * @param to the recipient's email address
     * @param cc the CC recipient's email address
     * @param bcc the BCC recipient's email address
     * @param subject the email subject
     * @param messageContent the email body
     * @param filePart1 the first attachment
     * @param filePart2 the second attachment
     * @throws ServletException if an error occurs during the processing of the email
     * @throws MessagingException if an error occurs during the creation of the email message or during the sending process
     */
    public void processEmail(String from, String to, String cc, String bcc, String subject, String messageContent, Part filePart1, Part filePart2) throws ServletException, MessagingException {
        Properties configProperties = getEmailConfigProperties();
        EmailMessage emailMessage = getEmailMessage(from, to, cc, bcc, subject, messageContent, configProperties);
        MultipartEmail multipartEmail = getMultipartEmail(messageContent, filePart1, filePart2);
        emailMessage.getMessage().setContent(multipartEmail.getMultipart());
        sendEmailMessage(emailMessage);
    }

    /**
     * This method is used to send the email message. It uses the Transport.send() method from
     * Jakarta Mail API to perform the actual sending.
     *
     * @param emailMessage The email message to be sent.
     * @throws ServletException if an error occurs during the email sending process.
     */
    private void sendEmailMessage(EmailMessage emailMessage) throws ServletException {
        try {
            Transport.send(emailMessage.getMessage());
        } catch (MessagingException e) {
            throw new ServletException("Error sending email.", e);
        }
    }

    /**
     * This method creates a MultipartEmail object and adds the specified file attachments to it.
     * A MultipartEmail represents an email that can contain both text and attachments.
     *
     * @param messageContent the email body
     * @param filePart1 the first file to be attached to the email
     * @param filePart2 the second file to be attached to the email
     * @return A MultipartEmail object containing the text and attachments.
     * @throws ServletException if an error occurs during the creation of the multipart email.
     */
    private MultipartEmail getMultipartEmail(String messageContent, Part filePart1, Part filePart2) throws ServletException {
        MultipartEmail multipartEmail;
        try {
            multipartEmail = new MultipartEmail(messageContent);
            multipartEmail.addAttachment(filePart1);
            multipartEmail.addAttachment(filePart2);
        } catch (MessagingException | IOException e) {
            throw new ServletException("Error creating multipart email.", e);
        }
        return multipartEmail;
    }

    /**
     * This method creates an EmailMessage object. An EmailMessage represents an email that is ready to be sent.
     * It is created using a Session object, the email details and content, and the email configuration properties.
     *
     * @param from the sender's email address
     * @param to the recipient's email address
     * @param cc the CC recipient's email address
     * @param bcc the BCC recipient's email address
     * @param subject the email subject
     * @param messageContent the email body
     * @param configProperties the properties for email configuration
     * @return An EmailMessage object that represents the email to be sent.
     * @throws ServletException if an error occurs during the creation of the email message.
     */
    private EmailMessage getEmailMessage(String from, String to, String cc, String bcc, String subject, String messageContent, Properties configProperties) throws ServletException {
        EmailConfiguration emailConfig = new EmailConfiguration(configProperties);
        EmailMessage emailMessage;
        try {
            emailMessage = new EmailMessage(emailConfig.configureEmailSession(), from, to, cc, bcc, subject, messageContent);
        } catch (MessagingException e) {
            throw new ServletException("Error creating email message.", e);
        }
        return emailMessage;
    }

    /**
     * This method is used to load the properties for email configuration from a configuration file.
     * The properties are stored in a Properties object, which is returned by this method.
     *
     * @return A Properties object containing the properties for email configuration.
     * @throws ServletException if an error occurs during the loading of the email configuration.
     */
    private Properties getEmailConfigProperties() throws ServletException {
        Properties configProperties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            configProperties.load(input);
        } catch (IOException e) {
            throw new ServletException("Error loading email configuration.", e);
        }
        return configProperties;
    }
}
