//Import packages and libraries
package schooldomain.neu.pallaksingh.connecteddevices.labs.module07;
import java.io.IOException;
import java.util.logging.Logger;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/*
 * The TempResourceHandler extends CoapResource to handle the 4 type of request messages: GET, POST, PUT, DELETE
 * received from the client
*/
public class TempResourceHandler extends CoapResource {
	
	//Initialize DataUtil for all conversions to and from object and JSONString
	private DataUtil dUtil;
	
	//Get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(TempResourceHandler.class.getName());
	
	//Initialize the sensorData
	private SensorData sensorData;
	
	/*
	 * This constructor is used to set the resource name by passing the parameter to the super class constructor
	 * 
	 * @param name The resource name passed during instantiation
	 */
	public TempResourceHandler(String name) {
		
		//Set the name for the resource by passing it to the super class constructor
		super(name);
	
		//Instantiate DataUtil
		this.dUtil = new DataUtil();
	}
	
	/*
	 * This method is an overriden method from the super class and is used to handle
	 * GET requests. It sends a valid response code to the sender indicating he received the request
	 * 
	 * @param: CoapExchange Represents exchange between CoAP request and response
	 */
	@Override
	public void handleGET(CoapExchange ce) {
		
		//Send the valid response code
		ce.respond(ResponseCode.VALID, "GET WORKED");
		
		//Log a message
		LOGGER.info("Received GET request message");	
	}
	
	/*
	 * This method is an overriden method from the super class and is used to handle
	 * DELETE requests. It sends a valid response code to the sender indicating he received the request
	 * 
	 * @param: CoapExchange Represents exchange between CoAP request and response
	 */
	@Override
	public void handleDELETE(CoapExchange ce) {
		
		//Send the valid response code
		ce.respond(ResponseCode.VALID, "GET WORKED");
		
		//Log a message
		LOGGER.info("Received DELETE request message");
	}
	
	/*
	 * This method is an overriden method from the super class and is used to handle
	 * POST requests. It sends a valid response code to the sender indicating he received the request
	 * 
	 * @param: CoapExchange Represents exchange between CoAP request and response
	 */
	@Override
	public void handlePOST(CoapExchange ce) {
        
		//Send the valid response code
		ce.respond(ResponseCode.VALID, "POST_REQUEST_SUCCESS");
       
		//Log a message
		LOGGER.info("Received POST request message");
    }
	
	/*
	 * This method is an overriden method from the super class and is used to handle
	 * PUT requests. It sends a valid response code to the sender indicating he received the request
	 * It also creates a SensorData object from the received PUT request (if applicable) and logs it
	 * 
	 * @param: CoapExchange Represents exchange between CoAP request and response
	 * 
	 */
	@Override
	public void handlePUT(CoapExchange ce) {
		
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
        LOGGER.info("\n------------------------------------------------------------------------" + "\n Received PUT request for SensorData " + jsonStrPretty);

        //Log the message
    	LOGGER.info("Converting to SensorData");
    	
    	//Convert to SensorData object
        this.convertIncomingDataToSensorData(ce.getRequestText());
    	
    	//Try to  the SensorData to file
    	try {
    		
    		//Write using dataUtil
			this.dUtil.writeSensorDataToFile(sensorData);
		} 
    	
    	//If an error occured (file doesn't exist)
    	catch (IOException e) {
			
    		//print the error
			e.printStackTrace();
		}
    }
	
	/*
	 * Method to convert the incoming PUT request message to a sensorDataString
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

	//Getters and setters for sensorData
	//Get the sensorData
	public SensorData getSensorData() {
		
		//return the current reference of SensorData 
		return sensorData;
	}

	//Set the sensorData
	public void setSensorData(SensorData sensorData) {
		
		//set the current reference of SensorData to the passed SensorData
		this.sensorData = sensorData;
	}	
}
