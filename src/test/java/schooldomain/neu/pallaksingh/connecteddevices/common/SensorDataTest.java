/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.common;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SensorData functionality.
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
public class SensorDataTest
{
	// setup methods
	
	//declare SensorData class;
	SensorData sensorData;
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(SensorDataTest.class.getName());
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//initialize SensorData class
		sensorData = new SensorData();
		
		LOGGER.setLevel(Level.INFO);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}
	
	// test methods
	
	/*
	 * This test tests the addValue() function of SensorData class. It checks whether the
	 * values are being correctly stored by getting the current value, the average value and the count
	 */
	@Test
	public void testAddValue()
	{
		//log the test
		LOGGER.info("Testing testAddValue()");
		
		//add any valid value
		this.sensorData.addValue(120);
		
		//the current value should represent the recently added value
		assertEquals(120, this.sensorData.getCurrentValue(), 0);
		
		//as it is the first test, the count should return 1
		assertEquals(1, this.sensorData.getCount());
		
		//add any valid value
		this.sensorData.addValue(140);
		
		//average value should return (120 + 140)/2 = 260/2 = 130 
		assertEquals(130, this.sensorData.getAverageValue(), 0);
	}
	
	/*
	 * This test tests the getAverageValue() function of SensorData class. It checks whether the average is being 
	 * accurately calculated by the function
	 */
	@Test
	public void testGetAverageValue() {
		
		//log the test
		LOGGER.info("Testing testGetAverageValue()");
		
		//add some valid values
		this.sensorData.addValue(60);
		this.sensorData.addValue(90);
		this.sensorData.addValue(180);
		
		//get the average
		float average = this.sensorData.getAverageValue();
		
		//check if average value = total value / count
		assertEquals(average, this.sensorData.totalValue/this.sensorData.totalCount, 0);
		
	}
	
	/*
	 * This test tests the getCount() function of SensorData class. It checks whether the count is 
	 * incremented properly
	 */
	@Test
	public void testGetCount() {
		
		//log the test
		LOGGER.info("Testing testGetCount()");
		
		//add some valid values
		this.sensorData.addValue(569);
		this.sensorData.addValue(900);
		this.sensorData.addValue(1809);
		
		//check if count is 3
		assertEquals(3, this.sensorData.getCount());
		
	}
	
	/*
	 * This test tests the getCurrentValue() function of SensorData class. It checks whether the currentValue is 
	 * updated properly 
	 */
	@Test
	public void testGetCurrentValue() {
		
		//log the test
		LOGGER.info("Testing testGetCurrentValue()");
		
		//add some valid value
		this.sensorData.addValue(569);
		
		//check currentValue as the most recently added value
		assertEquals(569, this.sensorData.getCurrentValue(), 0);
		
		//add some more valid values
		this.sensorData.addValue(900);
		this.sensorData.addValue(1809);
		
		//check if currentValue is the most recently added value
		assertEquals(1809, this.sensorData.getCurrentValue(),0);
		
	}
	
	/*
	 * This test tests the getMaxValue() function of SensorData class. It checks whether the maximum value is 
	 * updated properly 
	 */
	@Test
	public void testMaxValue() {
		
		//log the test
		LOGGER.info("Testing testMaxValue()");
		
		//check the max value when nothing has been added to sensorData. It should return a bogus negative value
		assertEquals(-99, this.sensorData.getMaxValue(), 0);
		
		//add some valid value
		this.sensorData.addValue(569);
		
		//check maxValue to be this value as it is the first value 
		assertEquals(569, this.sensorData.getMaxValue(), 0);
		
		//add some more valid values
		this.sensorData.addValue(900);
		this.sensorData.addValue(1809);
		
		//check if maxValue is the maximum added value
		assertEquals(1809, this.sensorData.getMaxValue(),0);
		
	}

	/*
	 * This test tests the getMinValue() function of SensorData class. It checks whether the minimum value is 
	 * updated properly 
	 */
	@Test
	public void testMinValue() {
		
		//log the test
		LOGGER.info("Testing testMinValue()");
		
		//check the min value when nothing has been added to sensorData. It should return a bogus negative value
		assertEquals(99, this.sensorData.getMinValue(), 0);
		
		//add some valid value
		this.sensorData.addValue(569);
		
		//check minValue to be this value as it is the first value 
		assertEquals(569, this.sensorData.getMinValue(), 0);
		
		//add some more valid values
		this.sensorData.addValue(900);
		this.sensorData.addValue(18);
		
		//check if maxValue is the minimum added value
		assertEquals(18, this.sensorData.getMinValue(),0);
		
	}

	/*
	 * This test tests the getName() function of SensorData class. It checks whether the name is 
	 * returned properly 
	 */
	@Test
	public void testGetName() {
		
		//log the test
		LOGGER.info("Testing testGetName()");
		
		//when name is not set, it has been initialized to 'Not Set', check if 'Not Set'
		assertEquals("Not Set", this.sensorData.getName());
		
		//set the name
		this.sensorData.setName("My Sensor");
		
		//check if name is "My Sensor"
		assertEquals("My Sensor", this.sensorData.getName());
		
		//set name to null
		this.sensorData.setName(null);
		
		//check if it changes to not set
		assertEquals("Not Set", this.sensorData.getName());
	
	}
	
	/*
	 * This test tests the setName() function of SensorData class. It checks whether the name is 
	 * updated properly 
	 */
	@Test
	public void testSetName() {
		
		//log the test
		LOGGER.info("Testing testSetName()");
		
		//set the name
		this.sensorData.setName("My Sensor");
		
		//when name is "My Sensor"
		assertEquals("My Sensor", this.sensorData.getName());
		
		//set the name
		this.sensorData.setName("My Temperature Sensor");
		
		//when name is "My Temperature Sensor"
		assertEquals("My Temperature Sensor", this.sensorData.getName());
		
		//set name to null
		this.sensorData.setName(null);
		
		//check if it changes to not set
		assertEquals("Not Set", this.sensorData.getName());
	}
		
	
	
}
