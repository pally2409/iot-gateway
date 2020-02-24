// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.common;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.google.gson.*;

import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemPerformanceAdaptor;

public class DataUtil {
	
	//instantiate gson
	Gson gson = new Gson();
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(DataUtil.class.getName());
	
	//create a FileHandler
	private static FileHandler fh; 
	
	//initialize log file names for Sensor and Actuator Data
	String sensorDataLogFile = "logfiles/SensorDataLogFile.log";
	String actuatorDataLogFile = "logfiles/ActuatorDataLogFile.log";
	

	//method to convert SensorData to JSON string
    public String toJsonFromSensorData(SensorData sensorData){
    	
    	//create json string from sensorData
    	String jsonStr = this.gson.toJson(sensorData);
    	
    	//return the string
    	return jsonStr;
    }
        
    
    //method to convert JSON string to SensorData 
    public SensorData toSensorDataFromJson(String jsonStr){
    	
    	//create SensorData object using the JSON string
    	SensorData sensorData = this.gson.fromJson(jsonStr, SensorData.class);
    	
    	//return the SensorData
    	return sensorData;
    	
    }
        
  
    //method to convert SensorData to JSON and write to filesystem
    public boolean writeSensorDataToFile(SensorData sensorData) throws IOException{
    	
    	//create json string from sensorData
    	String jsonStr = this.gson.toJson(sensorData);
    	
    	//create a pretty printed JSON
    	Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
    	
    	//get a JsonParser
    	JsonParser jp = new JsonParser();
    	
    	//get the parsed jsonString
    	JsonElement je = jp.parse(jsonStr);
    	
    	//get the pretty printed JSON string
    	String jsonStrPretty = gsonBuilder.toJson(je);
    	
    	
    	//try writing to file
    	try {
    		
    		//add the file handler to the logfiles folder
    		fh = new FileHandler(this.sensorDataLogFile);
    		
    		//set formatting to SimpleFormatter
    		SimpleFormatter sFormatter = new SimpleFormatter();
    		fh.setFormatter(sFormatter);
    		
    		//add the handler to the logger
    		LOGGER.addHandler(fh);
    		
    		//log the JSON string
    		LOGGER.info(jsonStrPretty);
    				
    	
    		//if error occured
    	} catch(SecurityException e) {
    		
    		//print stackTrace and return false
    		e.printStackTrace();
    		return false;
    	} 
    	
    	//print stackTrace and return false
    	catch (IOException e) {  
            e.printStackTrace();
            return false;
        }  
    	
    	//if successful return true
    	return true;
	
    }
        
    
    //method to convert ActuatorData to JSON string
    public String toJsonFromActuatorData(ActuatorData actuatorData){
    	
    	//create json string from sensorData
    	String jsonStr = this.gson.toJson(actuatorData);
    	
    	//return the string
    	return jsonStr;
    }
        
    
    //method to convert JSON string to ActuatorData 
    public ActuatorData toActuatorDataFromJson(String jsonStr) {
    	
    	//create SensorData object using the JSON string
    	ActuatorData actuatorData = this.gson.fromJson(jsonStr, ActuatorData.class);
    	
    	//return the SensorData
    	return actuatorData;
    }
        
    
    //method to convert ActuatorData to JSON and write to filesystem
    public boolean writeActuatorDataToFile(ActuatorData actuatorData) {
    	
    	//create json string from sensorData
    	String jsonStr = this.gson.toJson(actuatorData);
    	
    	//create a pretty printed JSON
    	Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
    	
    	//get a JsonParser
    	JsonParser jp = new JsonParser();
    	
    	//get the parsed jsonString
    	JsonElement je = jp.parse(jsonStr);
    	
    	//get the pretty printed JSON string
    	String jsonStrPretty = gsonBuilder.toJson(je);
    	
    	
    	//try writing to file
    	try {
    		
    		//add the file handler to the logfiles folder
    		fh = new FileHandler(this.actuatorDataLogFile);
    		
    		//set formatting to SimpleFormatter
    		SimpleFormatter sFormatter = new SimpleFormatter();
    		fh.setFormatter(sFormatter);
    		
    		//add the handler to the logger
    		LOGGER.addHandler(fh);
    		
    		//log the JSON string
    		LOGGER.info(jsonStrPretty);
    				
    	
    		//if error occured
    	} catch(SecurityException e) {
    		
    		//print stackTrace and return false
    		e.printStackTrace();
    		return false;
    	} 
    	
    	//print stackTrace and return false
    	catch (IOException e) {  
            e.printStackTrace();
            return false;
        }  
    	
    	//if successful return true
    	return true;
    }        

}
