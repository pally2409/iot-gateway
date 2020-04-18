//import packages and libraries
package schooldomain.neu.pallaksingh.connecteddevices.project;
import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.activation.*;

/**
 * This class provides an abstraction to all email related tasks using the Simple Mail Transfer Protocol
 * 
 * @author pallaksingh
 */
public class SmtpClientConnector {
	
	//Declare ConfigUtil for reading SMTP configuration from config file
	private static ConfigUtil config;
	
	//These variables are in class scope because they are needed by anonymous inner class of javax.mail.Authenticator()
	private String fromAddr; 				//sender's address
	private String authToken; 				//password
	
	/**
	 * The constructor is used to initialize the ConfigUtil object for reading configuration information
	 * 
	 * @param confUtil
	 * 			The reference to ConfigUtil for reading SMTP configuration
	 */
	public SmtpClientConnector(ConfigUtil confUtil) {
		
		//Initialize ConfigUtil for reading SMTP configuration from config file
		config 				= confUtil;
				
		//Initializing sender credentials from configuration file
		fromAddr 			= this.config.getValue("smtp.cloud", "fromAddr");
		authToken 			= this.config.getValue("smtp.cloud", "authToken");
	}
	
	/**
	 * Getter for the ConfigUtil instance
	 * @return		The current ConfigUtil instance for the class
	 */
	public ConfigUtil getcUtil() {
		
		//Return the current ConfigUtil instance
		return this.config;
	}
	
	/**
	 * Method for sending an email using the SMTP protocol
	 * 
	 * @param topic		
	 * 			Subject of the mail
	 * @param data
	 * 			Body of the mail
	 * @return Returns a boolean value indicating the success of the mail	
	 */
	public boolean publishMessage(String topic, String data) {
		
		/*
		 * Setting up attributes for sending e-mail notification
		 */
		String host;				//SMTP host
		int port; 					//Port number for SMTP process
		String toAddr;				//Receiver of the address
		
		//Try if the config file have valid values
		try {
			
		//Getting host and port number for opening socket connection
		host = this.config.getValue("smtp.cloud", "host");
		port = this.config.getIntegerValue("smtp.cloud", "port");
		
		//Getting receiver credentials from configuration file
        toAddr = this.config.getValue("smtp.cloud", "toAddr");
		} 
		
		//If found null due to invalid section, key, or invalid format
		catch(Exception e) {
			
			//Return false
			return false;
		}
        
        //Get the current system properties
        Properties properties = System.getProperties();
        
        //Set properties related to the email message
        properties.put("mail.smtp.host", host); 											//Host 
        properties.put("mail.smtp.starttls.enable", "true"); 								//switch connection to TLS protected connection
        properties.put("mail.smtp.user", fromAddr); 										//Set the sender
        properties.put("mail.smtp.password", authToken); 									//Set the password
        properties.put("mail.smtp.port", port); 											//Port for the SMTP process
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  //Create SMTP SSL socket
        properties.put("mail.smtp.auth", "true"); 										    //Enable need for authorization
        
        //Get new session with the properties and authenticator 
        //Anonymous inner class for authenticating the mail using the password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(fromAddr, authToken);
        	}
        });
        
        //Try to send the email using the attributes set above
        try {
        	
        	//Create the default MIME message 
        	MimeMessage message = new MimeMessage(session);
        	
        	//Set message attributes
            message.setFrom(new InternetAddress(fromAddr));									//Sender address
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));	//Receiver address
			message.setSubject(topic);														//Subject of email
			message.setText(data);															//Body of email
			
			//Get transport object that implements SMTP protocol
			Transport transport = session.getTransport("smtp");
			
			//Connect to the host with the given credentials
			transport.connect(host, Integer.valueOf(port), fromAddr, authToken);
			
			//Send the message
			transport.sendMessage(message, message.getAllRecipients());	
		} 
        
        //If an error occurred while sending the message
        catch (MessagingException mex) {
			
        	//Report the error stacktrace
			mex.printStackTrace();
			
			//Return false if found any exception such as failure in authentication indicating failure to publish message
			return false;
		}
        
        //Return true if mail is sent successfully indicating success in publishing message 
        return true;	
	}	
}
