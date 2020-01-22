package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class SystemMemUtilTask {
	
	public double[] getSensorData() {
		MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heapMemUsage = memBean.getHeapMemoryUsage();
		MemoryUsage nonHeapMemUsage = memBean.getNonHeapMemoryUsage();
		
		double heapMemUtil = ((double) heapMemUsage.getUsed()/heapMemUsage.getMax())*100.0;
		double nonHeapMemUtil = ((double) nonHeapMemUsage.getUsed()/nonHeapMemUsage.getMax())*100.0;
		
		if(heapMemUtil < 0) 	{heapMemUtil= 0.0;}
		if(nonHeapMemUtil < 0)  {nonHeapMemUtil= 0.0;}
		
		double[] memValues = {heapMemUtil, nonHeapMemUtil};
		return memValues;	
	}

}
