package schooldomain.neu.pallaksingh.connecteddevices.labs.module02;

//import libraries
import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.activation.*;


public class SmtpClientConnector {
	
	// instantiate ConfigUtil for reading SMTP configuration from config file
	private ConfigUtil config = new ConfigUtil();
	
	// these variables are in class scope because they are needed by anonymous inner class of javax.mail.Authenticator()
	private String fromAddr; //sender's address
	private String authToken; //password
	
	
	public SmtpClientConnector() {
		
		//initializing sender credentials from configuration file
		fromAddr = this.config.getValue("smtp.cloud", "fromAddr");
		authToken = this.config.getValue("smtp.cloud", "authToken");
		
	}
	
	// getters and setters
	public ConfigUtil getConfig() {
		return config;
	}


	public void setConfig(ConfigUtil config) {
		this.config = config;
	}


	public String getFromAddr() {
		return fromAddr;
	}


	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}



	// method for sending mail
	public boolean publishMessage(String topic, String data) {
		
		/*
		 * Setting up attributes for sending e-mail notification
		 */
		
		String host;
		int port; 
		String toAddr;
		
		//try if the config file have valid values
		try {
			
		//getting host and port number for opening socket connection
		host = this.config.getValue("smtp.cloud", "host");
		port = this.config.getIntegerValue("smtp.cloud", "port");
		
		//getting receiver credentials from configuration file
        toAddr = this.config.getValue("smtp.cloud", "toAddr");
        
		} 
		
		//if found null due to invalid section, key, or invalid format
		catch(Exception e) {
			
			//return false
			return false;
		}
        
        //get the current system properties
        Properties properties = System.getProperties();
        
        //set properties related to the email message
        properties.put("mail.smtp.host", host); //host
        properties.put("mail.smtp.starttls.enable", "true"); //switch connection to TLS protected connection
        properties.put("mail.smtp.user", fromAddr); //set the sender
        properties.put("mail.smtp.password", authToken); //set the password
        properties.put("mail.smtp.port", port); //smtp port
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //create smtp ssl socket
        properties.put("mail.smtp.auth", "true"); //authorization needed
        
        //get new session with the properties and authenticator 
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(fromAddr, authToken);
        	}
        });
        
        try {
        	
        	//create the default MIME message 
        	MimeMessage message = new MimeMessage(session);
        	
        	//set message attributes
            message.setFrom(new InternetAddress(fromAddr));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
			message.setSubject(topic);
			message.setText(data);
			
			//get transport object that implements smtp protocol
			Transport transport = session.getTransport("smtp");
			
			//connect to the host with the given credentials
			transport.connect(host, Integer.valueOf(port), fromAddr, authToken);
			
			//send the message
			transport.sendMessage(message, message.getAllRecipients());
			
			
		} catch (MessagingException mex) {
			// TODO Auto-generated catch block
			mex.printStackTrace();
			
			//return false if found any exception such as failure in authentication indicating failure to publish message
			return false;
		}
        
        //return true if mail is sent successfully indicating success in publishing message 
        return true;
		
	}	

}
