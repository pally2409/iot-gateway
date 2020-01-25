/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.labs;

import static org.junit.Assert.assertTrue;

import org.junit.After;

import org.junit.Before;
import org.junit.Test;

import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemCpuUtilTask;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemMemUtilTask;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemPerformanceAdaptor;

/**
 * Test class for all requisite Module01 functionality.
 * 
 * Instructions:
 * 1) Rename 'testSomething()' method such that 'Something' is specific to your needs; add others as needed, beginning each method with 'test...()'.
 * 2) Add the '@Test' annotation to each new 'test...()' method you add.
 * 3) Import the relevant modules and classes to support your tests.
 * 4) Run this class as unit test app.
 * 5) Include a screen shot of the report when you submit your assignment.
 * 
 * Please note: While some example test cases may be provided, you must write your own for the class.
 */
public class Module01Test
{
	/**
	 * @throws java.lang.Exception
	 */
	
	//declare the tasks
	SystemPerformanceAdaptor systemPerformanceAdaptor;
	
	//declare the adaptor
	SystemCpuUtilTask systemCpuUtilTask;
	SystemMemUtilTask systemMemUtilTask;
	
	// setup method
	@Before
	public void setUp() throws Exception
	{
		// instantiate the tasks
		this.systemCpuUtilTask = new SystemCpuUtilTask();
		this.systemMemUtilTask = new SystemMemUtilTask();
		
		// instantiate the adaptor
		this.systemPerformanceAdaptor = new SystemPerformanceAdaptor();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	
	//tear down method
	@After
	public void tearDown() throws Exception
	{
		// set the reference to the tasks as none to release the resources they're holding
		this.systemCpuUtilTask = null;
		this.systemMemUtilTask = null;
		
		// set the reference to the adaptor as none to release the resources they're holding
		this.systemPerformanceAdaptor = null;
				
	}
	
	// test methods
	/** 
	 * This test tests whether the CPU utilization percent returned by the corresponding task method
	 * is greater than 0.0 and less than or equal to 100.0
	 */
	@Test
	public void testSystemCpuUtilTask()
	{
		assertTrue("CPU Utilization less than 0", this.systemCpuUtilTask.getSensorData() >= 0);
		assertTrue("CPU Utilization greater than 100", this.systemCpuUtilTask.getSensorData() <= 100);
	}
	
	/** 
	 * This test tests whether the Memory utilization percent returned by the corresponding task method
	 * is greater than 0.0 and less than or equal to 100.0
	 */
	@Test
	public void testSystemMemUtilTask()
	{
		assertTrue("Heap Memory Utilization less than 0.0", this.systemMemUtilTask.getSensorData()[0] >= 0.0);
		assertTrue("Non Heap Memory Utilization greater than 100", this.systemMemUtilTask.getSensorData()[1] <= 100);
		assertTrue("Heap Memory Utilization greater than 100", this.systemMemUtilTask.getSensorData()[0] <= 100);
		assertTrue("Non Heap Memory Utilization less than 0.0", this.systemMemUtilTask.getSensorData()[1] >= 0.0);
	}
	
	/** 
	 * This test tests whether the system performance adaptor returns the accurate success value according to 
	 * whether or not it performed the task of getting and logging the values from the CPU and Memory tasks
	 */
	@Test
	public void testSystemPerformanceAdaptor() 
	{
		// return value from system performance adaptor's run function
		boolean success = this.systemPerformanceAdaptor.getSuccess();
		
		// check if the return is false when the enableAdaptor variable of adaptor is false
		assertTrue("Adaptor did not run because adaptor was disabled", success == false);
		
		// set the number of readings required to 0
		this.systemPerformanceAdaptor.setNumReadings(0);
		
		// return value from system performance adaptor's run function
		success = this.systemPerformanceAdaptor.getSuccess();
		
		// check if the return is false when the number of readings required were 0
		assertTrue("Adaptor did not run because number of readings entered were 0", success == false);
		
		// set the number of readings to a small value 
		this.systemPerformanceAdaptor.setNumReadings(1);
		
		// set the sleep time to a small value
		this.systemPerformanceAdaptor.setRateInSec(3);
		
		// enable the adaptor
		this.systemPerformanceAdaptor.setEnableAdaptor(true);
		
		// return value from system performance adaptor's run function
		success = this.systemPerformanceAdaptor.getSuccess();
			
		// check if the return is true when the adaptor runs and provides some readings
		assertTrue("Adaptor did run", success == false);
				
	}
	
}
