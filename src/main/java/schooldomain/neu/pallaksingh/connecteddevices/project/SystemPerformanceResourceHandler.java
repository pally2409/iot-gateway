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
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/**
 * The SystemPerformanceResourceHandler extends CoapResource to handle the 4 POST request message received from the client
 * 
 * @author pallaksingh
*/
public class SystemPerformanceResourceHandler extends CoapResource {
	
	//Initialize DataUtil for all conversions to and from object and JSONString
	private DataUtil dUtil;
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(SystemPerformanceResourceHandler.class.getName());
	
	//Initialize the sensorData
	ArrayList<SensorData> sensorDataList;
	
	//Initialize UbidotsClientConnector for sending system performance data to ubidots
	UbidotsClientConnector ubidotsClientConnector;
	
	/**
	 * This constructor is used to set the resource name by passing the parameter to the super class constructor
	 * 
	 * @param name 
	 * 			The resource name passed during instantiation
	 */
	public SystemPerformanceResourceHandler(String name, DataUtil dUtil, UbidotsClientConnector ubidotsClientConnector, SmtpClientConnector smtpClientConnector) {
		
		//Set the name for the resource by passing it to the super class constructor
		super(name);
	
		//Initialize DataUtil
		this.dUtil = dUtil;
			
		//Initialize UbidotsClientConnector
		this.ubidotsClientConnector = ubidotsClientConnector;
	}
	
	/**
	 * This method is an overriden method from the super class and is used to handle
	 * PUT requests. It sends a valid response code to the sender indicating he received the request
	 * It also creates a SensorData object from the received PUT request (if applicable) and logs it
	 * 
	 * @param: ce
	 * 			Represents exchange between CoAP request and response
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
        
    	//Send the data to Ubidots
        this.ubidotsClientConnector.publishSensorData(this.sensorDataList.get(0), 2);
        this.ubidotsClientConnector.publishSensorData(this.sensorDataList.get(1), 2);
    }
	
	/**
	 * Method to convert the incoming PUT request message to a sensorDataString
	 * 
	 * @returns A boolean indicating the success of conversion
	 */
	public boolean convertIncomingDataToSensorData(String sensorDataString) {
		
		//Try to convert the incoming string to SensorData
		try {
			
			//Call DataUtil method used for converting string to SensorData
			this.sensorDataList = this.dUtil.toSensorDataListFromJson(sensorDataString);
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
