package schooldomain.neu.pallaksingh.connecteddevices.project;

// import libraries
import java.lang.management.*;

import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

public class SystemCpuUtilTask {
	
	SensorData sensorData = new SensorData();
	
	
	public SystemCpuUtilTask() {
		super();
		// TODO Auto-generated constructor stub
		this.sensorData.setName("Gateway CPU");
	}


	// method to return the CPU utilization  
	public SensorData getSensorData() {
		
		// get the Operating System MXBean for OS related attributes and from it get the system load average
		this.sensorData.addValue((float) ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
		return this.sensorData;
	}
	

}
