/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.labs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import schooldomain.neu.pallaksingh.connecteddevices.labs.module05.GatewayDataManager;

/**
 * Test class for all requisite Module05 functionality.
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
public class Module05Test
{
	//initialize GatewayDataManager
	GatewayDataManager gatewayDataManager;
	
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//instantiate
		this.gatewayDataManager = new GatewayDataManager();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//set to null to release any resources 
		this.gatewayDataManager = null;
	}
	
	// test methods
	
	/**
	 * This method tests the start() method of GatewayDataManager 
	 */
	@Test
	public void testStart()
	{
		//run only on system
		if(this.gatewayDataManager.pUtil.jedis_sensor.isConnected()) {
			
			//check if it runs appropriately
			assertEquals(this.gatewayDataManager.start(), true);
		}
	
	}
	
}
