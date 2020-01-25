package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemPerformanceAdaptor extends Thread {
	
	long rateInSec; //how often the readings should be taken
	int numReadings; //how many times the readings should be taken
	boolean enableAdaptor = false; //disable adapter for the SystemPerformanceAdaptor initially
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(SystemPerformanceAdaptor.class.getName());
	
	
	//constructor when only provided numReadings, then set rateInSec to default value of 10
	SystemPerformanceAdaptor(int numReadings) {
		this.rateInSec = 10;
		this.numReadings = numReadings;
		LOGGER.setLevel(Level.INFO);
	}
	
	//constructor when provided numReadings and rateInSec 
	SystemPerformanceAdaptor(int rateInSec, int numReadings) {
		this.rateInSec = rateInSec;
		this.numReadings = numReadings;
		LOGGER.setLevel(Level.INFO);
	}
	
	//method called when the adapter thread starts 
	public void run() {
		
		// instantiate the tasks
		SystemCpuUtilTask systemCpuUtilTask = new SystemCpuUtilTask();
		SystemMemUtilTask systemMemUtilTask = new SystemMemUtilTask();
		
		//log the initial message
		LOGGER.info("Starting System Performance Adaptor Thread");
		
		//run the loop as indicated in the numReadings variable
		while(this.numReadings > 0) {
			
			// if the adaptor is enabled
			if(this.enableAdaptor == true) {
				
				// get the readings from the corresponding tasks
				float cpuUtil = systemCpuUtilTask.getSensorData();
				float heapMemUtil = (float)systemMemUtilTask.getSensorData()[0];
				float nonHeapMemUtil = (float) systemMemUtilTask.getSensorData()[1];
				
				// prepare the string to be logged
				String cpuPerformanceData = "CPU Average Load: " + String.valueOf(cpuUtil);
				String memPerformanceData = "Heap Memory Utilization: " + String.valueOf(heapMemUtil);
				
				// log the data
				LOGGER.info(cpuPerformanceData);
				LOGGER.info(memPerformanceData);
				
				// decrement as the number of readings by 1 to keep count of how many readings left
				this.numReadings--;
				
				// sleep for time indicated in rateInSec
				try {
					Thread.sleep(rateInSec*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		
		//disable the adaptor once done
		this.enableAdaptor = false;
	}

}
