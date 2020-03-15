/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.labs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module07.CoapClientConnector;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module07.CoapServerManager;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module07.TempResourceHandler;

/**
 * Test class for all requisite Module07 functionality.
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
public class Module07Test
{
	//Declare classes from Module 7 that need to be tested
	CoapClientConnector coapClientConnector;
	CoapServerManager coapServerManager;
	TempResourceHandler tempResourceHandler;
	
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//Instantiate classes from Module 7 that need to be tested
		this.coapClientConnector 			= new CoapClientConnector();
		this.coapServerManager 				= new CoapServerManager();
		this.tempResourceHandler			= new TempResourceHandler("Testing");	
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//Set the reference to classes in Module 7 to NULL to release any resources 
		this.coapClientConnector 			= null;
		this.coapServerManager 				= null;
		this.tempResourceHandler 			= null;	
	}
	
	// test methods
	
	/** TEST METHODS FOR CoapClientConnector */
	
	/**
	 * This method tests the createCoapClient() method of the
	 * CoapClientConnector class. It tests for successful
	 * creation of the client when it is given the valid host
	 * and for an indication when invalid host is given
	 */
	@Test
	public void testCreateCoapClient()
	{
		//Assert true for successful creation when valid host is given
		assertEquals(this.coapClientConnector.createCoapClient("coap.me:5683", "test"), true);
		
		//Assert false indicating failure to create client when invalid host is given
		assertEquals(this.coapClientConnector.createCoapClient("invalid", "other/block"), false);
	}
	
	/**
	 * This method tests the sendGETRequest() method of the
	 * CoapClientConnector class. It tests for successful
	 * creation of the client when it is given the valid host
	 * and for an indication when invalid host is given
	 */
	@Test
	public void testSendGETRequest()
	{
		//Create CoAP client with valid host
		this.coapClientConnector.createCoapClient("coap.me:5683", "test");
		
		//Assert true for successful sending of the message CoAP Client is initialized with valid host
		assertEquals(this.coapClientConnector.sendGETRequest(), true);
		
		//Create CoAP client with invalid host
		this.coapClientConnector.createCoapClient("invalid", "test");
		
		//Assert false indicating failure to send message when CoAP Client is initialized with invalid host
		assertEquals(this.coapClientConnector.sendGETRequest(), false);
	}
	
	/**
	 * This method tests the sendPOSTRequest() method of the
	 * CoapClientConnector class. It tests for successful
	 * creation of the client when it is given the valid host
	 * and for an indication when invalid host is given
	 */
	@Test
	public void testSendPOSTRequest()
	{
		//Create CoAP client with valid host
		this.coapClientConnector.createCoapClient("coap.me:5683", "test");
		
		//Assert true for successful sending of the message CoAP Client is initialized with valid host
		assertEquals(this.coapClientConnector.sendPOSTRequest(), true);
		
		//Create CoAP client with invalid host
		this.coapClientConnector.createCoapClient("invalid", "test");
		
		//Assert false indicating failure to send message when CoAP Client is initialized with invalid host
		assertEquals(this.coapClientConnector.sendPOSTRequest(), false);
	}
	
	/**
	 * This method tests the sendPUTRequest() method of the
	 * CoapClientConnector class. It tests for successful
	 * creation of the client when it is given the valid host
	 * and for an indication when invalid host is given
	 */
	@Test
	public void testSendPUTRequest()
	{
		//Create CoAP client with valid host
		this.coapClientConnector.createCoapClient("coap.me:5683", "test");
		
		//Assert true for successful sending of the message CoAP Client is initialized with valid host
		assertEquals(this.coapClientConnector.sendPUTRequest(), true);
		
		//Create CoAP client with invalid host
		this.coapClientConnector.createCoapClient("invalid", "test");
		
		//Assert false indicating failure to send message when CoAP Client is initialized with invalid host
		assertEquals(this.coapClientConnector.sendPUTRequest(), false);
	}
	
	/**
	 * This method tests the sendDELETERequest() method of the
	 * CoapClientConnector class. It tests for successful
	 * creation of the client when it is given the valid host
	 * and for an indication when invalid host is given
	 */
	@Test
	public void testSendDELETERequest()
	{
		//Create CoAP client with valid host
		this.coapClientConnector.createCoapClient("coap.me:5683", "test");
		
		//Assert true for successful sending of the message CoAP Client is initialized with valid host
		assertEquals(this.coapClientConnector.sendDELETERequest(), true);
		
		//Create CoAP client with invalid host
		this.coapClientConnector.createCoapClient("invalid", "test");
		
		//Assert false indicating failure to send message when CoAP Client is initialized with invalid host
		assertEquals(this.coapClientConnector.sendDELETERequest(), false);
	}
	
	/** TEST METHODS FOR CoapServerManager */
	
	/**
	 * This method tests the addResource() method of the
	 * CoapServerManager class. It always returns a true
	 */
	@Test
	public void testAddResource() {
		
		//This method always returns a true because server side, there aren't any errors since this is only server creation and starting/stopping
		assertEquals(this.coapServerManager.addResource(this.tempResourceHandler), true);		
	}
	
	/**
	 * This method tests the startServer() method of the
	 * CoapServerManager class. It always returns a true
	 */
	@Test
	public void testStartServer() {
		
		//This method always returns a true because server side, there aren't any errors since this is only server creation and starting/stopping
		assertEquals(this.coapServerManager.startServer(), true);		
	}
	
	/**
	 * This method tests the stopServer() method of the
	 * CoapServerManager class. It always returns a true
	 */
	@Test
	public void testStopServer() {
		
		//This method always returns a true because server side, there aren't any errors since this is only server creation and starting/stopping
		assertEquals(this.coapServerManager.stopServer(), true);		
	}
	
	/** TEST METHODS FOR TempResourceHandler */
	
	/**
	 * This method tests the convertIncomingDataToSensorData() of TempResourceHandler class
	 * It tests the sensorData values when a valid SensorData JSON String
	 * is sent to the method and a boolean value indicating success in
	 * conversion according to whether or not a valid SensorData String was
	 * sent to it
	 */
	@Test
	public void testConvertIncomingDataToSensorData() {
		
		//Instantiate DataUtil required for SensorData to JSON conversion required for this testing
		DataUtil dUtil = new DataUtil();
		
		//Create a sensorData object
		SensorData sensorData = new SensorData();
		
		//Add a valid value to sensorData
		sensorData.addValue(9);
		
		//Convert it to JSON string
		String sensorDataString = dUtil.toJsonFromSensorData(sensorData);
		
		//Assert true when valid string sent to the method
		assertEquals(this.tempResourceHandler.convertIncomingDataToSensorData(sensorDataString), true);
		
		//Check the value of SensorData reference of the TempResourceHandler
		assertEquals(this.tempResourceHandler.getSensorData().getCurrentValue(), 9, 0);
		
		//Assert false when an invalid string sent to the method
		assertEquals(this.tempResourceHandler.convertIncomingDataToSensorData("Random string"), false);	
	}
}
