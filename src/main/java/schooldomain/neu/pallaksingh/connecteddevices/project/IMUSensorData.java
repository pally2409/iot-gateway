package schooldomain.neu.pallaksingh.connecteddevices.project;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.logging.Logger;

/**
 * An intertial measurement unit sensor is a combination of gyrocscope, accelerometer, 
 * and magnetometer. This class is custom built for holding IMU sensor data. It is capable 
 * of holding the x, y and z values of the inertial measurement unit sensors specificially
 * gyroscope and accelerometer
 * 
 * @author pallaksingh
 */
public class IMUSensorData {
	
	//Declare the parameters for IMUSensorData;
	private Hashtable<String, Double> currentValue;	//For storing current x, y and z values for the IMU Sensor
	private Hashtable<String, Double> average;		//For storing average of x, y and z attributes for the IMU sensor
	private Hashtable<String, Double> maxValue;		//For storing the max value of x, y and z attributes for the IMU sensor
	private Hashtable<String, Double> minValue;		//For storing the min value of x, y and z attributes for the IMU sensor
	private int totalCount;							//For storing the the number of values gathered for x, y and z attributes for the IMU sensor
	private String sensorName;						//For storing name of the IMU sensor
	private String timestamp;						//For storing the timestamp of the IMU Sensor
	
	//Get the logger for the class
	private final static Logger LOGGER 				= Logger.getLogger(IMUSensorData.class.getName());
	
	/**
	 * The constructor initializes the values of the parameters for IMUSensorData
	 */
	public IMUSensorData() {
		
		//Calling the superclass
		super();
		
		//Initialize Hashtable object for storing current value
		this.currentValue				= new Hashtable<String, Double>();
		
		//Initialize Hashtable object for storing average value
		this.average					= new Hashtable<String, Double>();
		
		//Initialize the averages to 0
		this.average.put("x", (double) 0);
		this.average.put("y",(double) 0);
		this.average.put("z",  (double) 0);
		
		//Initialize Hashtable object for storing max value
		this.maxValue					= new Hashtable<String, Double>();
		
		//Initialize the maximum values to a random low number
		this.maxValue.put("x", (double) -999);
		this.maxValue.put("y",(double) -999);
		this.maxValue.put("z",  (double) -999);
		
		//Initialize Hashtable object for storing min value
		this.minValue					= new Hashtable<String, Double>();
		
		//Initialize the maximum values to a random high number
		this.minValue.put("x", (double) 999);
		this.minValue.put("y",(double) 999);
		this.minValue.put("z",  (double) 999);
		
		//Initialize the totalCount to a zero
		this.totalCount					= 0;
				
		//Initialize the IMU Sensor Name to "Not Set"
		this.sensorName					= "Not Set";
		
		//Initialize the timestamp to None
		this.timestamp					= "None"; 							
	}
	
	/**
	 * Method for updating the current value of the Sensor Data
	 * 
	 * @param val
	 * 			The value to be added
	 * 
	 * @return A boolean value indicating success in adding the value 
	 */
	public boolean addValue(Hashtable<String, Double> val) {
		
		//Try to add the value to the IMUSensorData object
		try {
		
			//Specfiy the formatting of the timestamp
			DateTimeFormatter dtf 			= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");				
			
			//Update the current value of IMUSensorData
			this.currentValue				= val;
			
			//Update the average value for each attribute
			this.average.put("x", (((this.totalCount * this.average.get("x")) + this.currentValue.get("x"))/(this.totalCount + 1)));
			this.average.put("y", ((((this.totalCount * this.average.get("y")) + this.currentValue.get("y"))/(this.totalCount + 1))));
			this.average.put("z", (((this.totalCount * this.average.get("z")) + this.currentValue.get("z"))/(this.totalCount + 1)));  
	        
	        //Update the timestamp
	        this.timestamp 					= dtf.format(LocalDateTime.now());
	        
	        //Update the maximum value for each attribute
	        //Check if the new value for x is less than the current maximum value of x
	        if(this.maxValue.get("x") 	< this.currentValue.get("x")) {
	            
	        	//Update the value
	        	this.maxValue.put("x", this.currentValue.get("x"));     		
	        }
	        
	        //Check if the new value for y is less than the current maximum value of y
	        if(this.maxValue.get("y") 	< this.currentValue.get("y")) {
	            
	        	//Update the value
	        	this.maxValue.put("y", this.currentValue.get("y"));       		
	        }
	        
	        //Check if the new value for z is less than the current maximum value of z
	        if(this.maxValue.get("z") 	< this.currentValue.get("z")) {
	            
	        	//Update the value
	        	this.maxValue.put("z", this.currentValue.get("z"));     		
	        }
	       
	        //Update the minimum value for each attribute
	        //Check if the new value for x is more than the current minimum value of x
	        if(this.minValue.get("x") 	> this.currentValue.get("x")) {
	            
	        	//Update the value
	        	this.minValue.put("x", this.currentValue.get("x"));        		
	        }
	        
	        //Check if the new value for y is more than the current minimum value of y
	        if(this.minValue.get("y") 	> this.currentValue.get("y")) {
	            
	        	//Update the value
	        	this.minValue.put("y", this.currentValue.get("y"));    		
	        }
	        
	        //Check if the new value for z is more than the current minimum value of z
	        if(this.minValue.get("z") 	> this.currentValue.get("z")) {
	            
	        	//Update the value
	        	this.minValue.put("z", this.currentValue.get("z"));    		
	        }
		} 
		
		//If an error occurred because the data format was incorrect
		catch(Exception e) {
			
			//Log the error
			LOGGER.info("Could not add the value as the data format was incorrect");
			
			//Print the error stacktrace
			e.printStackTrace();
			
			//return false to indicate failure in adding the value
			return false;	
		}
		
		//Increment the count
        this.totalCount                 += 1;   
		
		//If added successfully, return true to indicate success in adding value
		return true;
	}
	
	/** 
	 * Method that returns the average value of the IMU Sensor
	 * 
	 * @return the hashtable containing the average of x, y and z values
	 */
	public Hashtable<String, Double> getAverageValue() {
		
		//Return the average value of the IMU sensor
		return this.average;
	}
	
	/** 
	 * Method that returns the current value of the IMU Sensor
	 * 
	 * @return the hashtable containing the current x, y and z values
	 */
	public Hashtable<String, Double> getCurrentValue() {
		
		//Return the current value of the IMU sensor
		return this.currentValue;
	}
	
	/** 
	 * Method that returns the maximum value of the IMU Sensor attributes
	 * 
	 * @return the hashtable containing the maximum of x, y and z values
	 */
	public Hashtable<String, Double> getMaxValue() {
		
		//Return the maximum value of the IMU sensor attributes
		return this.maxValue;
	}
	
	/** 
	 * Method that returns the minimum value of the IMU Sensor attributes
	 * 
	 * @return the hashtable containing the minimum of x, y and z values
	 */
	public Hashtable<String, Double> getMinValue() {
		
		//Return the minimum value of the IMU sensor attributes
		return this.minValue;
	}
	
	/** 
	 * Method that returns the current value of the IMU Sensor
	 * 
	 * @return the int containing number of readings that have been taken for the IMU sensor till now
	 */
	public int getCount() {
		
		//Return the total count that stores the number of readings that have been taken till now
		return this.totalCount;
	}
	
	/**
	 * Method that returns the name of the sensor
	 * 
	 * @return the string containing the name of the sensor
	 */
	public String getName() {
		
		//Return the name of the sensor
		return this.sensorName;
	}
	
	/**
	 * Method that sets the name of the sensor
	 * 
	 * @param sensorName
	 * 			The name of the sensor to be set for the IMU sensor
	 */
	public void setName(String sensorName) {
		
		//Check if the value is not null
		if(sensorName!=null) {
			
			//Set the name of the sensor
			this.sensorName		= sensorName;
		} 
		
		//If null, set it to 'Not Set'
		else {
			
			//Set the name of the sensor to 'Not Set'
			this.sensorName		= "Not Set";
		}
	}
}
