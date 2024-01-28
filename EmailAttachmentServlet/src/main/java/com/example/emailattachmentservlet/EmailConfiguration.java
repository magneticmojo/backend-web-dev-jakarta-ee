package com.example.emailattachmentservlet;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

import java.util.Properties;

/**
 * Handles email configuration, especially creating a mail session
 * with the necessary properties for sending an email. It retrieves email configuration
 * details from a properties object.
 *
 * @author Björn Forsberg
 */
public class EmailConfiguration {

    private final Properties configProperties;

    /**
     * Constructor that initializes an EmailConfiguration object with a given set of properties.
     *
     * @param configProperties Properties object containing email configuration details.
     */
    public EmailConfiguration(Properties configProperties) {
        this.configProperties = configProperties;
    }

    /**
     * Configures and returns an email Session object using the properties specified during
     * the creation of this EmailConfiguration object.
     *
     * @return Configured Session object ready for creating MimeMessage objects.
     */
    public Session configureEmailSession() {
        String emailAddress = configProperties.getProperty("email.address");
        String emailPassword = configProperties.getProperty("email.password");

        Properties properties = getProperties();

        return Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, emailPassword);
            }
        });
    }

    /**
     * Creates and returns a Properties object containing the properties necessary
     * for sending an email via SMTP.
     *
     * @return Properties object with SMTP configuration.
     */
    private Properties getProperties() {
        Properties properties = new Properties();
        String host = "smtp.gmail.com";
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return properties;
    }
}
