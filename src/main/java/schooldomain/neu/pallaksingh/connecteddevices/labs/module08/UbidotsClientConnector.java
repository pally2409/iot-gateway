package schooldomain.neu.pallaksingh.connecteddevices.labs.module08;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.labbenchstudios.iot.common.CertManagementUtil;
import com.ubidots.*;
import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/*
 * UbidotsClientConnector class provides abstraction for all Ubidots related tasks such as updating value of variables via
 * both MQTT with TLS support and the Ubidots API. It also subscribes for update to variables using MQTT
 */
public class UbidotsClientConnector {
		
	//Declare ConfigUtil to take care of API credentials
	ConfigUtil cUtil;
	
	//Declare the API client that will communicate with the API for publishing humidity sensor readings
	ApiClient api;
	
	//Declare the data source for the constrained device
	DataSource dataSource;
	
	//Declare the variable IDs for the TempSensor and TempActuator
	String TEMP_SENSOR_LABEL; 	//TempSensor Label
	String HUMIDITY_SENSOR_ID; 	//HumiditySensor ID
	String TEMP_ACTUATOR_LABEL; //TempActuator Label
	
	//Declare MqttClientConnector for publishing temperature sensor readings and register a listener for actuator value changes
	MqttClientConnector mqttClientConnector;
	
	//Declare a String for MqttTopic for ubidots
	String MQTT_TOPIC;
	
	//Boolean value to indicate whether using HTTPs (Ubidots API) for publishing sensor data
	boolean useUbidotsAPI = false;
	
	//Get the logger for the class
		private final static Logger LOGGER = Logger.getLogger(UbidotsClientConnector.class.getName());
	/*
	 * Constructor used to instantiate the classes used for this class and also set the variables for Mqtt connection with TLS support
	 */
	public UbidotsClientConnector() throws MqttException {
		super();
		
		CertManagementUtil certManagementUtil = CertManagementUtil.getInstance();
		//Instantiate ConfigUtil to take care of API credentials
		cUtil = new ConfigUtil();
		
		//Try to connect to ubidots using the variables from configuration file
		try {
		
			//Instantiate the API client using the user specific API key
			api = new ApiClient(this.cUtil.getValue("ubidots.cloud", "apiKey"));
		
			//Instantiate MqttClientConnector for publishing temperature sensor readings and register a listener for actuator value changes
			mqttClientConnector = new MqttClientConnector("ssl://" + this.cUtil.getValue("ubidots.cloud", "host") + ":" + "8883");
			
			//Get the data source for the constrained device
			dataSource = api.getDataSource("iot-device");
			
			//Define the variable IDs for the TempSensor and TempActuator
			TEMP_SENSOR_LABEL 		= this.cUtil.getValue("ubidots.cloud", "tempSensorLabel"); 	//TempSensor ID
			HUMIDITY_SENSOR_ID		= this.cUtil.getValue("ubidots.cloud", "humiditySensorID"); //HumiditySensor ID
			TEMP_ACTUATOR_LABEL 	= this.cUtil.getValue("ubidots.cloud", "tempActuatorLabel"); 	//TempActuator ID
			
			//Define the MQTT topic for Ubidots  
			MQTT_TOPIC 				= this.cUtil.getValue("ubidots.cloud", "devicePath");
			
			//Set the socket factory obtained from the SSL certificate
			mqttClientConnector.conOpt.setSocketFactory(certManagementUtil.loadCertificate(this.cUtil.getValue("ubidots.cloud", "certFile")));
			
			//Set the user name token for ubidots
			mqttClientConnector.conOpt.setUserName(this.cUtil.getValue("ubidots.cloud", "userNameToken"));	
		}
		
		//If an error occurred especially on the pipeline, catch the error
		catch(Exception e) {
					
				//Print the stack trace
				e.printStackTrace();
		}
	}
	
	/*
	 * Getter for the ConfigUtil instance
	 * @return		The current ConfigUtil instance for the class
	 */
	public ConfigUtil getcUtil() {
		return cUtil;
	}

	/*
	 * Method to publish incoming SensorData to Ubidots cloud
	 * 
	 * @param sensorData		The SensorData object to be published
	 * @param qos				The Quality of Service level we want for publishing
	 * 
	 * @returns A boolean value to indicate success in publishing
	 */
	public boolean publishSensorData(SensorData sensorData, int qos) {

		//Declare the variable ID to which we are publishing Sensor Data 
		String variableParam;
		
		//If the sensor is a temperature sensor
		if(sensorData.getName().contentEquals("Temperature")) {
			
			//Set the variable ID to the temperature sensor label
			variableParam = TEMP_SENSOR_LABEL;
			
			//publish to the topic for temp sensor
			boolean success = mqttClientConnector.publishSensorValue("/v1.6/devices/iot-device/tempsensor", sensorData, 2);
			
			//If successfully published SensorData via MQTT
			if(success == true) {
				
				//Log a message
				LOGGER.info("Published SensorData using MQTT to ubidots");
				
				//return true indicating success in publishing
				return true;
			}
			
			//If failure to publish
			else {
				
				//Log a message
				LOGGER.info("Could not publish SensorData using MQTT to ubidots");
				
				//return a false indicating failure to publish SensorData
				return false;
			}
		} 
		
		//If the sensor is a humidity sensor
		else if(sensorData.getName().contentEquals("Humidity")) {
			
			//Set the variable ID to the humidity sensor ID
			variableParam = HUMIDITY_SENSOR_ID;
			
			//Try to send the humidity sensor data via the Ubidots API
			try {
				
				//get the temperature variable
				Variable tempVariable = this.api.getVariable(variableParam);
					
				//publish to temperature variable
				tempVariable.saveValue(sensorData.getCurrentValue());
			}
			
			//If an error occurs
			catch(Exception e) {
				
				//Print the error
				e.printStackTrace();
				
				//Log a message
				LOGGER.info("Could not publish SensorData using API to ubidots");
				
				//return false to indicate error
				return false;
			}
			
			//Log a message
			LOGGER.info("Published SensorData using API to ubidots");
			
			//If succeeded without error
			return true;
		}
		
		//If an arbitrary value, return false because no publishing occurs
		else {
			
			//return false
			return false;
		}	
	}
	
	/*
	 * Method to subscribe to a particular topic with a particular qos
	 * 
	 * @param topic			The topic to which the client should subscribe to
	 * @param qos			The Quality of Service level for this subscription
	 * 
	 * @returns A boolean value to indicate success in subscribing
	 */
	public boolean subscribeToTopic(String topic, ActuatorValueListener actuatorValueListener, int qos) {
			
		//Subscribe to the topic passed in the parameter with the given qos and get the success 
		boolean success = this.mqttClientConnector.subscribeToActuatorData(topic, actuatorValueListener, qos);
		
		//if subscribed successfully
		if(success == true) {
			
			//Log a message
			LOGGER.info("Subscribed to ActuatorData updates via MQTT from ubidots");
			
			//return true to indicate success in subscribing
			return true;
		} 
		
		//If there was a failure
		else {
			
			//Log a message
			LOGGER.info("Could not subscribe to ActuatorData updates via MQTT from ubidots");
			
			//return false to indicate failure in subscribing
			return false;
		}
	}	
}
