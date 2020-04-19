//Import packages and libraries
package schooldomain.neu.pallaksingh.connecteddevices.project;
import java.util.logging.Logger;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/**
 * The ActuatorResponseResourceHandler extends CoapResource to handle POST requests concerning a response from the 
 * actuator from the CoapClient 
 * 
 * @author pallaksingh
*/
public class ActuatorResponseResourceHandler extends CoapResource {
	
	//Initialize DataUtil for all conversions to and from object and JSONString
	private DataUtil dUtil;
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(ActuatorResponseResourceHandler.class.getName());
	
	//Initialize the sensorData for holding the SensorData post conversion from JSON string
	SensorData sensorData;
	
	//Initialize UbidotsClientConnector for sending the actuator response to Ubidots Cloud
	UbidotsClientConnector ubidotsClientConnector;
	
	//Declare MqttClientConnector for communicating to constrained device that a fall was detected
	MqttClientConnector mqttClientConnector;
	
	//Declare SmtpClientConnector for sending a mail in case of a fall is detected
	SmtpClientConnector smtpClientConnector;
	
	/**
	 * This constructor is used to set the resource name by passing the parameter to the super class constructor,
	 * initialize the classes needed for the operation of this class
	 * 
	 * @param name 
	 * 			The resource name passed during instantiation
	 * @param dUtil
	 * 			DataUtil reference for all conversions to and from object and JSONString
	 * @param ubidotsClientConnector
	 * 			UbidotsClientConnector reference for sending the actuator response to Ubidots Cloud
	 */
	public ActuatorResponseResourceHandler(String name, DataUtil dUtil, UbidotsClientConnector ubidotsClientConnector, MqttClientConnector mqttClientConnector, SmtpClientConnector smtpClientConnector) {
		
		//Set the name for the resource by passing it to the super class constructor
		super(name);
	
		//Initialize DataUtil
		this.dUtil = dUtil;
			
		//Initialize UbidotsClientConnector
		this.ubidotsClientConnector = ubidotsClientConnector;
		
		//Initialize MqttClientConnector for sending actuator data to constrained device if a fall is detected
		this.mqttClientConnector 		= mqttClientConnector; 
		
		//Initialize SmtpClientConnector for sending a mail in case of a fall is detected
		this.smtpClientConnector		= smtpClientConnector;	
	}
	
	/**
	 * This method is a method override from the super class and is used to handle POST requests. It 
	 * sends a valid response code to the sender indicating he received the request
	 * It also creates a SensorData object from the received POST request, logs it and publishes it to Ubidots
	 * 
	 * @param: ce 
	 * 			Represents exchange of CoAP request and response
	 */
	@Override
	public void handlePOST(CoapExchange ce) {
		
		//Send the valid response code
        ce.respond(ResponseCode.VALID, "PUT_REQUEST_SUCCESS");
        
        //log the String
      	//create a pretty printed JSON (properly indented)
        Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
          	
        //get a JsonParser
        JsonParser jp = new JsonParser();
          	
        //get the parsed jsonString
        JsonElement je = jp.parse(ce.getRequestText());
          	
        //get the pretty printed JSON string
        String jsonStrPretty = gsonBuilder.toJson(je);
          	
        //Log the incoming data
        LOGGER.info("\n------------------------------------------------------------------------" + "\n Received POST request for SensorData " + jsonStrPretty);

        //Log the message
    	LOGGER.info("Converting to SensorData");
    	
    	//Convert to SensorData object
    	this.convertIncomingDataToSensorData(ce.getRequestText());
    	
    	//Check if the sensor is the emergency button
    	if(this.sensorData.getName().contentEquals("Emergency")) {
    		
    		//Check if there was no response from the user
    		if(this.sensorData.getCurrentValue() == 1.0) {
    			
    			//Display on the user's device that we are notifying the emergency contact
    			
    			//Instantiate a new ActuatorData object to be sent to the actuator (constrained device)
            	ActuatorData actuatorData = new ActuatorData();
    			
            	//Set the parameters
            	actuatorData.setCommand("DISPLAY WARNING");				//The command
            	actuatorData.setName("CALLING DISPLAY");				//Actuator Name
            	actuatorData.setValue(1);								//Value to be displayed
            	
            	//Send via MQTT
            	this.mqttClientConnector.publishActuatorData("Connected-Devices/Actuator_Data", actuatorData, 2);
            	
            	//Send a mail using SmtpClientConnector
            	smtpClientConnector.publishMessage("EMERGENCY: Please check on Mr. Sense Hat", "Our systems have detected a fall on the wearable device that Mr. Sense Hat is wearing. "
            			+ "We have not received a response from him post the fall. Please check on him immediately. \n The Fall A Friend team");
    		}
    	}
    	//Publish the SensorData object to Ubidots 
        this.ubidotsClientConnector.publishSensorData(this.sensorData, 2);
    }
	
	/**
	 * Method to convert the incoming POST request message to a sensorDataString
	 * 
	 * @param sensorDataString
	 * 			The incoming JSON string that arrived in the POST request
	 * 	
	 * 
	 * @returns A boolean indicating the success of conversion
	 */
	public boolean convertIncomingDataToSensorData(String sensorDataString) {
		
		//Try to convert the incoming string to SensorData
		try {
			
			//Call DataUtil method used for converting string to SensorData
			this.sensorData = this.dUtil.toSensorDataFromJson(sensorDataString);
		}
		
		//If an error occurred during conversion
		catch(Exception e) {
			
			//print the error message
			e.printStackTrace();
			
			//return false indicating failure in assignment
			return false;
		}
		
		//if converted successfully, return true to indicate success
		return true;
	}	
}
