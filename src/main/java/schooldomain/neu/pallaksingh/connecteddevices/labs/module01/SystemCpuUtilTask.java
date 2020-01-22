package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

import java.lang.management.*;

public class SystemCpuUtilTask {
	
	public float getSensorData() {
		return (float) ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();
	}
	

}
