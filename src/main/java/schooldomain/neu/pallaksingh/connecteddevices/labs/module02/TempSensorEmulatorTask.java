/*
 * import packages and libraries
 */
package schooldomain.neu.pallaksingh.connecteddevices.labs.module02;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemPerformanceAdaptor;
public class TempSensorEmulatorTask {
	
	// initialize sensor data to hold the aggregate and current temp values
	SensorData sensorData = new SensorData();
	
	//intialize smtp connector for sending notifications
	SmtpClientConnector smtpConnector = new SmtpClientConnector();
	
	//disable the emulator initially
	boolean enableEmulator = false;
	
	float minTemp = 0.0f; //lower bound for temperature emulator
	float maxTemp = 30.0f; //upper bound for temperature emulator
	int rateInSec = 10; //how often emulator should provide readings
	float threshold = 5.0f; //threshold for temperature difference from average
	int numReadings = 10; //number of readings to take 
	
	//get the logger for the class
	private static Logger LOGGER = Logger.getLogger(TempSensorEmulatorTask.class.getSimpleName());
	
	/*
	 * 	constructor takes in the lower and upper bound on temperature generator, 
    	how often it should generate temp and the threshold for difference between 
    	average and current temp
	 */
	
	public TempSensorEmulatorTask(float minTemp, float maxTemp, int rateInSec, float threshold, int numReadings)	 {
		
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.rateInSec = rateInSec;
		this.threshold = threshold;
		this.numReadings = numReadings;
		
		LOGGER.setLevel(Level.INFO);
	}
	
	/*
	 * empty constructor for keeping the default values
	 */
	public TempSensorEmulatorTask() {
		
		
	}
	
	
	//getters and setters
	
	//get lower bound of generator 
	public float getMinTemp() {
		return minTemp;
	}

	//set lower bound of generator
	public void setMinTemp(float minTemp) {
		this.minTemp = minTemp;
	}

	//get upper bound of generator
	public float getMaxTemp() {
		return maxTemp;
	}

	//set upper bound of generator
	public void setMaxTemp(float maxTemp) {
		this.maxTemp = maxTemp;
	}

	//get the frequency of readings set
	public int getRateInSec() {
		return rateInSec;
	}

	//set the frequency of readings
	public void setRateInSec(int rateInSec) {
		this.rateInSec = rateInSec;
	}

	//get the threshold for max difference between current and average sensor data
	public float getThreshold() {
		return threshold;
	}

	//set the threshold for max difference between current and average sensor data
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	//get num of readings to be generated
	public int getNumReadings() {
		return numReadings;
	}
	
	//set num of readings to be generated
	public void setNumReadings(int numReadings) {
		this.numReadings = numReadings;
	}

	//get smtp connector reference
	public SmtpClientConnector getSmtpConnector() {
		return smtpConnector;
	}

	//get if emulator is enabled
	public boolean isEnableEmulator() {
		return enableEmulator;
	}

	//set the emulator
	public void setEnableEmulator(boolean enableEmulator) {
		this.enableEmulator = enableEmulator;
	}

	//get the logger
	public static Logger getLogger() {
		return LOGGER;
	}

	//method to generate a random temperature between the lower and upper bound
	public boolean generateRandomTemperature() {
		
		//generator doesn't run if 0 readings set:
		if(this.numReadings == 0) {
			return false;
		}
		
		LOGGER.info("Starting Temp Sensor Emulator thread");
		
		// run the loop as many times as indicated in the numReadings variable
		while(this.numReadings > 0) {
			
			//if the emulator is enabled
			if(this.enableEmulator) {
				
				//use Math.random
				float temp = (float)(Math.random()*(maxTemp-minTemp)+1)+minTemp;
				
				//add the temp to sensorData 
				this.sensorData.addValue(temp);
				
				//store updated values from sensorData object
                String time = "						Time: " + this.sensorData.timestamp;
                String current = "						Current: " + String.valueOf(this.sensorData.getCurrentValue());
                String average = "						Average: " + String.valueOf(this.sensorData.getAverageValue());
                String samples = "						Samples: " + String.valueOf(this.sensorData.getCount());
                String min_temp = "						Min: " + String.valueOf(this.sensorData.getMinValue());
                String max_temp = "						Max: " + String.valueOf(this.sensorData.getMaxValue());
                String data = "Temperature" + "\n" + time + "\n" + current + "\n" + average + "\n" + samples + "\n" + min_temp + "\n" + max_temp;
                
                // log the current sensorData values 
                LOGGER.info(data);
                
                // check if the current value and the average value differ by more than the threshold
                if(Math.abs(this.sensorData.getCurrentValue() - this.sensorData.getAverageValue()) > this.threshold) {
                	
                	//send notfication if so
                	this.sendNotification(data);
                	
                }
                
                // decrement as the number of readings by 1 to keep count of how many readings left
                this.numReadings--;
                
                // sleep for time indicated in rateInSec
				try {
					Thread.sleep(this.rateInSec*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			} 
			
			//if the emulator is not enabled
			else {
				
				//generator didn't run
				return false;
				
			}
		}
		
		//generator is done running
		return true;
	}
	
	//return the reference to the sensorData variable of this task
	public SensorData getSensorData() {
		return this.sensorData;
	}
	
	//method to send the notification when currentValue differs from the threshold by more than 5
	public boolean sendNotification(String data) {
		
		//if the currentValue is greater than the threshold by more than 5
		if(this.getSensorData().getCurrentValue() >= this.getSensorData().getAverageValue()) {
			
			//log the message
			LOGGER.info("\n Current temperature exceeds average by >=" + String.valueOf(this.threshold) + " Triggering Alert");
			
			//send a message with the sensorData details
			this.smtpConnector.publishMessage("Excessive Temperature", data);
			
			//return true for successful
			return true;
		} 
		
		//if the currentValue is lesser than the threshold by more than 5
		else {
			
			//log the message
			LOGGER.info("\n Average temperature exceeds current value by >" + String.valueOf(this.threshold) + " Triggering Alert");
			
			//send a message with the sensorData details
			this.smtpConnector.publishMessage("Too low Temperature", data);
			
			//return true for successful
			return true;
		}
	}

}

