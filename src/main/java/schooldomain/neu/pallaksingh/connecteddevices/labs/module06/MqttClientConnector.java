//Import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module06;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemPerformanceAdaptor;

/*
 * MqttClientConnector class provides abstraction for all MQTT related tasks such as
   subscribing, publishing various Sensor Data. It also holds the callback methods
   for when message is received, connection is created
 */
public class MqttClientConnector implements MqttCallback {
	
	//Initialize MqttClient
	MqttClient client;
	
	//Initialize DataUtil
	DataUtil dUtil = new DataUtil();
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(MqttClientConnector.class.getName());
		
	//This method returns the reference to the MqttClient 
	public MqttClient getClient() {
		
		return this.client;
	}
	
	//This method connects to the host with the clientID provided
	public void connect(String host, String clientID) {
		
		//Try to set up the Mqtt Connection
		try {
			
			//Set the connection options
			MqttConnectOptions conOpt = new MqttConnectOptions();
			
			//Refresh the session
			conOpt.setCleanSession(true);
			
			//Initialize client
			this.client = new MqttClient(host, clientID, new MemoryPersistence());
			
			//Set the callback for the client to this class, as it holds all the callback functions
			this.client.setCallback(this);
			
			//Set the options for the client
			this.client.connect(conOpt);
			
			//If any error occured
		} catch (MqttException e) {
			
			//Print error trace
			e.printStackTrace();		
		}
	}

	/*
	 * This method subscribes to the standard SensorData topic with the given Quality of Service level
	 * Returns boolean value to indicate success of subscription
	 */
	public boolean subscribeToSensorData(int qos) {
		
		//Try subscribe to the topic
		try {
			
			//Subscribe with given QoS 
			this.client.subscribe("Connected-Devices/Sensor_Data", qos);
			
		//If error occurred
		} catch (MqttException e) { 
			
			//Print error trace
			e.printStackTrace();
			
			//Return False for failure to subscribe
			return false;
		}
		
		//If successfully subscribed, return true
		return true;
	}
	
	/*
	 * This method subscribes to the standard ActuatorData topic with the given Quality of Service level
	 * Returns boolean value to indicate success of  subscription
	 */
	public boolean subscribeToActuatorData(int qos) {
		
		//Try subscribe to the topic
		try {
					
			//Subscribe with given topic and QoS
			this.client.subscribe("Connected-Devices/Actuator_Data", qos);
					
					
		//If error occurred
		} catch (MqttException e) { 
					
			//Print error trace
			e.printStackTrace();
					
			//Return False for failure to subscribe
			return false;
		}
				
		//If successfully subscribed, return true
		return true;	
	}
	
	/*
	 * This method takes the actuatorData as the parameter and converts it to 
	 * JSON string and publishes it to the standard ActuatorData topic. Returns
	 * boolean to indicate success of publishing
	 */
	public boolean publishActuatorData(ActuatorData actuatorData, int qos) {
			
		//Convert the ActuatorData to JSON string
		String jSON = this.dUtil.toJsonFromActuatorData(actuatorData);
		
		//Set the parameters of MqttMessage object to be published
		MqttMessage mqttMessage = new MqttMessage();
		
		//Set the Quality of Service level
		mqttMessage.setQos(qos);
		
		//Set the payload to JSON string of Actuator Data
		mqttMessage.setPayload(jSON.getBytes());
		
		//Try to publish ActuatorData topic
		try {
						
			//Publish message to the standard topic
			this.client.publish("Connected-Devices/Actuator_Data", mqttMessage);
						
		//if error occurred
		} catch (MqttException e) { 
						
			//Print error trace
			e.printStackTrace();
						
			//Return False for failure to subscribe
			return false;
		}
					
		//If successfully subscribed, return true
		return true;	
	}
	
	/*
	 * This method takes the SensorData as the parameter and converts it to 
	 * JSON string and publishes it to the standard SensorData topic. Returns
	 * boolean to indicate success of publishing
	 */
	public boolean publishSensorData(SensorData sensorData, int qos) {
			
		//Convert SensorData to JSON string
		String jSON = this.dUtil.toJsonFromSensorData(sensorData);
		
		//set the parameters of MqttMessage object to be published
		MqttMessage mqttMessage = new MqttMessage();
		
		//Set the Quality of Service level
		mqttMessage.setQos(qos);
		
		//Set the payload to JSON string of Actuator Data
		mqttMessage.setPayload(jSON.getBytes());
		
		//Try to publish SensorData topic
		try {
						
			//Publish to the standard topic
			this.client.publish("Connected-Devices/Sensor_Data", mqttMessage);
						
		//if error occurred
		} catch (MqttException e) { 
						
			//Print error trace
			e.printStackTrace();
						
			//Return False for failure to subscribe
			return false;
		}
					
		//If successfully subscribed, return true
		return true;
	}		

	//Callback for lost connection. Logs the cause of lost connection
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
		//log the error
		LOGGER.info("Connection lost because " + cause);	
	}
	
	/*
	 * Callback function for when there is a message from the publisher. It logs the
	 * incoming data, converts to appropriate type (SensorData object or ActuatorData object)
	 * from looking at the source topic 
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
		//log the String
		//create a pretty printed JSON (properly indented)
    	Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
    	
    	//get a JsonParser
    	JsonParser jp = new JsonParser();
    	
    	//get the parsed jsonString
    	JsonElement je = jp.parse(message.toString());
    	
    	//get the pretty printed JSON string
    	String jsonStrPretty = gsonBuilder.toJson(je);
    	
    	//Log the incoming data
    	LOGGER.info("\n------------------------------------------------------------------------" + "\n Received Sensor Data from " + topic + "\n" + jsonStrPretty);
		
    	//If the source topic is of SensorData
		if(topic.contentEquals("Connected-Devices/Sensor_Data")) {
			
			//Convert JSON to SensorData
	    	SensorData sensorData = this.dUtil.toSensorDataFromJson(message.toString());
	    	
	    	//Log the message
	    	LOGGER.info("Converting to SensorData");
	    	
	    	//Write the SensorData to file
	    	this.dUtil.writeSensorDataToFile(sensorData);	
		}
    		
		//If the source topic is of ActuatorData
		if(topic.contentEquals("Connected-Devices/Actuator_Data")) {
			
			//Convert JSON to ActuatorData
	    	ActuatorData actuatorData = this.dUtil.toActuatorDataFromJson(message.toString());
	    	
	    	//Log the message
	    	LOGGER.info("Converting to ActuatorData");
	    	
	    	//Write the ActuatorData to file
	    	this.dUtil.writeActuatorDataToFile(actuatorData);	
		}
	}

	//Callback function for completed delivery, it logs a message for completed delivery
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
		//log the message
		LOGGER.info("Delivery Complete");
	}
}