//Import packages and libraries
package schooldomain.neu.pallaksingh.connecteddevices.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/**
 * The IMUSensorDataResourceHandle extends CoapResource to handle POST requests concerning a 
 * new IMU sensor data from the constrained device
 * 
 * @author pallaksingh
*/
public class IMUSensorDataResourceHandler extends CoapResource {
	
	//Initialize DataUtil for all conversions to and from object and JSONString
	private DataUtil dUtil;
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(IMUSensorDataResourceHandler.class.getName());
	
	//Declare a list for holding the IMUSensorData post conversion from JSON string
	ArrayList<IMUSensorData> imuSensorDataList;

	//Declare UbidotsClientConnector for sending the actuator response to Ubidots Cloud
	UbidotsClientConnector ubidotsClientConnector;
	
	//Initialize IMUDataHandler for handling incoming IMU data and whether it indicates a fall
	static IMUDataHandler imuDataHandler 				= new IMUDataHandler();
	
	//Declare MqttClientConnector for communicating to constrained device that a fall was detected
	MqttClientConnector mqttClientConnector;
	
	//Initialize a counter indicating number of IMUreadings received till now
	static int counter 									= 0;
	
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
	 * 			UbidotsClientConnector reference for sending the IMU data to Ubidots Cloud
	 * @param mqttClientConnector
	 * 			mqttClientConnector reference for sending actuator data to constrained device if a fall is detected
	 */
	public IMUSensorDataResourceHandler(String name, DataUtil dUtil, UbidotsClientConnector ubidotsClientConnector, MqttClientConnector mqttClientConnector, SmtpClientConnector smtpClientConnector) {
		
		//Set the name for the resource by passing it to the super class constructor
		super(name);
	
		//Initialize DataUtil for all conversions to and from object and JSONString
		this.dUtil 						= dUtil;
			
		//Initialize UbidotsClientConnector for sending the IMU data to Ubidots Cloud
		this.ubidotsClientConnector 	= ubidotsClientConnector;
		
		//Initialize MqttClientConnector for sending actuator data to constrained device if a fall is detected
		this.mqttClientConnector 		= mqttClientConnector; 
		
		//Initialize SmtpClientConnector for sending a mail in case of a fall is detected
		this.smtpClientConnector		= smtpClientConnector;
		
		//Initialize counter holding number of readings till now to 0
		counter							= 0;
	}
	
	/**
	 * This method is a method override from the super class and is used to handle POST requests. It 
	 * sends a valid response code to the sender indicating he received the request
	 * It also creates a IMUSensorData object list from the received POST request, logs it and publishes 
	 * it to Ubidots. It checks whether the current accelerometer data indicates a fall and triggers an actuation
	 * 
	 * @param: ce 
	 * 			Represents exchange of CoAP request and response
	 */
	@Override
	public void handlePOST(CoapExchange ce) {
		
		//Increment the counter holding the readings
		counter++;

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
        LOGGER.info("\n------------------------------------------------------------------------" + "\n Received POST request for IMUSensorData " + jsonStrPretty);

        //Log the message
    	LOGGER.info("Converting to IMUSensorData");
    	
    	//Convert to IMUSensorData object list
        this.convertIncomingDataToIMUSensorData(ce.getRequestText());
    	
        //Check if the accelerometer data indicates a fall
    	if(imuDataHandler.checkFall(imuSensorDataList.get(1).getCurrentValue().get("x"), imuSensorDataList.get(1).getCurrentValue().get("y"), imuSensorDataList.get(1).getCurrentValue().get("z"))) {
    		
    		//Log that a fall was detected
    		LOGGER.info("FALL DETECTED");
    		
        	//Log the message for actuation
    		LOGGER.info("Triggering a warning message to be displayed on actuator since a fall was detected");
    		
    		//Instantiate a new ActuatorData object to be sent to the actuator (constrained device)
        	ActuatorData actuatorData = new ActuatorData();
        	
        	//Set the parameters
        	actuatorData.setCommand("DISPLAY WARNING");				//The command
        	actuatorData.setName("WARNING MESSAGE DISPLAY");		//Actuator Name
        	actuatorData.setValue(1);								//Value to be displayed
        	
        	//Publish the actuator data to the standard topic for actuator data to which the constrained device has subscribed to
        	mqttClientConnector.publishActuatorData("Connected-Devices/Actuator_Data", actuatorData, 2);
    	}
    	
    	//Check if the counter is 10, (we publish every 10 values to Ubidots
    	if(counter == 10) {
        
    		//Publish the gyroscope data to ubidots
	        ubidotsClientConnector.publishIMUSensorData(imuSensorDataList.get(0), 2);
	        
	        //Publish the accelerometer data to ubidots
	        ubidotsClientConnector.publishIMUSensorData(imuSensorDataList.get(1), 2);
	        
	        //Set the counter to zero again to reset the count
	        counter = 0;
    	}  	
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
	public boolean convertIncomingDataToIMUSensorData(String sensorDataString) {
		
		//Try to convert the incoming string to SensorData
		try {
			
			//Call DataUtil method used for converting string to IMUSensorData list
			this.imuSensorDataList = this.dUtil.toImuSensorDataListFromJson(sensorDataString);
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
