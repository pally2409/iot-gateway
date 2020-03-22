//Import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module08;
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

/*
 * MqttClientConnector class provides abstraction for all MQTT related tasks such as
 * subscribing, publishing various SensorData and ActuatorData. 
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
	private final static Logger LOGGER = Logger.getLogger(MqttClientConnector.class.getName());
	
	//Set the options to describe the client should connect to the server
	MqttConnectOptions conOpt = new MqttConnectOptions();
	
	//disabled TLS by default
	boolean tlsEnabled = false;			//variable to indicate whether TLS is enabled
	
	/*
	 * Constructor that instantiates classes needed for this particular class and seys 
	 */
	public MqttClientConnector(String hostSensor) {
		super();
		
		//Instantiate DataUtil used for converting incoming data to JSON for publishing to MQTT broker
		dUtil = new DataUtil();
		
		//Instantiate CertManagementUtil for loading the SSL certificate 
		certManagementUtil = CertManagementUtil.getInstance();
		
		//Initialize client
		try {
			this.client = new MqttClient(hostSensor, MqttClient.generateClientId());
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.client = null;
		}
		
		//Clean the session 
		this.conOpt.setCleanSession(true);
	}
	
	/*
	 * This method subscribes to the given SensorData topic with the given Quality of Service level
	 * Returns boolean value to indicate success of subscription
	 * 
	 * @param topic					The topic to subscribe to
	 * @param SensorDataListener	The callback when there is a new message or if there is a change in the connection
	 * @return		A boolean value indicating the success of subscribing
	 */ 
	public boolean subscribeToSensorData(String topic, SensorDataListener sensorDataListener, int qos) {
		
		//Try subscribe to the topic
		try {
			//Log a message
			LOGGER.info("Subscribing to SensorData topic: " + topic);
			
			//Set the callback for the connection to be triggered if there is a change in the connection or there is a new message
			this.client.setCallback(sensorDataListener);
			
			//Connect to the broker
			this.client.connect(this.conOpt);
			
			//Subscribe to the given topic with given QoS 
			this.client.subscribe(topic, qos);
			
		//If error occurred
		} catch (MqttException e) { 
			
			//Print error trace
			e.printStackTrace();
			
			//Log a message
			LOGGER.info("Could not subscribe to the SensorData topic " + topic);
			
			//Return False for failure to subscribe
			return false;
		}
		
		//Log a message
		LOGGER.info("Successfully subscribed to SensorData topic " + topic);
		
		//If successfully subscribed, return true
		return true;
	}
	
	/*
	 * This method subscribes to the given SensorData topic with the given Quality of Service level
	 * Returns boolean value to indicate success of subscription
	 * 
	 * @param topic					The topic to subscribe to
	 * @param SensorDataListener	The callback when there is a new message or if there is a change in the connection
	 * @return		A boolean value indicating the success of subscribing
	 */
	public boolean subscribeToActuatorData(String topic, ActuatorValueListener actuatorValueListener, int qos) {
		
		//Try subscribe to the topic
		try {
			
			//Log a message
			LOGGER.info("Subscribing to ActuatorData topic: " + topic);
			
			//Set the callback for the connection to be triggered if there is a change in the connection or there is a new message
			this.client.setCallback(actuatorValueListener);
			
			//Connect to the broker
			this.client.connect(this.conOpt);
			
			//Subscribe to the topic
			this.client.subscribe(topic, qos);

		//If error occurred
		} catch (MqttException e) { 
					
			//Print error trace
			e.printStackTrace();
			
			//Log a message
			LOGGER.info("Could not subscribe to the ActuatorData topic " + topic);
					
			//Return False for failure to subscribe
			return false;
		}
		
		//Log a message
		LOGGER.info("Successfully subscribed to ActuatorData topic " + topic);
		
		//If successfully subscribed, return true
		return true;	
	}
	
	/*
	 * This method takes the ActuatorData as the parameter and converts it to 
	 * JSON string and publishes it to the given ActuatorData topic with the given Quality of Service level.
	 * 
	 * @param topic					The topic to publish to
	 * @param ActuatorData  		The actuatorData object to be published
	 * @return		A boolean value indicating the success of publishing
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
		} catch (MqttException e) { 
						
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
	
	/*
	 * This method takes the SensorData as the parameter and converts it to 
	 * JSON string and publishes it to the given SensorData topic with the given Quality of Service level.
	 * 
	 * @param topic					The topic to publish to
	 * @param SensorData  			The SensorData object to be published
	 * @return		A boolean value indicating the success of publishing
	 */
	public boolean publishSensorData(String topic, SensorData sensorData, int qos) {
			
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
			
			//log a message
			LOGGER.info("Publishing SensorData to topic " + topic);
			
			//Connect to the broker
			this.client.connect(this.conOpt);
			
			//Publish to the given topic
			this.client.publish(topic , mqttMessage);
			
			//Disconnect from the broker
			this.client.disconnect();
						
		//if error occurred
		} catch (MqttException e) { 
						
			//Print error trace
			e.printStackTrace();
			
			//Log a message
			LOGGER.info("Could not publish SensorData to " + topic);
						
			//Return False for failure to publish
			return false;
		}
		
		//Log a message
		LOGGER.info("Successfully published SensorData to " + topic);
					
		//If successfully published, return true
		return true;
	}
	
	/*
	 * This method takes the SensorData as the parameter and publishes the current value 
	 * to the given SensorData topic with the given Quality of Service level.
	 * 
	 * @param topic					The topic to publish to
	 * @param SensorData  			The SensorData object to be published
	 * @return		A boolean value indicating the success of publishing
	 */
	public boolean publishSensorValue(String topic, SensorData sensorData, int qos) {
		
		//Convert SensorData to JSON string
		String jSON = String.valueOf(sensorData.getCurrentValue());
		
		//set the parameters of MqttMessage object to be published
		MqttMessage mqttMessage = new MqttMessage();
		
		//Set the payload to JSON string of Actuator Data
		mqttMessage.setPayload(jSON.getBytes());
		
		//Try to publish SensorData topic
		try {
			
			//log a message
			LOGGER.info("Publishing SensorData current value to " + topic);
			
			//Connect to the broker
			this.client.connect(this.conOpt);
			
			//Publish to the given topic
			this.client.publish(topic , mqttMessage);
			
			//Disconnect from the broker
			this.client.disconnect();
						
		//if error occurred
		} catch (Exception  e) { 
						
			//Print error trace
			e.printStackTrace();
			
			//Log a message
			LOGGER.info("Could not publish SensorData current value to " + topic);
						
			//Return False for failure to subscribe
			return false;
		}
			
		//Log a message
		LOGGER.info("Successfully published SensorData current value to " + topic);
		
		//If successfully subscribed, return true
		return true;		
	}
}