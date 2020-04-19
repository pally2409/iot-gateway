/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.labs;

import static org.junit.Assert.assertEquals;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module07.CoapClientConnector;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module07.CoapServerManager;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module07.TempResourceHandler;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module08.ActuatorValueListener;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module08.MqttClientConnector;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module08.SensorDataListener;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module08.UbidotsClientConnector;

/**
 * Test class for all requisite Module08 functionality.
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
public class Module08Test
{
	//Declare classes from Module 8 that need to be tested
	MqttClientConnector mqttClientConnector;
	UbidotsClientConnector ubidotsClientConnector;
	
	//Declare SensorDataListener which is the callback for SensorData from constrained device
	static SensorDataListener sensorDataListener;
		
	//Declare ActuatorValueListener which is the callback for Actuator values from Ubidots
	static ActuatorValueListener actuatorValueListener;
	
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//Instantiate objects from Module 8 that need to be tested
		this.mqttClientConnector 		= new MqttClientConnector("tcp://broker.mqttdashboard.com:1883");		//MqttClientConnector object
		this.ubidotsClientConnector		= new UbidotsClientConnector();											//UbidotsClientConnector object		
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//Set the reference of variables to None to release any resources 
		this.mqttClientConnector 	= null;		//MqttClientConnector object
		this.ubidotsClientConnector	= null;		//UbidotsClientConnector object
	}
	
	// test methods
	
	/**
	 * This method tests the publishActuatorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 * @throws MqttException 
	 */
	@Test
	public void testPublishActuatorData() 
	{
		//Connect with valid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboard.com:1883");
		
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
		
		//Assert true for when the client can publish when connected
		assertEquals(this.mqttClientConnector.publishActuatorData("Connected-Devices/Actuator_Data",actuatorData, 2), true);
		
		//Connect with invalid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboardd.com:1883");
				
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
	
	/**
	 * This method tests the publishSensorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 * @throws MqttException 
	 */
	@Test
	public void testPublishSensorData() 
	{
		//Connect with valid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboard.com:1883");

		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Create sensorData
		SensorData sensorData = new SensorData();
		
		//Add some value
		sensorData.addValue(9);
		
		//Assert true for when the client can publish when connected
		assertEquals(this.mqttClientConnector.publishSensorData("Connected-Devices/Sensor_Data", sensorData, 2), true);
		
		//Connect with invalid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboardd.com:1883");
				
		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//Assert false for failure to publish with no connection
		assertEquals(this.mqttClientConnector.publishSensorData("Connected-Devices/Sensor_Data", sensorData, 2), false);
	}
	
	/**
	 * This method tests the subscribeToSensorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 * @throws MqttException 
	 */
	@Test
	public void testSubscribeToSensorData() 
	{
		//Connect with valid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboard.com:1883");
		
		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//Get the status of success of subscription when broker url is valid
		boolean success = this.mqttClientConnector.subscribeToSensorData("Connected-Devices/Sensor_Data", sensorDataListener, 2);
		
		//Assert true for when the client can subscribe when connected
		assertEquals(success, true);
		
		//Connect with invalid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboardd.com:1883");
				
		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Get the status of success of subscription when broker url is invalid
		success = this.mqttClientConnector.subscribeToSensorData("Connected-Devices/Sensor_Data", sensorDataListener, 2);
				
		//Assert false for failure to subscribe with no connection
		assertEquals(String.valueOf(success) + " is not equal to false", success, false);	
	}	
	
	/**
	 * This method tests the subscribeToActuatorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 * @throws MqttException 
	 */
	@Test
	public void testSubscribeToActuatorData() 
	{
		//Connect with valid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboard.com:1883");
		
		//Get the status of success of subscription when broker url is valid
		boolean success = this.mqttClientConnector.subscribeToActuatorData("Connected-Devices/Actuator_Data", actuatorValueListener, 2);

		//Assert true for when the client can subscribe when connected
		assertEquals(String.valueOf(success) + " is not equal to true" ,success, true);
		
		//connect with invalid values of host and port
		this.mqttClientConnector =  new MqttClientConnector("tcp://broker.mqttdashboardd.com:1883");
		
		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Get the status of success of subscription when broker url is invalid
		success = this.mqttClientConnector.subscribeToActuatorData("Connected-Devices/Actuator_Data", actuatorValueListener, 2);
				
		//Assert false for failure to publish with no connection
		assertEquals(String.valueOf(success) + " is not equal to false", success, false);
	}
	
	/**
	 * This method tests the publishSensorValue() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 * @throws MqttException 
	 */
	@Test
	public void testPublishSensorValue() 
	{
		//Connect with valid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboard.com:1883");

		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Create sensorData
		SensorData sensorData = new SensorData();
		
		//Add some value
		sensorData.addValue(9);
		
		//Assert true for when the client can publish when connected
		assertEquals(this.mqttClientConnector.publishSensorValue("Connected-Devices/Sensor_Data", sensorData, 2), true);
		
		//Connect with invalid values of host and port
		this.mqttClientConnector = new MqttClientConnector("tcp://broker.mqttdashboardd.com:1883");
				
		//Sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//Assert false for failure to publish with no connection
		assertEquals(this.mqttClientConnector.publishSensorValue("Connected-Devices/Sensor_Data", sensorData, 2), false);
	}
	
	/**
	 * This method tests the publishSensorValue() method of the UbidotsClientConnector class
	 * It checks whether it is able to publish to Ubidots if sent Humidity and Temperature 
	 * sensor data and asserts false when given any other value because it is not a variable
	 * on Ubidots  
	 */
	@Test
	public void testPublishSensorDataUbidots() 
	{
		//Create sensorData
		SensorData sensorData = new SensorData();
		
		//Add some value
		sensorData.addValue(9);
		
		//Test only when on the system and not on the pipeline
		if(this.ubidotsClientConnector.getcUtil().isConfigFileLoaded()) {
			
			//Set a name
			sensorData.setName("Temperature");
			
			//Assert true for when it can publish temperature data to ubidots
			assertEquals(this.ubidotsClientConnector.publishSensorData(sensorData, 2),true);
			
			//Change the sensor name name to Humidity
			sensorData.setName("Humidity");
			
			//Assert true for when it can publish humidity data to ubidots
			assertEquals(this.ubidotsClientConnector.publishSensorData(sensorData, 2),true);
			
			//Set another name for the sensor
			sensorData.setName("Not a name");	
		}
		
		//Assert false because the sensor name is not a valid name as no variable for it exists on ubidots
		assertEquals(this.ubidotsClientConnector.publishSensorData(sensorData, 2),false);
	}	
}
