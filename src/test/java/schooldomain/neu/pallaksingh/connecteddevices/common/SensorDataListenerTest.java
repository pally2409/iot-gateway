/**
import libraries and packages
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.common;
import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for all requisite ActuatorData functionality.
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
public class SensorDataListenerTest
{
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(SensorDataListenerTest.class.getName());
	
	//declare ActuatorData
	SensorDataListener sensorDataListener; 
	
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//instantiate ActuatorData
		this.sensorDataListener = new SensorDataListener();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//set the reference to the variables to null to release any resources
		this.sensorDataListener = null;
	}
	
	// test methods
	
	/**
	 * Tests the onPMessage() method of SensorDataListener class. 
	 * Checks whether the Actuation is appropriately triggered
	 */
	@Test
	public void testONPMessage()
	{
		//when running on system try
		try {
			
			//send some valid message to the listener
			this.sensorDataListener.onPMessage("__keyspace@0__:*", "__keyspace@0__:sensorDatae19e3d04-dbc3-4d81-a0bc-6f045d275af6", "set");
			
			//assert actuatorData values 
			assertEquals(this.sensorDataListener.actuatorData.getName(), "Temperature Actuator");
			assertEquals(this.sensorDataListener.actuatorData.getCommand(), "DECREASE TEMP");
			assertEquals(this.sensorDataListener.actuatorData.getValue(), "UP");	
		} 
		
		// if running on pipeline
		catch(Exception e) {
			
			e.printStackTrace();
		}
		
	
	}

	
}
