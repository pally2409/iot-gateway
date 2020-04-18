//Import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.project;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocketFactory;

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
import com.labbenchstudios.iot.common.CertManagementUtil;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemPerformanceAdaptor;

/**
 * MqttClientConnector class provides abstraction for all MQTT related tasks required for this project such as subscribing 
 * to ActuatorData and publishing ActuatorData
 */
public class MqttClientConnector {
	
	//Declare MqttClient 
	MqttClient client;
	
	//Declare the SSLSocketFactory for the particular certificate loaded
	SSLSocketFactory sslSock;
	
	//Declare DataUtil used for converting incoming data to JSON for publishing to MQTT broker
	DataUtil dUtil;
	
	//Declare ConfigUtil to deal with the topics and variables for ubidots cloud
	ConfigUtil confUtil;
	
	//Declare CertManagementUtil for loading the SSL certificate 
	CertManagementUtil certManagementUtil;
	
	//Get the logger for the class
	private final static Logger LOGGER 			= Logger.getLogger(MqttClientConnector.class.getName());
	
	//Set the options to describe the client should connect to the server
	MqttConnectOptions conOpt 					= new MqttConnectOptions();
	
	//Disabled TLS by default
	boolean tlsEnabled = false;		
	
	/**
	 * Constructor that instantiates classes needed for this particular class and loads the certificates required for MQTT with TLS
	 * 
	 * @param hostSensor
	 * 			Host to connect to for MQTT connection
	 */
	public MqttClientConnector(String hostSensor, DataUtil dUtil) {
		super();
		
		//Instantiate DataUtil used for converting incoming data to JSON for publishing to MQTT broker
		this.dUtil = dUtil;
		
		//Instantiate CertManagementUtil for loading the SSL certificate 
		certManagementUtil = CertManagementUtil.getInstance();
		
		//Initialize client
		try {
			
			//Initialize a client with the host name 
			this.client = new MqttClient(hostSensor, MqttClient.generateClientId());
		
		//If an error occurs during clientID generation
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			
			//Set the clientID to null
			e.printStackTrace();
			this.client = null;
		}
		
		//Clean the session 
		this.conOpt.setCleanSession(true);
	}
	
	/**
	 * Getter method for the MqttClient
	 * 
	 * @return The current MqttClient reference		
	 */
	public MqttClient getClient() {
		
		//Return the current MqttClient reference
		return this.client;
	}
	
	/**
	 * This method takes the ActuatorData as the parameter and converts it to 
	 * JSON string and publishes it to the given ActuatorData topic with the given Quality of Service level.
	 * 
	 * @param topic					
	 * 			The topic to publish to
	 * @param ActuatorData  		
	 * 			The actuatorData object to be published
	 * @return	A boolean value indicating the success of publishing
	 */
	public boolean publishActuatorData(String topic, ActuatorData actuatorData, int qos) {
		
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
			
			//log a message
			LOGGER.info("Publishing ActuatorData to " + topic);
			
			//Connect to the broker
			this.client.connect(this.conOpt);
			
			//Publish message to the given topic
			this.client.publish(topic, mqttMessage);
			
			//Disconnect from the broker
			this.client.disconnect();
						
		//if error occurred
		} catch (Exception e) { 
						
			//Print error trace
			e.printStackTrace();
			
			//Log a message
			LOGGER.info("Could not publish ActuatorData to " + topic);
						
			//Return False for failure to subscribe
			return false;
		}
		
		//Log a message
		LOGGER.info("Successfully published ActuatorData to " + topic);
					
		//If successfully subscribed, return true
		return true;	
	}
}