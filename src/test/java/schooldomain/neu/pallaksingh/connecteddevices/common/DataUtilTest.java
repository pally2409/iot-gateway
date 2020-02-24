/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.common;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for DataUtil functionality.
 * 
 * Instructions:
 * 1) Rename 'testSomething()' method such that 'Something' is specific to your needs; add others as needed, beginning each method with 'test...()'.
 * 2) Add the '@Test' annotation to each new 'test...()' method you add.
 * 3) Import the relevant modules and classes to support your tests.
 * 4) Run this class as unit test app
 * 5) Include a screen shot of the report when you submit your assignment
 * 
 * Please note: While some example test cases may be provided, you must write your own for the class.
 */
public class DataUtilTest
{
	// setup methods
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(DataUtilTest.class.getName());
		
	//declare DataUtil
	DataUtil dataUtil; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		
		//instantiate DataUtil
		this.dataUtil = new DataUtil();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//set the reference to the variables to null to release any resources
		this.dataUtil = null;
	}
	
	// test methods
	
	/**
	 * This method tests the toJsonFromActuatorData() of DataUtil class
	 * Checks if two actuatorData instances with same values produce the same 
	 * JSONStr
	 */
	@Test
	public void testActuatorDataToJson()
	{
		//Create actuatorData
		ActuatorData actuatorData1 = new ActuatorData();
				
		//Create two actuatorData instances and check if their jsonStr are equal
		actuatorData1.setName("Temp Actuator");
		actuatorData1.setCommand("INCREASE");
		actuatorData1.setValue("UP");
				
		//Create actuatorData
		ActuatorData actuatorData2 = new ActuatorData();
						
		//Create two actuatorData instances and check if their jsonStr are equal
		actuatorData2.setName("Temp Actuator");
		actuatorData2.setCommand("INCREASE");
		actuatorData2.setValue("UP");
				
		//Get their JSON strings 
		String jsonStr1 = this.dataUtil.toJsonFromActuatorData(actuatorData1);
		String jsonStr2 = this.dataUtil.toJsonFromActuatorData(actuatorData2);
				
		//Check for equality
		assertEquals(jsonStr1, jsonStr2);
		
	}
	
	/**
	 * @throws IOException 
	 * This method tests writeSensorDataToFile() of DataUtil class.
	 * It tests if there is any error in writing the file and if 
	 * it breaks if the file doesn't exist.
	 * 
	 */
	@Test
	public void testwriteSensorDataToFile() throws IOException
	{
		//Create a sensorData instance
		SensorData sensorData = new SensorData();
								
		//Create two sensor Data instances and check if their jsonStr are equal
		//First sensorData 
		sensorData.setName("Temperature Sensor");
		sensorData.addValue(9);
		
		//Write to file (screenshot included in assignment documentation)
		//and test if true
		assertEquals(true, this.dataUtil.writeSensorDataToFile(sensorData));
		
		//Change the file Name to something bogus that doesn't exist, it should return false
		this.dataUtil.sensorDataLogFile = "/logfiles/blahblah.log";
		assertEquals(false, this.dataUtil.writeSensorDataToFile(sensorData));
	}
	
	/**
	 * This method tests the toJsonFromSensorData() of DataUtil class
	 * Checks if two sensorData instances with same values produce the same 
	 * JSONStr 
	 */
	@Test
	public void testSensorDataToJson()
	{
		//Create sensorData
		SensorData sensorData1 = new SensorData();
						
		//Create two sensor Data instances and check if their jsonStr are equal
		//First sensorData 
		sensorData1.setName("Temperature Sensor");
		sensorData1.addValue(9);
		
		//set timestamp to None because it is the only thing that will be different
		sensorData1.timestamp = "None";
						
		//Create actuatorData
		SensorData sensorData2 = new SensorData();
								
		//Second sensorData
		sensorData2.setName("Temperature Sensor");
		sensorData2.addValue(9);
		
		//set timestamp to None because it is the only thing that will be different
		sensorData2.timestamp = "None";
						
		//Get their JSON strings 
		String jsonStr1 = this.dataUtil.toJsonFromSensorData(sensorData1);
		String jsonStr2 = this.dataUtil.toJsonFromSensorData(sensorData2);
						
		//Check for equality
		assertEquals(jsonStr1, jsonStr2);
							
	}
	
	/**
	 * This method tests the toActuatorDataFromJson() of DataUtil class
	 * Checks if the actuatorData instances created from JSONString are equivalent in values
	 * and if they have the same data type
	 */
	@Test
	public void testJsonToActuatorData()
	{
		
		//Create actuatorData
		ActuatorData actuatorData = new ActuatorData();
				
		//Set some command, name and value
		actuatorData.setName("Temp Actuator");
		actuatorData.setCommand("INCREASE");
		actuatorData.setValue("UP");
				
		//convert to JSON 
		String jsonStr = this.dataUtil.toJsonFromActuatorData(actuatorData);
				
		//convert back to ActuatorData
		ActuatorData actuatorDataTest = this.dataUtil.toActuatorDataFromJson(jsonStr);
				
		//test if their variables are equal
		assertEquals("Temp Actuator", actuatorDataTest.getName());
		assertEquals("INCREASE", actuatorDataTest.getCommand());
		assertEquals("UP", actuatorDataTest.getValue());
				
		/*
			* Set the variable to a double value, check if the conversion to JsonStr and back 
			* to ActuatorData value remains of double type
		*/
				
		//Set some command, name and double value
		actuatorData.setName("Temp Actuator");
		actuatorData.setCommand("INCREASE");
		actuatorData.setValue(6.0);
				
		//convert to JSON 
		jsonStr = this.dataUtil.toJsonFromActuatorData(actuatorData);
				
		//convert back to ActuatorData
		actuatorDataTest = this.dataUtil.toActuatorDataFromJson(jsonStr);
				
		//test if their variables are equal
		assertEquals("Temp Actuator", actuatorDataTest.getName());
		assertEquals("INCREASE", actuatorDataTest.getCommand());
		assertEquals(6.0, actuatorDataTest.getValue());
	}
	
	/**
	 * This method tests writeActuatorDataToFile() of DataUtil class.
	 * It tests if there is any error in writing the file and if 
	 * it breaks if the file doesn't exist.
	 */
	@Test
	public void testwriteActuatorDataToFile()
	{
		//Create a sensorData instance
		ActuatorData actuatorData = new ActuatorData();
										
		//Create two sensor Data instances and check if their jsonStr are equal
		//First sensorData 
		actuatorData.setName("Temperature Sensor");
		actuatorData.setCommand("INCREASE");
		actuatorData.setValue("UP");
		
				
		//Write to file (screenshot included in assignment documentation)
		//and test if true
		assertEquals(true, this.dataUtil.writeActuatorDataToFile(actuatorData));
				
		//Change the file Name to something bogus that doesn't exist, it should return false
		this.dataUtil.actuatorDataLogFile = "/logfiles/blahblah.log";
		assertEquals(false, this.dataUtil.writeActuatorDataToFile(actuatorData));
	}
	
	/**
	 * This method tests the toSensorDataFromJson() of DataUtil class
	 * Checks if the sensorData instances created from JSONString are equivalent in values
	 * and if they have the same data type
	 */
	@Test
	public void testJsonToSensorData()
	{
		//Create sensorData
		SensorData sensorData = new SensorData();
						
		//Add some value and set a name
		sensorData.addValue((float)9.0);
		sensorData.setName("Temperature Sensor");
						
		//convert to JSON 
		String jsonStr = this.dataUtil.toJsonFromSensorData(sensorData);
						
		//convert back to ActuatorData
		SensorData sensorDataTest = this.dataUtil.toSensorDataFromJson(jsonStr);
						
		//test if their variables are equal
		assertEquals("Temperature Sensor", sensorDataTest.getName());
		assertEquals(9.0, (double)sensorDataTest.getCurrentValue(), 0);
	}
	
}
