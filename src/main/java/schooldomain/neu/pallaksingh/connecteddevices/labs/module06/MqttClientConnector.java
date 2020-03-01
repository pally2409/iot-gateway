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

public class MqttClientConnector implements MqttCallback {
	
	//initialize MqttClient
	MqttClient client;
	
	//initialize DataUtil
	DataUtil dUtil = new DataUtil();
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(MqttClientConnector.class.getName());
		
	public MqttClientConnector() {
		
		super();
		// TODO Auto-generated constructor stub
	}
	
	//this method returns the reference to the MqttClient 
	public MqttClient getClient() {
		
		return this.client;
	}
	
	//this method connects to the host with the clientID provided
	public void connect(String host, String clientID) {
		
		//initialize MqttClient 
		try {
			
			//set the connection options
			MqttConnectOptions conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(true);
			
			//initialize client
			this.client = new MqttClient(host, clientID, new MemoryPersistence());
			
			//set the callback for the client to this class, as it holds all the callback functions
			this.client.setCallback(this);
			
			//set the options for the client
			this.client.connect(conOpt);
					
		} catch (MqttException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
				
		}
	}
	
	//this method subscribes to the standard SensorData topic with the given qos
	public boolean subscribeToSensorData(int qos) {
		
		//try subscribe to the topic
		try {
			
			//subscribe with given topic and qos
			this.client.subscribe("Connected-Devices/Sensor_Data", qos);
			
			
		//if error occurred
		} catch (MqttException e) { 
			
			// TODO Auto-generated catch block
			//print stacktrace
			e.printStackTrace();
			
			//return false for error
			return false;
		}
		
		//if runs successfully, return a true
		return true;
	
	}
	
	//This method subscribes to the standard ActuatorData topic with the given qos
	public boolean subscribeToActuatorData(int qos) {
		
		//try subscribe to the topic
		try {
					
			//subscribe with given topic and qos
			this.client.subscribe("Connected-Devices/Actuator_Data", qos);
					
					
		//if error occurred
		} catch (MqttException e) { 
					
			// TODO Auto-generated catch block
			//print stacktrace
			e.printStackTrace();
					
			//return false for error
			return false;
		}
				
		//if runs successfully, return a true
		return true;
		
	}
	
	/*
	 * This method takes the actuatorData as the parameter and converts it to 
	 * JSON string and publishes it to the standard ActuatorData topic
	 */
	public boolean publishActuatorData(ActuatorData actuatorData, int qos) {
			
		//convert to JSON string
		String jSON = this.dUtil.toJsonFromActuatorData(actuatorData);
		
		//set the parameters of MqttMessage object to be published
		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setQos(qos);
		mqttMessage.setPayload(jSON.getBytes());
		
		//try to publish ActuatorData topic
		try {
						
			//publish to given topic
			this.client.publish("Connected-Devices/Actuator_Data", mqttMessage);
						
		//if error occurred
		} catch (MqttException e) { 
						
			// TODO Auto-generated catch block
			//print stacktrace
			e.printStackTrace();
						
			//return false for error
			return false;
		}
					
		//if runs successfully, return a true
		return true;	
	}
	
	/*
	 * This method takes the sensorData as the parameter and converts it to 
	 * JSON string and publishes it to the standard SensorData topic
	 */
	public boolean publishSensorData(SensorData sensorData, int qos) {
			
		//convert to JSON string
		String jSON = this.dUtil.toJsonFromSensorData(sensorData);
		
		//set the parameters of MqttMessage object to be published
		MqttMessage mqttMessage = new MqttMessage();
		mqttMessage.setQos(qos);
		mqttMessage.setPayload(jSON.getBytes());
		
		//try to publish SensorData topic
		try {
						
			//publish to given topic
			this.client.publish("Connected-Devices/Sensor_Data", mqttMessage);
						
		//if error occurred
		} catch (MqttException e) { 
						
			// TODO Auto-generated catch block
			//print stacktrace
			e.printStackTrace();
						
			//return false for error
			return false;
		}
					
		//if runs successfully, return a true
		return true;
	}		

	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
		//log the error
		LOGGER.info("Connection lost because " + cause);	
	}
	
	//callback function for when there is a message from the publisher
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
		//log the String
		//create a pretty printed JSON
    	Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
    	
    	//get a JsonParser
    	JsonParser jp = new JsonParser();
    	
    	//get the parsed jsonString
    	JsonElement je = jp.parse(message.toString());
    	
    	//get the pretty printed JSON string
    	String jsonStrPretty = gsonBuilder.toJson(je);
    	
    	LOGGER.info("\n------------------------------------------------------------------------" + "\n Received Sensor Data from " + topic + "\n" + jsonStrPretty);
		
		if(topic.contentEquals("Connected-Devices/Sensor_Data")) {
			
			//convert to SensorData
	    	SensorData sensorData = this.dUtil.toSensorDataFromJson(message.toString());
	    	
	    	LOGGER.info("Converting to SensorData");
	    	
	    	//write the SensorData to file, this method 
	    	this.dUtil.writeSensorDataToFile(sensorData);
			
		}
    		
		if(topic.contentEquals("Connected-Devices/Actuator_Data")) {
			
			//convert to ActuatorData
	    	ActuatorData actuatorData = this.dUtil.toActuatorDataFromJson(message.toString());
	    	
	    	LOGGER.info("Converting to ActuatorData");
	    	
	    	//write the ActuatorData to file, this method 
	    	this.dUtil.writeActuatorDataToFile(actuatorData);
			
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
		//log the message
		LOGGER.info("Delivery Complete");
	}

}
