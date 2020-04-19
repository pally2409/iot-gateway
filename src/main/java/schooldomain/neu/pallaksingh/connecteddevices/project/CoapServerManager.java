//Import packages and libraries
package schooldomain.neu.pallaksingh.connecteddevices.project;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;

/**
 * The CoapServerManager class provides an abstraction to all server management tasks such as
 * creating the server, adding the resource handler, starting the server and stopping the server
 * 
 * @author pallaksingh
 */
public class CoapServerManager {
	
	//Initialize the CoapServer
	private CoapServer coapServer;
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(CoapServerManager.class.getName());
	
	//The constructor is used to instantiate the CoapServer
	public CoapServerManager() {
		super();
		
		//Instantiate the CoapServer
		this.coapServer = new CoapServer();
	}
	
	/**
	 * This method adds the resource passed as the parameter to the CoapServer. It validates
	 * whether the resource is not a null and only then adds it to the CoapServer
	 * 
	 * @returns A boolean value indicating the success in adding of resource
	 */
	public boolean addResource(CoapResource resource) {
		
		//Check if the passed resource is a null
		if (resource!=null) {
			
			//If not, add it to the CoapServer reference
			this.coapServer.add(resource);
			
			//Return a true to indicate successful adding of resource
			return true;
		}
		
		//If passed a null in the argument
		else {
			
			//Return a false to indicate failure in adding of resource
			return false;
		}	
	}
	
	/**
	 * Method to start the CoapServer. It checks whether the coapServer is null (hasn't been instantiated), 
	 * if a resource handler hasn't been added to it, it does so by instantiating the TempResourceHandler 
	 * and setting the name to "Temp" representing temperature
	 * 
	 * @returns A true value because this method does not fail
	 */
	public boolean startServer()   {
		
		//Check if the coapServer reference is null
		if(coapServer == null) {
			
			//Instantiate the CoapServer
			this.coapServer = new CoapServer();

		}
		
		//Log the message that the server has been started
		LOGGER.info("Starting the CoAP server");
		
		//Start the coapServer
		coapServer.start();
		
		//Always returns a true because this method does not fail
		return true;
	}
	
	/**
	 * Method to stop the CoapServer. 
	 * 
	 * @returns A true value because this method does not fail
	 */
	public boolean stopServer()   {
		
		//Log the message that the server has been stopped
		LOGGER.info("Stopping the CoAP server");
		
		//Start the coapServer
		coapServer.stop();
		
		//Always returns a true because this method does not fail
		return true; 
	}
}
