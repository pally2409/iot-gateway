package schooldomain.neu.pallaksingh.connecteddevices.labs.module07;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;

public class CoapClientConnector {
	
	//Initialize the CoapClient
	private CoapClient coapClient;
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(CoapClientConnector.class.getName());
	
	//Set the variable that defines the format of the type of messages we send in our PUT and POST request messages
	private static final int TEXT_PLAIN = 0;
	
	/*
	 * This method is used to create the CoapClient 
	 * 
	 * @params host				The URI of the CoAP server 
	 * @params resourceName		The resource directory at the CoAP server that one is talking to
	 * @returns 				A boolean indicating whether or not the client was successfully created
	 */
	public boolean createCoapClient(String host, String resourceName) {
		
		//Form a url string from the host and resourceName
		String clientURI = "coap://" + host + "/" + resourceName;
		
		//Try to create the CoAP client using the given host and resourceName
		try {
			
			//Instantiate the CoAP client
			this.coapClient = new CoapClient(clientURI);
			
			//Send a PING to check if valid host
			this.sendPING();	
		} 
		
		//If an error occurs during assignment and pinging due to invalid host name
		catch(Exception e) {
			
			//print the error
			e.printStackTrace();
			
			//return false indicating failure
			return false;	
		}
		
		//return True to indicate successful running if runs without errors
		return true;	
	}
	
	/*
	 * Method to send the GET request message to the CoAP server
	 * 
	 * @returns A boolean value indicating the success of sending the message
	 */
	public boolean sendGETRequest() {
		
		//Try to send the GET request message via the CoAP client created earlier
		try {
			
			//Get the response from the GET request
			CoapResponse resp = coapClient.get();
			
			//Log the response text
			LOGGER.info(resp.getResponseText());
		} 
		
		//If an error occurred during sending the request message
		catch(Exception e) {
			
			//print the error
			e.printStackTrace();
			
			//return false indicating failure
			return false;
		}
		
		//return True to indicate successful running if runs without errors
		return true;	
	}
	
	/*
	 * Method to send the PUT request message to the CoAP server
	 * 
	 * @returns A boolean value indicating the success of sending the message
	 */
	public boolean sendPUTRequest() {
		
		//Try to send the PUT request message via the CoAP client created earlier
		try {
		
			//Get the response from the PUT request
			CoapResponse resp = coapClient.put("TEST MESSAGE IN PUT REQUEST", TEXT_PLAIN);
			
			//Log the response text
			LOGGER.info(resp.getResponseText());
		}
		
		//If an error occurs during assignment and pinging due to invalid host name
		catch(Exception e) {
					
			//print the error
			e.printStackTrace();
					
			//return false indicating failure
			return false;	
		}
				
		//return True to indicate successful running if runs without errors
		return true;	
	}
	
	/*
	 * Method to send the POST request message to the CoAP server
	 * 
	 * @returns A boolean value indicating the success of sending the message
	 */
	public boolean sendPOSTRequest() {
		
		//Try to send the POST request message via the CoAP client created earlier
		try {
		
			//Get the response from the POST request
			CoapResponse resp = coapClient.post("TEST MESSAGE IN POST REQUEST", TEXT_PLAIN);
					
			//Log the response text
			LOGGER.info(resp.getResponseText());	
		}
		
		//If an error occurs during assignment and pinging due to invalid host name
		catch(Exception e) {
					
			//print the error
			e.printStackTrace();
					
			//return false indicating failure
			return false;	
		}
				
		//return True to indicate successful running if runs without errors
		return true;	
	}
	
	/*
	 * Method to send the DELETE request message to the CoAP server
	 * 
	 * @returns A boolean value indicating the success of sending the message
	 */
	public boolean sendDELETERequest() {
		
		//Try to send the DELETE request message via the CoAP client created earlier
		try {
		
			//Get the response from the DELETE request
			CoapResponse resp = coapClient.delete();
			
			//Log the response text
			LOGGER.info(resp.getResponseText());		
		}
		
		//If an error occurs during assignment and pinging due to invalid host name
		catch(Exception e) {
					
			//print the error
			e.printStackTrace();
					
			//return false indicating failure
			return false;	
		}
				
		//return True to indicate successful running if runs without errors
		return true;		
	}
	
	/*
	 * Method to send the PING message to the CoAP server
	 */
	public void sendPING() {
		
		//Get the response from the DELETE request
		boolean resp = coapClient.ping();
		
		//Log the response text
		LOGGER.info(String.valueOf(resp));		
	}
}
