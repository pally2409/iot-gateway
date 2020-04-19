// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.project;
import org.eclipse.paho.client.mqttv3.MqttException;
/**
 * Class that instantiates GatewayDataManager and calls the run method of the GatewayDataManager which in turn
 * listens for new sensor data from the constrained device, processes it and publishes it to Ubidots and triggers 
 * appropriate actuation on the constrained device
 * 
 * @author pallaksingh
 */
public class GatewayHandlerApp {

	/**
	 * Main method; the starting point for gateway device operations for the Fall Detection System. It starts the GatewayDataManager 
	 * that is the starter of all the main operations of the project.
	 **/
	public static void main(String[] args) throws MqttException {
		// TODO Auto-generated method stub
		
		//Instantiate GatewayDataManager
		GatewayDataManager  gatewayDataManager = new GatewayDataManager();
		
		//Run the gateway data manager
		gatewayDataManager.run();
	}
}