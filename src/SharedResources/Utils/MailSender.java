package SharedResources.Utils;

import Main.Entities.User;
import Packages.Chihab.Models.Entities.Association;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailSender {
    public static void sendMail(String recepient, Association a, User manager, String relayed, String plainPassword) throws Exception {
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        //Enable authentication
        properties.put("mail.smtp.auth", "true");
        //Set TLS encryption enabled
        properties.put("mail.smtp.starttls.enable", "true");
        //Set SMTP host
        properties.put("mail.smtp.host", "smtp.gmail.com");
        //Set smtp port
        properties.put("mail.smtp.port", "587");

        //Your gmail address
        String myAccountEmail = "TestPIdevJava@gmail.com";
        //Your gmail password
        String password = "PIdev12345678";

        //Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        //Prepare email message
        Message message = prepareMessage(session, myAccountEmail, recepient, a, manager, relayed, plainPassword);

        //Send mail
        assert message != null;
        Transport.send(message);
        System.out.println("Message sent successfully");
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient, Association a, User manager, String relayed, String plainPassword) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Association" + a.getNom() + " created !");
            String htmlCode = "<h1> You are now a " + a.getNom() + "member " + "name" + ": </h1> <br/> <h2><b> Role " +
                    "" +
                    "" + a.getNom() + " </b></h2>";
            message.setContent(htmlCode, "text/html");
            return message;
        } catch (Exception ex) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, "Error parsing message", ex);
        }
        return null;
    }
}
