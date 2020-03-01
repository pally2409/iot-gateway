/**
 * 
 */
package schooldomain.neu.pallaksingh.connecteddevices.labs;

import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.PersistenceUtilTest;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module06.MqttClientConnector;

/**
 * Test class for all requisite Module06 functionality.
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
public class Module06Test
{
	
	//define MqttClientConnector
	MqttClientConnector mqttClientConnector;
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(Module06Test.class.getName());
	
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		
		//initialize MqttClientConnector
		this.mqttClientConnector = new MqttClientConnector();
		
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//initialize MqttClientConnector
		this.mqttClientConnector = null;	
	}
	
	// test methods
	
	/**
	 * This method tests the connect() method of the MqttClientConnector class
	 * It checks whether the program breaks if the host and port are invalid
	 * and if it runs properly with valid host and port 
	 */
	@Test
	public void testConnect()
	{
		//connect with valid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboard.com:1883", "Subscriber_Pallak");
		
		//get the client reference of MqttClientConnector
		MqttClient client = this.mqttClientConnector.getClient();
		
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check whether the client is connected
		assertEquals(client.isConnected(), true);
		
		//connect with invalid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboardd.com:1883", "Subscriber_Pallak");
				
		//get the client reference of MqttClientConnector
		client = this.mqttClientConnector.getClient();
				
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//check whether the client is connected
		assertEquals(client.isConnected(), false);
		
	}
	
	
	
	/**
	 * This method tests the publishActuatorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 */
	@Test
	public void testPublishActuatorData()
	{
		//connect with valid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboard.com:1883", "Subscriber_Pallak");
		
		//get the client reference of MqttClientConnector
		MqttClient client = this.mqttClientConnector.getClient();
		
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create actuatorData
		ActuatorData actuatorData = new ActuatorData();
		
		//add some command
		actuatorData.setCommand("INCREASE");
		
		//check whether the client can publish when connected
		assertEquals(this.mqttClientConnector.publishActuatorData(actuatorData, 2), true);
		
		//connect with invalid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboardd.com:1883", "Subscriber_Pallak");
				
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//check whether the client can publish when not connected
		assertEquals(this.mqttClientConnector.publishActuatorData(actuatorData, 2), false);
		
	}
	
	/**
	 * This method tests the publishSensorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 */
	@Test
	public void testPublishSensorData()
	{
		//connect with valid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboard.com:1883", "Subscriber_Pallak");
		
		//get the client reference of MqttClientConnector
		MqttClient client = this.mqttClientConnector.getClient();
		
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//create sensorData
		SensorData sensorData = new SensorData();
		
		//add some value
		sensorData.addValue(9);
		
		//check whether the client can publish when connected
		assertEquals(this.mqttClientConnector.publishSensorData(sensorData, 2), true);
		
		//connect with invalid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboardd.com:1883", "Subscriber_Pallak");
				
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//check whether the client can publish when not connected
		assertEquals(this.mqttClientConnector.publishSensorData(sensorData, 2), false);
		
	}
	
	/**
	 * This method tests the subscribeToSensorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 */
	@Test
	public void testSubscribeToSensorData()
	{
		//connect with valid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboard.com:1883", "Subscriber_Pallak");
		
		//get the client reference of MqttClientConnector
		MqttClient client = this.mqttClientConnector.getClient();
		
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check whether the client can subscribe when connected
		assertEquals(this.mqttClientConnector.subscribeToSensorData(2), true);
		
		//connect with invalid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboardd.com:1883", "Subscriber_Pallak");
				
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//check whether the client can publish when not connected
		assertEquals(this.mqttClientConnector.subscribeToSensorData(2), false);
		
	}	
	
	/**
	 * This method tests the subscribeToActuatorData() method of the MqttClientConnector class
	 * It checks whether the program breaks if the client is not connected to MQTT host
	 */
	@Test
	public void testSubscribeToActuatorData()
	{
		//connect with valid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboard.com:1883", "Subscriber_Pallak");
		
		//get the client reference of MqttClientConnector
		MqttClient client = this.mqttClientConnector.getClient();
		
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//check whether the client can subscribe when connected
		assertEquals(this.mqttClientConnector.subscribeToActuatorData(2), true);
		
		//connect with invalid values of host and port
		this.mqttClientConnector.connect("tcp://broker.mqttdashboardd.com:1883", "Subscriber_Pallak");
				
		//sleep for one second to let it connect
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//check whether the client can publish when not connected
		assertEquals(this.mqttClientConnector.subscribeToActuatorData(2), false);
		
	}	
	
}
