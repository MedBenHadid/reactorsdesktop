package Packages.Mohamed.util;

import Packages.Mohamed.Entities.Mission;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class
sendMail {

    public static void sendMail(String recepient, Mission mission) throws Exception {
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
        Message message = prepareMessage(session, myAccountEmail, recepient, mission);

        //Send mail
        assert message != null;
        Transport.send(message);
        System.out.println("Message sent successfully");
    }

    private static Message prepareMessage(Session session, String myAccountEmail, String recepient, Mission mission) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Nouvelle mission Reactors !");
            String htmlCode = "<h1> Votre nouvelle mission : </h1> <br/> <h2><b>" + mission.getTitleMission() + " </b></h2>";
            htmlCode += "<h2> Decription : </h2>" + mission.getDescription() + " </p><br>";
            htmlCode += "<h4> debut : " + mission.getDateCreation() + " </h4>";
            htmlCode += "<h4> fin : " + mission.getDateFin() + " </h4>";
            htmlCode += "<h4> Location : " + mission.getLocation() + " </h4>";
            htmlCode += "<h3> Cordiallement, </h3>";

            message.setContent(htmlCode, "text/html");
            return message;
        } catch (Exception ex) {
            Logger.getLogger(sendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


    public static void sendMailToAdmin(String sender, String recepient, String value) throws Exception {
        System.out.println("Preparing to send email to admin");
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
        Message message = prepareMessageToAdmin(session, myAccountEmail, value, recepient, sender);

        //Send mail
        assert message != null;
        Transport.send(message);
        System.out.println("Message sent successfully");
    }

    private static Message prepareMessageToAdmin(Session session, String myAccountEmail, String value, String To, String from) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("TestPIdevJava@gmail.com"));
            message.setSubject("Contact.Reactors !");
            String htmlCode = "<h1> Message de : </h1> <br/> <h2><b>" + from + " </b></h2>";
            htmlCode += "<h2> To : </h2>" + To + " </p><br>";
            htmlCode += "<h4> Le message : " + value + " </h4><br/>";
            htmlCode += "<h3> Cordiallement, </h3>";

            message.setContent(htmlCode, "text/html");
            return message;
        } catch (Exception ex) {
            Logger.getLogger(sendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
