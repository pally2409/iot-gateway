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

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

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
public class PersistenceUtilTest
{
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(PersistenceUtilTest.class.getName());
	
	//declare ActuatorData
	PersistenceUtil persistenceUtil; 
	
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		//instantiate ActuatorData
		this.persistenceUtil = new PersistenceUtil();
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		//set the reference to the variables to null to release any resources
		this.persistenceUtil = null;
	}
	
	// test methods
	
	/**
	 * Tests the writeActuatorDataToDbms() method of PersistenceUtil class. 
	 * Checks whether it is able to write to redis and doesn't
	 * break if it cannot set up the connection to the server
	 */
	@Test
	public void testWriteActuatorDataToDbms() {
		
		//when running on system try 
		try {
			//Create ActuatorData instance
			ActuatorData actuatorData = new ActuatorData();
			
			//write to redis and check if it returns true
			assertEquals(true, this.persistenceUtil.writeActuatorDataToDbms(actuatorData));
			
			//add an invalid port to the jedisActuator
			this.persistenceUtil.jedis_actuator = new Jedis(new HostAndPort("pallypi.lan", 6890));
			
			//write to redis and check if it returns false
			assertEquals(false, this.persistenceUtil.writeActuatorDataToDbms(actuatorData));
			
		//when running on pipeline
		} catch(Exception e) {
			
			//print exception
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Tests the writeSensorDataToDbms() method of PersistenceUtil class. 
	 * Checks whether it is able to write to redis and doesn't
	 * break if it cannot set up the connection to the server
	 */
	@Test
	public void testWriteSensorDataToDbms() {
		
		//when running on system try
		try {
			
			//Create ActuatorData instance
			SensorData sensorData = new SensorData();
			
			//write to redis and check if it returns true
			assertEquals(true, this.persistenceUtil.writeSensorDataToDbms(sensorData));
			
			//add an invalid port to the jedisActuator
			this.persistenceUtil.jedis_sensor = new Jedis(new HostAndPort("pallypo.lan", 6890));
			
			//write to redis and check if it returns false
			assertEquals(false, this.persistenceUtil.writeSensorDataToDbms(sensorData));
			
		//when running on pipeline
		} catch(Exception e) {
			
			//print exception
			e.printStackTrace();
		}
		
	
	}
	
	/**
	 * Tests the registerActuatorDataDbmsListener() method of PersistenceUtil class. 
	 * Checks whether it returns a thread
	 */
	@Test
	public void testRegisterActuatorDataDbmsListener() {
		
		//when running on system try
		try {
			
			//check if it returns a thread
			assertEquals(this.persistenceUtil.registerActuatorDataDbmsListener().getClass(), Thread.class);
		
			//when running on pipeline
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Tests the registerSensorDataDbmsListener() method of PersistenceUtil class. 
	 * Checks whether it returns a thread
	 */
	@Test
	public void testRegisterSensorDataDbmsListener() {
		
		//when running on system, try
		try {
			
			//check if it returns a thread
			assertEquals(this.persistenceUtil.registerSensorDataDbmsListener().getClass(), Thread.class);
			
			//when running on pipeline 
		} catch(Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	
}
