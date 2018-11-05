package server;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailSender {
	
	//the code that will be verified as part of authentication
	private int code;
	
	private static final String FROM_EMAIL = "TrojanPassProtector@gmail.com";
	private static final String FROM_PASSWORD = "cardinalandgold";
	private static final String HOST = "smtp.gmail.com";
	
	//constructor takes user's email as param and sends email w/ random generated code attached
	public EmailSender(String recipient) {
		
		//generate random code
		Random rnd = new Random();
		this.code = 100000 + rnd.nextInt(900000);

		//set up email properties
		Properties properties = System.getProperties();

		properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", EmailSender.HOST);
        properties.put("mail.smtp.user", EmailSender.FROM_EMAIL);
        properties.put("mail.smtp.password", EmailSender.FROM_PASSWORD);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);

        //generate and send email
        try {
            message.setFrom(new InternetAddress(EmailSender.FROM_EMAIL));
            InternetAddress toEmail = new InternetAddress(recipient);

            message.addRecipient(Message.RecipientType.TO, toEmail);

            message.setSubject("Trojan Password Protector Authenticaton");
            message.setText("Your six-digit authentication code: " + this.code);
            Transport transport = session.getTransport("smtp");
            transport.connect(EmailSender.HOST, EmailSender.FROM_EMAIL, EmailSender.FROM_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            System.out.println("ae: " + ae.getMessage());
        }
        catch (MessagingException me) {
        	System.out.println("me: " + me.getMessage());
        }
	}
	
	public int getCode() {
		return this.code;
	}
}
