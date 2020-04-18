package schooldomain.neu.pallaksingh.connecteddevices.project;

//import libraries
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

public class SystemMemUtilTask {
	
	SensorData sensorData = new SensorData();
	
	
	
	public SystemMemUtilTask() {
		super();
		// TODO Auto-generated constructor stub
		this.sensorData.setName("Gateway Mem");
	}



	// method to return the CPU utilization  
	public SensorData getSensorData() {
		
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
		sensorData.addValue((float)heapMemUtil);
		return sensorData;	
	}

}
