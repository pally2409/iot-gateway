/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.labs;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtilTest;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module02.SmtpClientConnector;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module02.TempEmulatorAdaptor;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module02.TempSensorEmulatorTask;

/**
 * Test class for all requisite Module02 functionality.
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
public class Module02Test
{
	// setup methods
	
	//declare SMTPClientConnector
	SmtpClientConnector smtpClientConnector;
	
	//declare TempSensorEmulatorTask
	TempSensorEmulatorTask tempSensorEmulatorTask;
	
	//declare TempEmulatorAdaptor
	TempEmulatorAdaptor tempEmulatorAdaptor;
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(Module02Test.class.getName());
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//initialize SMTPClientConnector
		smtpClientConnector = new SmtpClientConnector();
		
		//initialize TempSensorEmulatorTask
		tempSensorEmulatorTask = new TempSensorEmulatorTask();
		
		//initialize TempEmulatorAdaptor
		tempEmulatorAdaptor = new TempEmulatorAdaptor();
		
		LOGGER.setLevel(Level.INFO);
		
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		// set the reference to the variables as none to release the resources they're holding
		this.smtpClientConnector = null;
		this.tempSensorEmulatorTask = null;
		this.tempEmulatorAdaptor = null;
	}
	
	// test methods
	
	/**
	 * This method tests the publishMessage() of SmtpClientConnector class. It checks whether the SmtpClientConnector is able to successfully 
	 * send an email or not by testing it in various scenarios like when the configurations are correct, wrong configurations, wrong values 
	 * in configurations, connection failure
	 */
	@Test
	public void testPublishMessage()
	{
		//log the test
		LOGGER.info("Testing testGetBooleanProperty()");
		
		//when testing on the pipeline on cloud, the config file has been ignored. hence, load sample config file
		if(this.smtpClientConnector.getConfig().isConfigFileLoaded() == false) {
			this.smtpClientConnector.getConfig().loadConfig("/sample/ConnectedDevicesConfig_NO_EDIT_TEMPLATE_ONLY.props");
		}
		
		//when default config file is loaded properly (testing on the computer)
		else {
			
			//when configurations are correct
			boolean success = this.smtpClientConnector.publishMessage("Testing Message", "This is a test");
			
			//test true on success of mail
			assertEquals(success, true);
		}
		
		//load the sample config file where fromAddr and toAddr, authToken are not valid
		this.smtpClientConnector.getConfig().loadConfig("/sample/ConnectedDevicesConfig_NO_EDIT_TEMPLATE_ONLY.props");
		
		//test false on success of mail 
		boolean success = this.smtpClientConnector.publishMessage("Testing Message", "This is a test");
		assertEquals(success, false);
		

	}
	
	/**
	 * This method tests the generateRandomTemperature() function of the TempSensorEmulatorTask class. It checks whether the 
	 * generator runs in various scenarios such as disabling the emulator, setting the number of readings to 0
	 */
	@Test
	public void testGenerateRandomTemperature()
	{
		//log the test
		LOGGER.info("Testing testGenerateRandomTemperature()");
		
		//enable the emulator
		this.tempSensorEmulatorTask.setEnableEmulator(true);
		
		//change numReadings to a small finite value to check
		this.tempSensorEmulatorTask.setNumReadings(3);
		
		//change sleep time (rateInSec) to a small amount
		this.tempSensorEmulatorTask.setRateInSec(1);
		
		//run when numReadings > 0 and adaptor is enabled
		assertEquals(true, this.tempSensorEmulatorTask.generateRandomTemperature());
		
		//change numReadings to 0
		this.tempSensorEmulatorTask.setNumReadings(0);
		
		//run when numReadings = 0 and emulator is enabled, should return false because generator didn't run
		assertEquals(false, this.tempSensorEmulatorTask.generateRandomTemperature());
		
		//disable the emulator
		this.tempSensorEmulatorTask.setEnableEmulator(false);
		
		//change numReadings to > 0
		this.tempSensorEmulatorTask.setNumReadings(0);
		
		//run when numReadings > 0 and emulator is disabled, should return false because generator didn't run
		assertEquals(false, this.tempSensorEmulatorTask.generateRandomTemperature());
		
	}
	
	/**
	 * This method tests the getSensorData() function of the TempSensorEmulatorTask class. It simply checks
	 * whether the reference to the sensorData of tempSensorEmulatorTask is valid or not
	 */
	@Test
	public void testGetSensorData()
	{
		//log the test
		LOGGER.info("Testing testGetSensorData()");
		
		//check type of the return of the method, should be of type SensorData
		assertEquals(this.tempSensorEmulatorTask.getSensorData(), (SensorData)this.tempSensorEmulatorTask.getSensorData());
		
		//check if the returned sensorData reference belongs to the tempSensorEmulatorTas
		SensorData sensorData = this.tempSensorEmulatorTask.getSensorData();
		
		//add a value to returned sensorData
		sensorData.addValue(30);
		
		//check if the sensorData value is same as the one added
		assertEquals(30, this.tempSensorEmulatorTask.getSensorData().getCurrentValue(), 0);
		
	}
	
	/**
	 * This method tests the sendNotification function of the TempSensorEmulatorTask class. It simply whether
	 * the notification is being sent or not. This has been shown in documentation using screenshot of the
	 * email
	 */
	@Test
	public void testSendNotification()
	{
		//log the test
		LOGGER.info("Testing testSendNotification()");
		
		//if the config file is loaded: while testing on system
		if(this.tempSensorEmulatorTask.getSmtpConnector().getConfig().isConfigFileLoaded() == true) {
			
			//returns true, notification is being sent
			assertEquals(true, this.tempSensorEmulatorTask.sendNotification("Hello Testing"));
			
		} 
		
	}
	
	/**
	 * This method tests the run() function of the tempEmulatorAdaptor. It tests whether the run is calling the generateRandomTemperature() of
	 * tempSensorEmulatorTask appropriately
	 */
	@Test
	public void testTempEmulatorAdaptor()
	{
		//log the test
		LOGGER.info("Testing testTempEmulatorAdaptor()");
		
		//get the reference to the tempSensorEmulatorTask
		TempSensorEmulatorTask tempSensTask = this.tempEmulatorAdaptor.getTempSensorEmulatorTask();
		
		//change numReadings to a small finite value to check
		tempSensTask.setNumReadings(3);
		
		//change sleep time (rateInSec) to a small amount
		tempSensTask.setRateInSec(1);
		
		//enable the tempEmulatorTask's emulator
		tempSensTask.setEnableEmulator(true);
		
		//run the run function of tempEmulatorAdaptor and get the value of success of the adaptor
		this.tempEmulatorAdaptor.run();
		
		//check whether the success is true
		assertEquals(true, this.tempEmulatorAdaptor.isSuccess());
		
		//change numReadings to 0
		tempSensTask.setNumReadings(0);
		
		//run when numReadings = 0 and emulator is enabled, should return false because generator didn't run
		this.tempEmulatorAdaptor.run();
		assertEquals(false, this.tempEmulatorAdaptor.isSuccess());
		
		//disable the emulator
		tempSensTask.setEnableEmulator(false);
		
		//change numReadings to > 0
		tempSensTask.setNumReadings(0);
		
		//run when numReadings > 0 and emulator is disabled, should return false because generator didn't run
		assertEquals(false, this.tempEmulatorAdaptor.isSuccess());
		
	}

	
	
}
