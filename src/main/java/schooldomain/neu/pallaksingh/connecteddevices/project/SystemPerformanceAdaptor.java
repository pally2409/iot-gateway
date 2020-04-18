package schooldomain.neu.pallaksingh.connecteddevices.project;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/**
 * This class performs the fetching of the current system performance data from the corresponding tasks
 * and publishes it to Ubidots
 * 
 * @author pallaksingh
 */
public class SystemPerformanceAdaptor extends Thread {
		
	//Initialize the parameters for the operation of this class
	long rateInSec; 									//How often the readings should be taken
	boolean enableAdaptor; 								//Adapter for the SystemPerformanceAdaptor 
	boolean success; 									//Success for the adaptor thread
	boolean earlyStopping;								//Used during testing so that the thread does not run forever
	SystemCpuUtilTask systemCpuUtilTask;				//Task for fetching current CPU utilization
	SystemMemUtilTask systemMemUtilTask;				//Task for fetching current memory utilization
	UbidotsClientConnector ubidotsClientConnector;		//UbidotsClientConnector for publishing the gateway system performance data
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(SystemPerformanceAdaptor.class.getName());
	
	/**
	 * Constructor when not provided any parameters except UbidotsClientConnector is used to
	 * initialize the UbidotsClientConnector reference along with instantiating the corresponding
	 * performance tasks. It is also used to initialize the parameter for rateInSec to a default of 15 and
	 * disable the adaptor by default so that no fetching occurs
	 * 
	 * @param ubidotsClientConnector
	 * 					Reference for UbidotsClientConnector to be used for publishing the system utilization data to Ubidots
	 */
	public SystemPerformanceAdaptor(UbidotsClientConnector ubidotsClientConnector) {
		
		//Set the frequency of readings to 15 seconds by default
		this.rateInSec 				= 15;
		
		//Set the level for the logger
		LOGGER.setLevel(Level.INFO);
		
		//Set the reference for the UbidotsClientConnector
		this.ubidotsClientConnector = ubidotsClientConnector;
		
		//Instantiate the task for CPU utilization
		systemCpuUtilTask 			= new SystemCpuUtilTask();
		
		//Instantiate the task for memory utilization
		systemMemUtilTask 			= new SystemMemUtilTask();
		
		//Disable the adaptor by default
		this.enableAdaptor 			= false;
		
		//Disable early stopping variable by default. Only to be used during testing
		this.earlyStopping			= false;
		
		//Log the initial message
		LOGGER.info("Starting System Performance Adaptor Thread");
	}
	
	/**
	 * Constructor when provided rateinSec parameter and UbidotsClientConnector is used to initialize the 
	 * rateInSec parameter initialize the UbidotsClientConnector reference along with instantiating the corresponding
	 * performance tasks. It is also used to disable the adaptor by default so that no fetching occurs
	 * 
	 * @param ubidotsClientConnector
	 * 				Reference for UbidotsClientConnector to be used for publishing the system utilization data to Ubidots
	 * @param rateInSec 
	 * 				Frequency of readings to be taken (duration of sleep after each reading has been taken	
	 */
	public SystemPerformanceAdaptor(UbidotsClientConnector ubidotsClientConnector, int rateInSec) {
		
		//Set the frequency of readings to 15 seconds by default
		this.rateInSec 				= rateInSec;
		
		//Set the level for the logger
		LOGGER.setLevel(Level.INFO);
		
		//Set the reference for the UbidotsClientConnector
		this.ubidotsClientConnector = ubidotsClientConnector;
		
		//Instantiate the task for CPU utilization
		systemCpuUtilTask 			= new SystemCpuUtilTask();
		
		//Instantiate the task for memory utilization
		systemMemUtilTask 			= new SystemMemUtilTask();
		
		//Disable the adaptor by default
		this.enableAdaptor 			= false;
		
		//Log the initial message
		LOGGER.info("Starting System Performance Adaptor Thread");
	}
	
	/**
	 * Method to return the success, indicating whether or not any readings were taken
	 * 
	 * @return A boolean value indicating whether any readings were taken
	 */
	public boolean isSuccess() {
		
		//Return the success variable indicating whether any readings were taken
		return success;
	}

	/**
	 * Method to enable/disable the fetcher 
	 * 
	 * @param enableAdaptor
	 * 			The boolean value to enable/disable the fetcher to indicate whether readings should be taken
	 */
	public void enableAdaptor(boolean enableAdaptor) {
		
		//Set the fetcher to the parameter passed in the setter
		this.enableAdaptor = enableAdaptor;
	}

	/**
	 * Method to set the earlyStopping variable so that the loop doesn't run forever
	 * in the new thread during testing
	 * 
	 * @param earlyStopping
	 * 			The boolean value to enable/disable earlyStopping so that the thread doesn't
	 * 			run forever
	 */
	public void setEarlyStopping(boolean earlyStopping) {
		
		//Set the earlyStopping variable to the parameter passed in the setter
		this.earlyStopping = earlyStopping;
	}

	/**
	 * This method calls the SystemCpuUtilTask and SystemMemUtilTask's getSensorData() method
	 * to return the current CPU and Memory utilization. It publishes the data to Ubidots  
	 * after. It only runs every few seconds set by the rateInSec variable
	 */
	public void run() {
				
		//Log the initial message
		LOGGER.info("Starting System Performance Adaptor Thread");
		
		//Only run if the adaptor is enabled
		if(this.enableAdaptor == true) {
			
			//Run an infinite loop
			while(true) {
				
				//Get the readings from the corresponding tasks
				SensorData cpuUtil 				= systemCpuUtilTask.getSensorData();	//Get the CPU utilization
				SensorData heapMemUtil 			= systemMemUtilTask.getSensorData();	//Get the memory utilization
				
				//Set the success variable to true as atleast one reading was taken
				this.success					= true;
				
				//Check if we are only running the method during testing, then break out of the loop and not publish the data
				if(this.earlyStopping == true) { 
					
					//break out of the infinite loop
					break;
				}
				
				//Publish the data to Ubidots
				this.ubidotsClientConnector.publishSensorData(cpuUtil, 2);
				this.ubidotsClientConnector.publishSensorData(heapMemUtil, 2);
				
				//Sleep for time indicated in rateInSec
				try {
					
					//Sleep for seconds indicated by rateInSec
					Thread.sleep(rateInSec*1000);
				} 
				
				//If an error occurred
				catch (InterruptedException e) {
					
					//Print the error trace
					e.printStackTrace();
				}		
			}
		} 
		
		//If the adaptor is disabled
		else {
			
			//Set the success variable to indicate no readings were taken
			this.success				= false;
		}
	}
}
