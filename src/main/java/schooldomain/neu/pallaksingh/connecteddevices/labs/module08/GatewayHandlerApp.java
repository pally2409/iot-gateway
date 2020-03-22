// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module08;

import org.eclipse.paho.client.mqttv3.MqttException;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/*
 * Class that instantiates GatewayDataManager and calls the run method of the GatewayDataManager which in turn
 * listens for new sensor data from the constrained device and publishes it to Ubidots and listens for ActuatorData 
 * from Ubidots to send back to the constrained device
 */
public class GatewayHandlerApp {

	/*
	 * Main method; the starting point for gateway device operations for module 8
	 */
	public static void main(String[] args) throws MqttException {
		// TODO Auto-generated method stub
		
//		/*
//		 * MODULE 8
//		 */
//		
		//Instantiate GatewayDataManager
		GatewayDataManager  gatewayDataManager = new GatewayDataManager();
		
		//Run the gateway data manager
		gatewayDataManager.run();
	}
}