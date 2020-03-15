package schooldomain.neu.pallaksingh.connecteddevices.labs.module07;

/*
 * Class that instantiates and calls CoapClientConnector's various methods for sending various types 
 * of request messages (GET, PUSH, PUT, DELETE) to the CoAP server
 */
public class CoapClientApp {
	
	//Set the parameters required to connect and send request messages to the CoAP server
	private static final String HOST_SERVER = "pallypi.lan";				//The Server URI
	private static final String RESOURCE_NAME = "other/block";				//The Server URI
	
	/*
	 * Main method; the starting point for client side operations for module 7
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Instantiate CoapClientConnector
		CoapClientConnector coapClientConnector = new CoapClientConnector();
		
		//create the client using the host and resource name
		coapClientConnector.createCoapClient(HOST_SERVER, RESOURCE_NAME);
		
		//Send various type of CoAP request messages to the CoAP server
		coapClientConnector.sendGETRequest();			//The GET request
		coapClientConnector.sendPOSTRequest();			//The POST request
		coapClientConnector.sendPUTRequest();			//The PUT request
		coapClientConnector.sendDELETERequest();		//The DELETE request
	}
}
