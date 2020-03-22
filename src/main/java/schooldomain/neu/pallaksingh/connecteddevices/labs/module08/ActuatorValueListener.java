//Import packages and libraries
package schooldomain.neu.pallaksingh.connecteddevices.labs.module08;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;

/*
 * Class that implements MqttCallback, thereby serving as the callback function for 
 * the subscription to ActuatorData values from Ubidots. Whenever there is a change in 
 * Actuator Data on Ubidots, this class converts the incoming Actuator Data JSON to ActuatorData 
 * object and passes it to the constrained device 
 */
public class ActuatorValueListener implements MqttCallback {

	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(ActuatorValueListener.class.getName());
	
	//Declare DataUtil for all conversion to and from JSON
	DataUtil dUtil;
	
	//Declare MqttClientConnector for sending ActuatorData back to the constrained device
	MqttClientConnector mqtt;
	
	/*
	 * The constructor instantiates the classes needed for this class
	 * It also sets the MqttClientConnector to the reference passed to it
	 */
	public ActuatorValueListener() throws MqttException {
		super();
		// TODO Auto-generated constructor stub
		
		//Instantiate DataUtil for all conversion to and from JSON
		dUtil = new DataUtil();
		
		//Set the MqttClientConnector reference for sending ActuatorData back to the constrained device
		this.mqtt = new MqttClientConnector("tcp://broker.mqttdashboard.com:1883");
	}

	/*
	 * Callback function when connection to the broker is lost
	 * 
	 * @param cause			The cause for the connection loss
	 */
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
		//log the cause for losing the connection
		LOGGER.info("Connection lost because " + cause);	
	}
	
	/*
	 * Callback function for when a new message arrives
	 * 
	 * @param topic			The topic from which the message arrived
	 * @param MqttMessage	The body of the message
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
		//log the String
      	//create a pretty printed JSON (properly indented)
        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
          	
        //get a JsonParser
        JsonParser jp = new JsonParser();
          	
        //get the parsed jsonString
        JsonElement je = jp.parse(new String(message.getPayload()));
          	
        //get the pretty printed JSON string
        String jsonStrPretty = gsonBuilder.toJson(je);
          	
        //Log the incoming data
        LOGGER.info("\n------------------------------------------------------------------------" + "\n Received new ActuatorData " + jsonStrPretty + "\n------------------------------------------------------------------------");
		
		//Create a JSON object from the JSON string
    	JSONObject jsonObject = new JSONObject(message.toString());
    
    	//Retrieve the value from the JSON object
    	int value = jsonObject.getInt("value");
		
		//Create an ActuatorData object from the particular value
		ActuatorData actuatorData = new ActuatorData();
		
		//Set the name
		actuatorData.setName("TempActuator");
		
		//Set command
		actuatorData.setCommand("UPDATE");
		
		//Set the value for the actuator
		actuatorData.setValue(value);
		
		//Publish the ActuatorData to the constrained device using Mqtt
		mqtt.publishActuatorData("Connected-Devices/Actuator_Data", actuatorData, 1);
		
		//Log the message
		LOGGER.info("Sent Actuator Data to the Constrained Device");
	}

	/*
	 * Callback function for the delivery is completed
	 * 
	 * @param token			The delivery token when the delivery of the message is complete		
	 */
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
		//Log the message that delivery is complete
		LOGGER.info("Delivery Complete");	
	}
}
