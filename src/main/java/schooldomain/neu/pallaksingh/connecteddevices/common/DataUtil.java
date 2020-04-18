// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.common;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemPerformanceAdaptor;
import schooldomain.neu.pallaksingh.connecteddevices.project.IMUSensorData;

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
    
    /*
     * Method to parse the JSON string containing variable values from ubidots
     * 
     * @param 	jsonStr				The JSON string received from ubidots
     * @returns	The String value from the JSON string
     */
    public String fromUbidotsJsonToString(String jsonStr) {
    	
    	//Create a JSON object from the JSON string
    	JSONObject jsonObject = new JSONObject(jsonStr);
    
    	//Retrieve the value from the JSON object
    	String value = jsonObject.getString("value");
    	
    	//return the value
    	return value;
    }
    
    /**
     * Method to parse the JSON string containing the IMUSensorData
     * 
     * @param 	jsonStr
     * 				The JSON string to be converted to IMUSensorData object
     * 
     * @return  the IMUSensorData object formed by parsing the JSON string
     */
    public IMUSensorData toIMUSensorDataFromJson(String jsonStr) {
    	
    	//Convert the JSON string to IMUSensorData object
    	IMUSensorData imuSensorData		= this.gson.fromJson(jsonStr, IMUSensorData.class);
    	
    	//Return the IMUSensorData object
    	return imuSensorData;
    }
    
    /**
     * Method to parse the JSON string containing the IMUSensorData
     * 
     * @param imuSensorData
     * 			The IMUSensorData object to be converted to Json String
     * 
     * @return the JSON string formed by parsing the IMUSensorData object
     */
    public String toJsonFromImuSensorData(IMUSensorData imuSensorData) {
    	
    	//Convert the IMUSensorData object to JSON string
    	String jsonStr		= this.gson.toJson(imuSensorData);
    	
    	//Return the JSON string
    	return jsonStr;
    }
    
    /**
     * Method to parse an ArrayList containing IMUSensorData objects and
     * convert to JSON string
     * 
     * @param imuSensorDataList
     * 				ArrayList containing IMUSensorData objects to be converted to Json String
     * 
     * @return the JSON string formed from the list containing IMUSensorData objects
     */
    public String toJsonFromImuSensorDataList(ArrayList<IMUSensorData> imuSensorDataList) {
    	
    	IMUSensorData gyroscope = new IMUSensorData();
    	IMUSensorData accelerometer = new IMUSensorData();
    	
    	for(IMUSensorData imuSensor : imuSensorDataList) {
    		
    		if(imuSensor.getName().contentEquals("Gyroscope")) {
    			
    			gyroscope =	imuSensor;
    		}
    		
    		if(imuSensor.getName().contentEquals("Accelerometer")) {
    			
    			accelerometer = imuSensor;
    		}
    	}
    	
    	Hashtable<String, IMUSensorData> imuSensorDataDict = new Hashtable<String, IMUSensorData>();
    	imuSensorDataDict.put("Gyroscope", gyroscope);
    	imuSensorDataDict.put("Accelerometer", accelerometer);
    	
    	//Convert the IMUSensorData object list to JSON string
    	String jsonStr		= this.gson.toJson(imuSensorDataDict);
    	
    	//Return the jsonString
    	return jsonStr;
    	
    }
    
    /**
     * Method to parse an ArrayList containing IMUSensorData objects and
     * convert to JSON string
     * 
     * @param jsonStr
     * 			The JSON string containing IMUSensorData objects in string form
     * 
     * @return The ArrayList containing IMUSensorData objects 
     */
    public ArrayList<IMUSensorData> toImuSensorDataListFromJson(String jsonStr) {
    	
    	JSONObject jsonObject = new JSONObject(jsonStr);
    	JSONObject jsonObjectGyroscope = jsonObject.getJSONObject("Gyroscope");
    	JSONObject jsonObjectAccelerometer = jsonObject.getJSONObject("Accelerometer");
    	
    	IMUSensorData gyroscopeData = gson.fromJson(jsonObjectGyroscope.toString(), IMUSensorData.class);
    	IMUSensorData accelerometerData = gson.fromJson(jsonObjectAccelerometer.toString(), IMUSensorData.class);
    	ArrayList<IMUSensorData> imuSensorDataList = new ArrayList<IMUSensorData>();
    	
    	imuSensorDataList.add(gyroscopeData);
    	imuSensorDataList.add(accelerometerData);
    	return imuSensorDataList;
    }
    
    /**
     * Method to parse an ArrayList containing SensorData objects and
     * convert to JSON string
     * 
     * @param sensorDataList
     * 				ArrayList containing SensorData objects to be converted to Json String
     * 
     * @return the JSON string formed from the list containing SensorData objects
     */
    public String toJsonFromSensorDataList(ArrayList<SensorData> sensorDataList) {
    	
    	SensorData memoryUtil = new SensorData();
    	SensorData cpuUtil = new SensorData();
    	
    	for(SensorData sensor : sensorDataList) {
    		
    		if(sensor.getName().contentEquals("Memory")) {
    			
    			memoryUtil =	memoryUtil;
    		}
    		
    		if(sensor.getName().contentEquals("CPU")) {
    			
    			cpuUtil = cpuUtil;
    		}
    	}
    	
    	Hashtable<String, SensorData> sensorDataDict = new Hashtable<String, SensorData>();
    	sensorDataDict.put("Memory", memoryUtil);
    	sensorDataDict.put("CPU", cpuUtil);
    	
    	//Convert the IMUSensorData object list to JSON string
    	String jsonStr		= this.gson.toJson(sensorDataDict);
    	
    	//Return the jsonString
    	return jsonStr;
    	
    }
    
    /**
     * Method to parse an ArrayList containing IMUSensorData objects and
     * convert to JSON string
     * 
     * @param jsonStr
     * 			The JSON string containing IMUSensorData objects in string form
     * 
     * @return The ArrayList containing IMUSensorData objects 
     */
    public ArrayList<SensorData> toSensorDataListFromJson(String jsonStr) {
    	
    	JSONObject jsonObject = new JSONObject(jsonStr);
    	JSONObject jsonObjectMem = jsonObject.getJSONObject("Memory");
    	JSONObject jsonObjectCpu = jsonObject.getJSONObject("CPU");
    	
    	SensorData memData = gson.fromJson(jsonObjectMem.toString(), SensorData.class);
    	SensorData cpuData = gson.fromJson(jsonObjectCpu.toString(), SensorData.class);
    	ArrayList<SensorData> sensorDataList = new ArrayList<SensorData>();
    	
    	sensorDataList.add(memData);
    	sensorDataList.add(cpuData);
    	return sensorDataList;
    }
    
    
}
