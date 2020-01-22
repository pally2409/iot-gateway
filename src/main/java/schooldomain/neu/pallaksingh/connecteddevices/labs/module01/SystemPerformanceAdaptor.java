package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemPerformanceAdaptor extends Thread {
	
	long rateInSec;
	int runTime;
	boolean enableAdaptor;
	private final static Logger LOGGER = Logger.getLogger(SystemPerformanceAdaptor.class.getName());
	
	SystemPerformanceAdaptor(int runTime) {
		this.rateInSec = 10;
		this.runTime = runTime;
		LOGGER.setLevel(Level.INFO);
	}
	
	SystemPerformanceAdaptor(int rateInSec, int runTime) {
		this.rateInSec = rateInSec;
		this.runTime = runTime;
		LOGGER.setLevel(Level.INFO);
	}
	
	public void run() {
		
		SystemCpuUtilTask systemCpuUtilTask = new SystemCpuUtilTask();
		SystemMemUtilTask systemMemUtilTask = new SystemMemUtilTask();
		LOGGER.info("Starting System Performance Adaptor Thread");
		
		while(this.runTime > 0) {
			if(this.enableAdaptor == true) {
				float cpuUtil = systemCpuUtilTask.getSensorData();
				float heapMemUtil = (float)systemMemUtilTask.getSensorData()[0];
				float nonHeapMemUtil = (float) systemMemUtilTask.getSensorData()[1];
				String cpuPerformanceData = "CPU Utilization: " + String.valueOf(cpuUtil);
				String memPerformanceData = "Heap Memory Utilization: " + String.valueOf(heapMemUtil) + " Non Heap Memory Utilization: " + String.valueOf(nonHeapMemUtil);
				LOGGER.info(cpuPerformanceData);
				LOGGER.info(memPerformanceData);
				try {
					Thread.sleep(rateInSec*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.runTime--;
			}
		}
		
		this.enableAdaptor = false;
	}

}
