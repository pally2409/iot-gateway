//Import packages and libraries
package schooldomain.neu.pallaksingh.connecteddevices.labs.module08;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/*
 * Class that implements MqttCallback, thereby serving as the callback function for
 * the subscription to Actuator Data values from Ubidots.
 * This class converts the incoming Actuator Data JSON to ActuatorData object and
 * passes it to the constrained device
 */
public class SensorDataListener implements MqttCallback {
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(SensorDataListener.class.getName());

	//Declare DataUtil for all conversion to and from JSON
	DataUtil dUtil;
		
	//Declare UbidotsClientConnector for sending SensorData to ubidots
	UbidotsClientConnector ubidotsClientConnector;
		
	/*
	* The constructor instantiates the classes needed for this class
	* It also sets the MqttClientConnector to the reference passed to it
	*/
	public SensorDataListener(UbidotsClientConnector ubidotsClientConnector) throws MqttException {
		super();
		// TODO Auto-generated constructor stub
		
		//Instantiate DataUtil for all conversion to and from JSON
		dUtil = new DataUtil();
			
		//Set the UbidotsClientConnector reference for sending ActuatorData back to the constrained device
		this.ubidotsClientConnector = new UbidotsClientConnector();
		LOGGER.info("SENSOR DATA LISTENER");
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
		
		//Convert message to SensorData using DataUtil
		SensorData sensorData = dUtil.toSensorDataFromJson(message.toString());
		
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
        LOGGER.info("\n------------------------------------------------------------------------" + "\n Received new SensorData " + jsonStrPretty + "\n------------------------------------------------------------------------");

		//Send the data to ubidotsClientConnector to publish to ubidots
		ubidotsClientConnector.publishSensorData(sensorData, 2);			
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
