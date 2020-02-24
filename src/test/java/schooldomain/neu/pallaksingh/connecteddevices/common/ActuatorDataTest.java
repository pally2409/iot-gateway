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
public class ActuatorDataTest
{
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(ActuatorDataTest.class.getName());
	
	//declare ActuatorData
	ActuatorData actuatorData; 
	
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//instantiate ActuatorData
		this.actuatorData = new ActuatorData();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//set the reference to the variables to null to release any resources
		this.actuatorData = null;
	}
	
	// test methods
	
	/**
	 * Tests the getCommand() method of ActuatorData module. 
	 * Checks whether the command is updating appropriately, 
	 * and doesn't break if the command is set to None
	 */
	@Test
	public void testGetCommand()
	{
		//test the command when nothing has been set
		assertEquals("Not Set", this.actuatorData.getCommand());
		
		//test the command when set to 'INCREASE TEMP'
		this.actuatorData.setCommand("INCREASE TEMP");
		assertEquals("INCREASE TEMP", this.actuatorData.getCommand());
		
		//test the command remains 'INCREASE TEMP' when set to null because it should not change 
		this.actuatorData.setCommand(null);
		assertEquals("INCREASE TEMP", this.actuatorData.getCommand());
	
	}
	
	/**
	 * Tests the setCommand() method of ActuatorData module. 
	 * Checks whether the command is updating appropriately, 
	 * and doesn't break if the command is set to Null
	 */
	@Test
	public void testSetCommand()
	{
		//test the command when set to null
		this.actuatorData.setCommand(null);
		
		//it should remain Not Set
		assertEquals("Not Set", this.actuatorData.getCommand());
		
		//test the command when set to 'INCREASE TEMP'
		this.actuatorData.setCommand("INCREASE TEMP");
		assertEquals("INCREASE TEMP", this.actuatorData.getCommand());
	
	}
	
	/**
	 * Tests the getValue() method of ActuatorData module. 
	 * Checks whether the value is updating appropriately, 
	 * and doesn't break if the value is set to Null
	 */
	@Test
	public void testGetValue()
	{
		//test the value when nothing has been set
		assertEquals("Not Set", this.actuatorData.getValue());
				
		//test the value when set to 'UP'
		this.actuatorData.setValue("UP");
		assertEquals("UP", this.actuatorData.getValue());
				
		//test the value remains 'UP' when set to null because it should not change 
		this.actuatorData.setValue(null);
		assertEquals("UP", this.actuatorData.getValue());
	
	}
	
	/**
	 * Tests the setValue() method of ActuatorData module. 
	 * Checks whether the value is updating appropriately, 
	 * and doesn't break if the value is set to Null
	 */
	@Test
	public void testSetValue()
	{
		//test the value when set to null
		this.actuatorData.setCommand(null);
		
		//it should remain Not Set
		assertEquals("Not Set", this.actuatorData.getValue());
		
		//test the value when set to 'UP'
		this.actuatorData.setValue("UP");
		assertEquals("UP", this.actuatorData.getValue());
	
	}
	
	/**
	 * Tests the getName() method of ActuatorData module. 
	 * Checks whether the name is updating appropriately, 
	 * and doesn't break if the name is set to Null
	 */
	@Test
	public void testGetName()
	{
		//test the name when nothing has been set
		assertEquals("Not Set", this.actuatorData.getName());
				
		//test the name when set to 'Temperature'
		this.actuatorData.setName("Temperature");
		assertEquals("Temperature", this.actuatorData.getName());
				
		//test the name remains 'Temperature' when set to null because it should not change 
		this.actuatorData.setName(null);
		assertEquals("Temperature", this.actuatorData.getName());
	
	}
	
	/**
	 * Tests the setName() method of ActuatorData module. 
	 * Checks whether the name is updating appropriately, 
	 * and doesn't break if the name is set to Null
	 */
	@Test
	public void testSetName()
	{
		//test the name when set to null
		this.actuatorData.setName(null);
		
		//it should remain Not Set
		assertEquals("Not Set", this.actuatorData.getName());
		
		//test the name when set to 'Temperature'
		this.actuatorData.setName("Temperature");
		assertEquals("Temperature", this.actuatorData.getName());
	
	}
	
	
	
}
