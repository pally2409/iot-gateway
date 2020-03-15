//Import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module07;

/*
 * Class that instantiates CoapServerManager and calls the startServer of the CoapServerManager which in turn
 * creates and starts the CoapServer
 */
public class CoapServerApp {

	/*
	 * Main method; the starting point for server side operations for module 7
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		/*
//		 * MODULE 7
//		 */
//		
		//Instantiate CoapServerManager
		CoapServerManager coapServerManager = new CoapServerManager();
		
		//Add the resource handler to the CoapServer instance 
		coapServerManager.addResource(new TempResourceHandler("Temp"));
				
		//Start the CoAP Server
		coapServerManager.startServer();
	}
}