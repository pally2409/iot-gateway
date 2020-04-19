/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Hashtable;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/**
 * Test class for all requisite Project functionality.
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
public class ProjectTest
{
	//Declare classes needed for the testing of the Project
	ConfigUtil confUtil;
	DataUtil dUtil;
	
	//Declare classes from Project that need to be tested
	CoapServerManager coapServerManager;				//The abstraction for all Coap server related tasks
	IMUDataHandler imuDataHandler;						//The data handler for accelerometer raw data consisting the algorithm for detecting fall from derived values of the data
	IMUSensorData imuSensorData;						//Object custom built to hold X, Y and Z attributes for accelerometer and gyroscope
	MqttClientConnector mqttClientConnector;			//The abstraction for all MQTT client related tasks
	SmtpClientConnector smtpClientConnector;			//The abstraction for sending mail via SMTP
	SystemCpuUtilTask systemCpuUtilTask;				//Task for gathering CPU utilization data
	SystemMemUtilTask systemMemUtilTask;				//Task for gathering Memory utilization data
	UbidotsClientConnector ubidotsClientConnector;		//The abstraction for all Ubidots related tasks
	SystemPerformanceAdaptor systemPerformanceAdaptor;	//Adaptor for gathering system performance data
		
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//Declare classes needed for the testing of the Project
		dUtil									= new DataUtil();
		confUtil								= new ConfigUtil();
		
		//Initialize classes from Project that need to be tested
		coapServerManager						= new CoapServerManager();
		imuDataHandler							= new IMUDataHandler();
		imuSensorData							= new IMUSensorData();
		mqttClientConnector						= new MqttClientConnector("tcp://broker.mqttdashboard.com:1883", dUtil);
		smtpClientConnector						= new SmtpClientConnector(confUtil);
		systemCpuUtilTask						= new SystemCpuUtilTask();
		systemMemUtilTask						= new SystemMemUtilTask();
		ubidotsClientConnector					= new UbidotsClientConnector(confUtil, dUtil);
		systemPerformanceAdaptor 				= new SystemPerformanceAdaptor(ubidotsClientConnector);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//Set all variables to null to release any resources
		dUtil									= null;
		confUtil								= null;
		coapServerManager						= null;
		imuDataHandler							= null;
		imuSensorData							= null;
		mqttClientConnector						= null;
		smtpClientConnector						= null;
		systemCpuUtilTask						= null;
		systemMemUtilTask						= null;
		ubidotsClientConnector					= null;
		systemPerformanceAdaptor 				= null;
	}
	
	// test methods
	/** TEST METHODS FOR SystemMemUtilTask */
	
	/**
	 * This method tests the class SystemMemUtilTask's getSensorData() method, it checks
	 * whether the method returns a valid percentage for the memory utilization which is 
	 * between 0 and 100
	 */
	@Test
	public void testSystemMemUtilTask()
	{
		//Initialize the SensorData object with the object returned from SystemMemUtilTask's getSensorData() method
		SensorData sensorData			= this.systemMemUtilTask.getSensorData();
		
		//Assert that the memory utilization is greater than 0
		assertTrue("Memory Utilization cannot be less than 0. Found current value to be less than zero", sensorData.getCurrentValue() >= 0.0);
		
		//Assert that the memory utilization is less than or equal to 100
		assertTrue("Memory Utilization cannot exceed 100. Found current value to be exceeding zero", sensorData.getCurrentValue() <= 100);
	}
	
	/** TEST METHODS FOR SystemCpuUtilTask */
	
	/**
	 * This method tests the class SystemCpuUtilTask's getSensorData() method, it checks
	 * whether the method returns a valid percentage for the CPU utilization which is 
	 * between 0 and 100
	 */
	@Test
	public void testSystemCpuUtilTask()
	{
		//Initialize the SensorData object with the object returned from SystemCpuUtilTask's getSensorData() method
		SensorData sensorData			= this.systemCpuUtilTask.getSensorData();
		
		//Assert that the CPU utilization is greater than 0
		assertTrue("CPU Utilization cannot be less than 0. Found current value to be less than zero", sensorData.getCurrentValue() >= 0.0);
		
		//Assert that the CPU utilization is less than or equal to 100
		assertTrue("CPU Utilization cannot exceed 100. Found current value to be exceeding zero", sensorData.getCurrentValue() <= 100);
	}
	
	/** TEST METHODS FOR SystemPerformanceAdaptor */
	
	/**
	 * This method tests the class SystemPerformanceAdaptor's run() method. Since the run
	 * method is an override from the Thread class, we only test whether the readings are
	 * taken in accordance with whether or not the adaptor is enabled or disabled. It asserts a
	 * false for success variable when the adaptor has been disabled and asserts a true when
	 * the adaptor has been enabled to indicate readings are taken only if the adaptor
	 * is enabled
	 */
	@Test
	public void testSystemPerformanceAdaptor()
	{
		//Set the earlyStopping variable to true so that the thread doesn't run forever
		this.systemPerformanceAdaptor.setEarlyStopping(true);

		//Run the run() method 
		this.systemPerformanceAdaptor.run();
		
		//Assert that no readings are taken when the adaptor is disabled by checking the success variable
		assertTrue("No readings are to be taken when enableAdaptor is disabled", this.systemPerformanceAdaptor.isSuccess() == false);
		
		//Enable the adaptor
		this.systemPerformanceAdaptor.enableAdaptor(true);
		
		//Call the run method again
		this.systemPerformanceAdaptor.run();
		
		//Assert that readings are taken when the adaptor is enabled by checking the success variable
		assertTrue("Readings are to be taken as the adaptor is enabled", this.systemPerformanceAdaptor.isSuccess() == true);
	}
	
	/** TEST METHODS FOR UbidotsClientConnector */
	
	/**
	 * This method tests the publishSensorData() method of UbidotsClientConnector. It simply checks
	 * whether an exception is caught and indicated by the boolean variable returned by the method if
	 * there is no appropriate variable for the sensor
	 */
	@Test
	public void testPublishSensorDataUbidots() 
	{
		//Only test when testing on the system
		if(this.ubidotsClientConnector.getcUtil().isConfigFileLoaded() == true) {
			
			//Create a new SensorData object
			SensorData sensorData 			= new SensorData();
			
			//Add a value to the SensorData
			sensorData.addValue(0);
			
			//Assert false when we don't have a name for the sensor and hence, no variable on Ubidots cloud
			assertTrue("Ubidots didn't publish sensor data as it did not find a variable ID for the sensor", this.ubidotsClientConnector.publishSensorData(sensorData, 2) == false);
			
			//Set a valid name for the sensor which has a variable on Ubidots cloud
			sensorData.setName("Emergency");
			
			//Assert true when we have a valid name for the sensor and hence, a variable on Ubidots cloud
			assertTrue("Ubidots published the sensor data as it has a variable ID for the sensor", this.ubidotsClientConnector.publishSensorData(sensorData, 2) == true);		
		}
	}
	
	/**
	 * This method tests the publishIMUSensorData() method of UbidotsClientConnector. It simply checks
	 * whether an exception is caught and indicated by the boolean variable returned by the method if
	 * there is no appropriate variable for the IMUSensor
	 */
	@Test
	public void testPublishIMUSensorDataUbidots() 
	{
		//Only test when testing on the system
		if(this.ubidotsClientConnector.getcUtil().isConfigFileLoaded() == true) {
			
			//Create a new SensorData object
			IMUSensorData imuSensorData 					= new IMUSensorData();
			
			//Create a Hashtable with values
			Hashtable<String, Double> imuSensorValue		= new Hashtable<String, Double>();	
			
			//Add values to the hashtable to create a valid value for IMUSensorData object
			imuSensorValue.put("x", (double) 0);			//X attribute
			imuSensorValue.put("y", (double) 0);			//Y attribute
			imuSensorValue.put("z", (double) 0);			//Z attribute
			
			//Set the values for the imuSensor
			imuSensorData.addValue(imuSensorValue);
			
			//Assert false when we don't have a name for the sensor and hence, no variable on Ubidots cloud
			assertTrue("Ubidots didn't publish IMU sensor data as it did not find a variable ID for the IMUSensor", this.ubidotsClientConnector.publishIMUSensorData(imuSensorData, 2) == false);
			
			//Set a valid name for the sensor which has a variable on Ubidots cloud
			imuSensorData.setName("Gyroscope");
			
			//Assert true when we have a valid name for the sensor and hence, a variable on Ubidots cloud
			assertTrue("Ubidots published the IMU sensor data as it has a variable ID for the IMUSensor", this.ubidotsClientConnector.publishIMUSensorData(imuSensorData, 2) == true);		
		}
	}
	
	/** TEST METHODS FOR SmtpClientConnector */
	
	/**
	 * This method tests the publishMessage() method of SmtpClientConnector. It simply checks
	 * whether an exception is caught and indicated by the boolean variable returned by the method if
	 * there is an error while making the SMTP connection such as wrong port number or not valid
	 * authentication
	 */
	@Test
	public void testPublishMessage()
	{	
		//Only test when testing on the system
		if(this.smtpClientConnector.getcUtil().isConfigFileLoaded() == true) {
			
			//Assert true when the config file is valid and the message should be able to send
			assertTrue("Message was sent successfully because valid configuration", this.smtpClientConnector.publishMessage("Testing Message", "This is a test") == true);
			
			//Load the sample config file where SMTP configurations are not valid
			this.smtpClientConnector.getcUtil().loadConfig("/sample/ConnectedDevicesConfig_NO_EDIT_TEMPLATE_ONLY.props");
			
			//Assert false when the config file is not valid and the message should not be able to send
			assertTrue("Message was not sent successfully because invalid configuration", this.smtpClientConnector.publishMessage("Testing Message", "This is a test") == false);	
		}
	}
	
	/** TEST METHODS FOR IMUSensorData */
	
	/**
	 * This method tests the addValue() method of IMUSensorData class. It checks whether the
	 * values are being correctly stored by getting the current value, the average value and the count
	 */
	@Test
	public void testAddValue()
	{	
		//Create a Hashtable with values
		Hashtable<String, Double> imuSensorValue		= new Hashtable<String, Double>();
		
		//Add an invalid value
		imuSensorValue.put("m", (double) 9);
		
		//Add it to the IMUSensorData object and assert false as it is invalid data format
		assertTrue("The data format was invalid, could not add value", this.imuSensorData.addValue(imuSensorValue) == false);

		//Add values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 9);			//X attribute
		imuSensorValue.put("y", (double) 10);			//Y attribute
		imuSensorValue.put("z", (double) 11);			//Z attribute
		
		//Add any valid value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Add it to the IMUSensorData object and assert true as it is a valid data format
		assertTrue("The data format was valid", this.imuSensorData.addValue(imuSensorValue) == true);
		
		//The current value should represent the recently added value
		assertTrue("The current value for x attribute was 9", this.imuSensorData.getCurrentValue().get("x") == (double)9);
		assertTrue("The current value for y attribute was 10", this.imuSensorData.getCurrentValue().get("y") == (double)10);
		assertTrue("The current value for z attribute was 11", this.imuSensorData.getCurrentValue().get("z") == (double)11);
		
		//As it is the first value added, the count should return 1
		assertEquals(2, this.imuSensorData.getCount());
		
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 14);			//X attribute
		imuSensorValue.put("y", (double) 15);			//Y attribute
		imuSensorValue.put("z", (double) 16);			//Z attribute
		
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Average value should return 11.5, 12.5 and 13.5 respectively
		assertTrue("The average value for x attribute was 11.5", this.imuSensorData.getAverageValue().get("x") == (((double)9+(double)9+(double)14)/3));
		assertTrue("The average value for y attribute was 12.5", this.imuSensorData.getAverageValue().get("y") == (((double)10+(double)10+(double)15)/3));
		assertTrue("The average value for z attribute was 13.5", this.imuSensorData.getAverageValue().get("z") == (((double)11+(double)11+(double)16)/3));
	}
	
	/**
	 * This method tests the getAverageValue() function of IMUSensorData class. It checks whether the average is being 
	 * accurately calculated by the method
	 */
	@Test
	public void testGetAverageValue() 
	{
		//Create a Hashtable with values
		Hashtable<String, Double> imuSensorValue		= new Hashtable<String, Double>();	
				
		//Add values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 9);			//X attribute
		imuSensorValue.put("y", (double) 10);			//Y attribute
		imuSensorValue.put("z", (double) 11);			//Z attribute
				
		//Add any valid value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 14);			//X attribute
		imuSensorValue.put("y", (double) 15);			//Y attribute
		imuSensorValue.put("z", (double) 16);			//Z attribute
		
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Average value should return 11.5, 12.5 and 13.5 respectively
		assertTrue("The average value for x attribute was 11.5", this.imuSensorData.getAverageValue().get("x") == (((double)9+(double)14)/2));
		assertTrue("The average value for y attribute was 12.5", this.imuSensorData.getAverageValue().get("y") == (((double)10+(double)15)/2));
		assertTrue("The average value for z attribute was 13.5", this.imuSensorData.getAverageValue().get("z") == (((double)11+(double)16)/2));	
	}
	
	/**
	 * This method tests the getCount() function of IMUSensorData class. It checks whether the count is 
	 * incremented properly
	 */
	@Test
	public void testGetCount() 
	{
		//Create a Hashtable with values
		Hashtable<String, Double> imuSensorValue		= new Hashtable<String, Double>();	
						
		//Add values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 9);			//X attribute
		imuSensorValue.put("y", (double) 10);			//Y attribute
		imuSensorValue.put("z", (double) 11);			//Z attribute
						
		//Add any valid value
		this.imuSensorData.addValue(imuSensorValue);
				
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 14);			//X attribute
		imuSensorValue.put("y", (double) 15);			//Y attribute
		imuSensorValue.put("z", (double) 16);			//Z attribute
		
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 19);			//X attribute
		imuSensorValue.put("y", (double) 20);			//Y attribute
		imuSensorValue.put("z", (double) 21);			//Z attribute
				
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 19);			//X attribute
		imuSensorValue.put("y", (double) 20);			//Y attribute
		imuSensorValue.put("z", (double) 21);			//Z attribute
					
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Assert true that the count is now 4 as 4 readings were added
		assertTrue("4 readings were added", this.imuSensorData.getCount() == 4);
	}
	
	/**
	 * This method tests the getCurrentValue() function of SensorData class. It checks whether the currentValue is 
	 * updated properly 
	 */
	@Test
	public void testGetCurrentValue() 
	{
		Hashtable<String, Double> imuSensorValue		= new Hashtable<String, Double>();
		
		//Add it to the IMUSensorData object and assert false as it is invalid data format
		assertTrue("The data format was invalid, could not add value", this.imuSensorData.addValue(imuSensorValue) == false);
		
		//Add values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 9);			//X attribute
		imuSensorValue.put("y", (double) 10);			//Y attribute
		imuSensorValue.put("z", (double) 11);			//Z attribute
		
		//Add any valid value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Add it to the IMUSensorData object and assert true as it is a valid data format
		assertTrue("The data format was valid", this.imuSensorData.addValue(imuSensorValue) == true);
		
		//The current value should represent the recently added value
		assertTrue("The current value for x attribute was 9", this.imuSensorData.getCurrentValue().get("x") == (double)9);
		assertTrue("The current value for y attribute was 10", this.imuSensorData.getCurrentValue().get("y") == (double)10);
		assertTrue("The current value for z attribute was 11", this.imuSensorData.getCurrentValue().get("z") == (double)11);	
	}
	
	/**
	 * This method tests the getMaxValue() function of SensorData class. It checks whether the maximum value is 
	 * updated properly 
	 */
	@Test
	public void testMaxValue() 
	{
		//Create a Hashtable with values
		Hashtable<String, Double> imuSensorValue		= new Hashtable<String, Double>();	
								
		//Add values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 9);			//X attribute
		imuSensorValue.put("y", (double) 10);			//Y attribute
		imuSensorValue.put("z", (double) 11);			//Z attribute
		
		//Add any valid value
		this.imuSensorData.addValue(imuSensorValue);
		
		//The max value should represent the recently added value as it is the first one
		assertTrue("The max value for x attribute was 9", this.imuSensorData.getMaxValue().get("x") == (double)9);
		assertTrue("The max value for y attribute was 10", this.imuSensorData.getMaxValue().get("y") == (double)10);
		assertTrue("The max value for z attribute was 11", this.imuSensorData.getMaxValue().get("z") == (double)11);	
						
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 14);			//X attribute
		imuSensorValue.put("y", (double) 15);			//Y attribute
		imuSensorValue.put("z", (double) 16);			//Z attribute
		
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);			
		
		//The max value should represent the recently added value as it is the greater one
		assertTrue("The max value for x attribute was 14", this.imuSensorData.getMaxValue().get("x") == (double)14);
		assertTrue("The max value for y attribute was 15", this.imuSensorData.getMaxValue().get("y") == (double)15);
		assertTrue("The max value for z attribute was 16", this.imuSensorData.getMaxValue().get("z") == (double)16);			
		
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 2);			//X attribute
		imuSensorValue.put("y", (double) 4);			//Y attribute
		imuSensorValue.put("z", (double) 20);			//Z attribute
						
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);
				
		//The max value should represent different values
		assertTrue("The max value for x attribute was 14", this.imuSensorData.getMaxValue().get("x") == (double)14);
		assertTrue("The max value for y attribute was 15", this.imuSensorData.getMaxValue().get("y") == (double)15);
		assertTrue("The max value for z attribute was 20", this.imuSensorData.getMaxValue().get("z") == (double)20);			
	}

	/**
	 * This method tests the getMinValue() function of IMUSensorData class. It checks whether the minimum value is 
	 * updated properly 
	 */
	@Test
	public void testMinValue() 
	{
		//Create a Hashtable with values
		Hashtable<String, Double> imuSensorValue		= new Hashtable<String, Double>();	
										
		//Add values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 9);			//X attribute
		imuSensorValue.put("y", (double) 10);			//Y attribute
		imuSensorValue.put("z", (double) 11);			//Z attribute
		
		//Add any valid value
		this.imuSensorData.addValue(imuSensorValue);
				
		//The min value should represent the recently added value as it is the first one
		assertTrue("The min value for x attribute was 9", this.imuSensorData.getMinValue().get("x") == (double)9);
		assertTrue("The min value for y attribute was 10", this.imuSensorData.getMinValue().get("y") == (double)10);
		assertTrue("The min value for z attribute was 11", this.imuSensorData.getMinValue().get("z") == (double)11);	
								
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 3);			//X attribute
		imuSensorValue.put("y", (double) 2);			//Y attribute
		imuSensorValue.put("z", (double) 1);			//Z attribute
				
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);			
				
		//The min value should represent the recently added value as it is the greater one
		assertTrue("The min value for x attribute was 3", this.imuSensorData.getMinValue().get("x") == (double)3);
		assertTrue("The min value for y attribute was 2", this.imuSensorData.getMinValue().get("y") == (double)2);
		assertTrue("The min value for z attribute was 1", this.imuSensorData.getMinValue().get("z") == (double)1);			
				
		//Add more values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 0);			//X attribute
		imuSensorValue.put("y", (double) 4);			//Y attribute
		imuSensorValue.put("z", (double) 20);			//Z attribute
								
		//Add the value
		this.imuSensorData.addValue(imuSensorValue);
						
		//The min value should represent different values
		assertTrue("The min value for x attribute was 0", this.imuSensorData.getMinValue().get("x") == (double)0);
		assertTrue("The min value for y attribute was 2", this.imuSensorData.getMinValue().get("y") == (double)2);
		assertTrue("The min value for z attribute was 1", this.imuSensorData.getMinValue().get("z") == (double)1);
	}

	/**
	 * This method tests the getName() function of IMUSensorData class. It checks whether the name is 
	 * returned properly 
	 */
	@Test
	public void testGetName() 
	{
		//When name is not set, it has been initialized to 'Not Set', check if 'Not Set'
		assertEquals("Not Set", this.imuSensorData.getName());
		
		//Set the name
		this.imuSensorData.setName("My Sensor");
		
		//Check if name is "My Sensor"
		assertEquals("My Sensor", this.imuSensorData.getName());
	}
	
	/**
	 * This method tests the setName() function of IMUSensorData class. It checks whether the name is 
	 * updated properly 
	 */
	@Test
	public void testSetName() 
	{
		//set the name
		this.imuSensorData.setName("My Sensor");
		
		//when name is "My Sensor"
		assertEquals("My Sensor", this.imuSensorData.getName());
		
		//set the name
		this.imuSensorData.setName("My Temperature Sensor");
		
		//when name is "My Temperature Sensor"
		assertEquals("My Temperature Sensor", this.imuSensorData.getName());
		
		//set name to null
		this.imuSensorData.setName(null);
		
		//check if it changes to not set
		assertEquals("Not Set", this.imuSensorData.getName());
	}
	
	/** TEST METHODS FOR CoapServerManager */
	
	/**
	 * This method tests the addResource() method of the CoapServerManager class.
	 * It simply checks whether an exception is caught and indicated by the boolean variable returned by the method if
	 * there is a null value passed instead of a resource handler
	 */
	@Test
	public void testAddResource() 
	{
		//Assert true if added a valid resource handler to the server manager
		assertEquals(this.coapServerManager.addResource(new SystemPerformanceResourceHandler("Random", this.dUtil, this.ubidotsClientConnector, this.smtpClientConnector)), true);
		
		//Assert false if added a null to the server manager
		assertTrue(this.coapServerManager.addResource(null) == false);	
	}
	
	/**
	 * This method tests the stopServer() method of the CoapServerManager class. It always returns a true
	 */
	@Test
	public void testStopServer() 
	{
		
		//This method always returns a true because server side, there aren't any errors since this is only server creation and starting/stopping
		assertEquals(this.coapServerManager.stopServer(), true);		
	}
	
	/** TEST METHODS FOR IMUDataHandler */
	
	/**
	 * This method tests the convertToRPY() method of IMUDataHandler class. It simply checks whether the derivation
	 * from accelerometer x, y, z values to roll, pitch and yaw values is accurate
	 */
	@Test
	public void testConvertToRPY() 
	{
		//Create a Hashtable with values
		Hashtable<String, Double> imuSensorValue		= new Hashtable<String, Double>();	
												
		//Add values to the hashtable to create a valid value for IMUSensorData object
		imuSensorValue.put("x", (double) 9);			//X attribute
		imuSensorValue.put("y", (double) 10);			//Y attribute
		imuSensorValue.put("z", (double) 11);			//Z attribute
				
		//Add any valid value
		this.imuSensorData.addValue(imuSensorValue);
		
		//Set a name
		this.imuSensorData.setName("Accelerometer");
		
		//Get the conversion from the DataHandler method
		double[] rph = this.imuDataHandler.convertToRPY(imuSensorData.getCurrentValue().get("x"), imuSensorData.getCurrentValue().get("y"), imuSensorData.getCurrentValue().get("z"));
		
		//Assign the imu variables to their own temporary variables
		double x = (double) 9;
		double y = (double) 10;
		double z = (double) 11;
		
		//Set the constant value of pi
		double PI = 3.14159265358979323846;

		//Check for accurate roll, pitch and yaw values calculations
		assertTrue("Error in roll value", 	rph[1] == 180 * Math.atan (x/Math.sqrt((y*y) + (z*z)))/PI); 	//Roll
		assertTrue("Error in pitch value", 	rph[0] == 180 * Math.atan (y/Math.sqrt(x*x + (z*z)))/PI); 		//Pitch
		assertTrue("Error in yaw value", 	rph[2] == 180 * Math.atan (z/Math.sqrt(x*x + (z*z)))/PI); 		//Yaw	
	}
	
	/**
	 * This method tests the checkFall() method of IMUDataHandler class. It simply checks whether the derivation
	 * from accelerometer x, y, z values to roll, pitch and yaw values correctly determines whether
	 * was a fall
	 */
	@Test
	public void testCheckFall() 
	{	
		//Assign the imu variables to their own temporary variables
		double x = (double) 35;
		double y = (double) 40;
		double z = (double) 40;
		
		//Assert false for no fall occured
		assertTrue("No fall occured", this.imuDataHandler.checkFall(x, y, z) == false);
	}
	
	/** TEST METHODS FOR MqttClientConnector */
	
	/**
	 * This method tests the publishActuatorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host 
	 */
	@Test
	public void testPublishActuatorData() 
	{
		//Connect with valid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboard.com:1883", this.dUtil);
		
		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Create actuatorData
		ActuatorData actuatorData = new ActuatorData();
		
		//Add some command
		actuatorData.setCommand("INCREASE");
		
		//Add some value
		actuatorData.setValue(9);
		
		//Assert true for when the client can publish when connected
		assertEquals(this.mqttClientConnector.publishActuatorData("Connected-Devices/Actuator_Data",actuatorData, 2), true);
		
		//Connect with invalid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboardd.com:1883", this.dUtil);
				
		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//Assert false for failure to publish with no connection
		assertEquals(this.mqttClientConnector.publishActuatorData("Connected-Devices/Actuator_Data", actuatorData, 2), false);
	}
}
