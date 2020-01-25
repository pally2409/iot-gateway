package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

//import libraries
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class SystemMemUtilTask {
	
	// method to return the CPU utilization  
	public double[] getSensorData() {
		
		// get the MXBean for memory related attributes 
		MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
		
		//from MemoryMXBean get the heap and non memory heap usage
		MemoryUsage heapMemUsage = memBean.getHeapMemoryUsage();
		MemoryUsage nonHeapMemUsage = memBean.getNonHeapMemoryUsage();
		
		//get the utilization by dividing the used by the maximum available memory
		double heapMemUtil = ((double) heapMemUsage.getUsed()/heapMemUsage.getMax())*100.0;
		double nonHeapMemUtil = ((double) nonHeapMemUsage.getUsed()/nonHeapMemUsage.getMax())*100.0;
		
		//set to zero if negative
		if(heapMemUtil < 0) 	{heapMemUtil= 0.0;}
		if(nonHeapMemUtil < 0)  {nonHeapMemUtil= 0.0;}
		
		// return as an array
		double[] memValues = {heapMemUtil, nonHeapMemUtil};
		return memValues;	
	}

}
