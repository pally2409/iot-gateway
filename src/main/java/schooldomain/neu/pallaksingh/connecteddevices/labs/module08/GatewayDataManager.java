//Import packages and modules
package schooldomain.neu.pallaksingh.connecteddevices.labs.module08;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttException;

/*
 * Class responsible for starting all operations of Module 08. These include listening for new sensor data from the
 * constrained device, publishing it to Ubidots, listening for Actuator Data from Ubidots and publishing it to the 
 * constrained device
 */
public class GatewayDataManager {
	
	//Declare UbidotsClientConnector for sending SensorData to Ubidots and listening for ActuatorData from ubidots
	static UbidotsClientConnector ubidotsClientConnector;
	
	//Declare MQTTClientConnector for listening for SensorData from constrained device
	static MqttClientConnector mqttClientConnector;
	
	//Declare SensorDataListener which is the callback for SensorData from constrained device
	static SensorDataListener sensorDataListener;
	
	//Declare ActuatorValueListener which is the callback for Actuator values from Ubidots
	static ActuatorValueListener actuatorValueListener;
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(GatewayDataManager.class.getName());

	//Threads required for the two communication tasks
	Thread sensorListenerThread;			//Thread for listening to incoming SensorData from Constrained Device and publishing to Ubidots
	Thread ubiDataListenerThread;			//Thread for listening to incoming ActuatorData from Ubidots and publishing to Constrained Device
	
	/*
	 * The constructor instantiates the classes needed for operations of this class
	 */
	public GatewayDataManager() throws MqttException {
		super();
		// TODO Auto-generated constructor stub
		
		//Instantiates UbidotsClientConnector for sending SensorData to Ubidots and listening for ActuatorData from ubidots
		ubidotsClientConnector 		= new UbidotsClientConnector() ;
		
		//Instantiates MQTTClientConnector for listening for SensorData from constrained device
		mqttClientConnector 		= new MqttClientConnector("tcp://broker.mqttdashboard.com:1883");
		
		//Instantiate SensorDataListener which is the callback for SensorData from constrained device
		sensorDataListener 			= new SensorDataListener(ubidotsClientConnector) ;
		
		//Instantiate ActuatorValueListener which is the callback for Actuator values from Ubidots
		actuatorValueListener 		= new ActuatorValueListener();		
	}
	
	/*
	 * Method to create and start threads for device to device communication and device to Ubidots communication
	 */
	public void run() {
		
		//Instantiate the thread for listening to incoming SensorData from Constrained Device and publishing to Ubidots
		this.sensorListenerThread = new Thread(new Runnable() {

			//The run method subscribes to SensorData 
			public void run() {
				
				//Log the message
				LOGGER.info("Initialized sensorListenerThread");
				
				//Subscribe to SensorData from constrained device using Mqtt
				mqttClientConnector.subscribeToSensorData("Connected-Devices/Sensor_Data", sensorDataListener, 1);	
			}
		});
		
		//Instantiate the thread for listening to incoming ActuatorData from Ubidots and publishing to Constrained Device
		this.ubiDataListenerThread = new Thread(new Runnable() {

			//The run method subscribes to ActuatorData
			public void run() {
				
				//Log the message
				LOGGER.info("Initialized ubiDataListenerThread");
				
				//Subscribe to ActuatorData from constrained device using Mqtt
				ubidotsClientConnector.subscribeToTopic("/v1.6/devices/iot-device/tempactuator", actuatorValueListener, 1);	
			}
		});
		
		//Start the two threads
		sensorListenerThread.start();
		ubiDataListenerThread.start();	
	}
}
