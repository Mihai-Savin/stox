package com.mihaisavin.stox.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@RestController
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private boolean enabled = false;
    private String emailsSenderAccount;
    private String emailsSenderPassword;

    public boolean sendEmail(String recipient, String content) {

        if (!enabled) {
            LOGGER.warn("Emailing is disabled");
            LOGGER.warn("Would have sent email notification to: " + recipient + "\n\n");
            LOGGER.warn("With following message:\n\n " + content);

            return false;
        }

        final String username = emailsSenderAccount;
        final String password = emailsSenderPassword;

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(emailsSenderAccount));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject("Stox Alert");
            message.setText("Dear " + recipient
                    + "\n\n " + content
                    + "\n\n Thank you for using our service."
                    + "\n\n Kind regards,"
                    + "\n\n Stox.com Team"
            );

            Transport.send(message);

            LOGGER.info("Email sent to " + recipient + ".");
        } catch (MessagingException e) {
            LOGGER.error("Email was NOT sent due to an error.");
            return false;
        }
        return true;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmailsSenderAccount() {
        return emailsSenderAccount;
    }

    public void setEmailsSenderAccount(String emailsSenderAccount) {
        this.emailsSenderAccount = emailsSenderAccount;
    }

    public String getEmailsSenderPassword() {
        return emailsSenderPassword;
    }

    public void setEmailsSenderPassword(String emailsSenderPassword) {
        this.emailsSenderPassword = emailsSenderPassword;
    }

}