package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

// import libraries
import java.lang.management.*;

public class SystemCpuUtilTask {
	
	// method to return the CPU utilization  
	public float getSensorData() {
		
		// get the Operating System MXBean for OS related attributes and from it get the system load average
		return (float) ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
	}
	

}
