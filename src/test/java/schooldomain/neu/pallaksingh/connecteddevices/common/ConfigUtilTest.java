/**
 * 
 */

package schooldomain.neu.pallaksingh.connecteddevices.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemPerformanceAdaptor;

/**
 * Test class for ConfigUtil functionality.
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
public class ConfigUtilTest
{
	// static
	
	//sample directory and file while running test in pipeline
	public static final String DIR_PREFIX = "./sample/";
	
	public static final String TEST_VALID_CFG_FILE   = DIR_PREFIX + "ConnectedDevicesConfig_NO_EDIT_TEMPLATE_ONLY.props";
	
	//declare the configUtil 
	ConfigUtil configUtil;
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(ConfigUtilTest.class.getName());
	
	// setup methods
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		LOGGER.setLevel(Level.INFO);
		
		//instantiate the configUtil
		configUtil = new ConfigUtil();
		
		//if the default config file is not loaded: while testing the pipeline on the cloud
		if(configUtil.configFileLoaded != true) {
			
			//load the sample config directory
			configUtil.loadConfig(TEST_VALID_CFG_FILE);
		}
	}
	
	// test methods
	
	/**
	 * This method checks the getBooleanValue() function of ConfigUtil class. It checks for valid boolean values, checks
	 * for when an invalid key is sent as a parameter and when an invalid section is sent as a parameter
	 */
	@Test
	public void testGetBooleanProperty()
	{
		//log the test
		LOGGER.info("Testing testGetBooleanProperty()");
		
		//check boolean value for ubidots.cloud section's useWebAccess key which is correctly stored as a boolean property
		Boolean checkVal = configUtil.getBooleanValue("ubidots.cloud", "useWebAccess");
		Boolean ret = true;
		assertEquals(ret, checkVal);
		
		//check boolean value for ubidots.cloud section's host key which is invalid boolean
		checkVal = configUtil.getBooleanValue("ubidots.cloud", "host");
		ret = null;
		assertEquals(ret, checkVal);
		
		//check boolean value for ubidots.cloud section's 'hostt' key which is an error in typing and should return a null
		checkVal = configUtil.getBooleanValue("ubidots.cloud", "hostt");
		assertEquals(ret, checkVal);
	
		//check boolean value for ubidots.cloudd section which is an error in typing and should return a null
		checkVal = configUtil.getBooleanValue("ubidots.cloudd", "host");
		assertEquals(ret, checkVal);		
		
	}
	
	/**
	 * This method checks the getIntegerValue() function of ConfigUtil class. It checks for valid integer values, checks
	 * for when an invalid key is sent as a parameter and when an invalid section is sent as a parameter
	 */
	@Test
	public void testGetIntegerProperty()
	{
		//log the test
		LOGGER.info("Testing testGetIntegerProperty()");
		
		//check integer value for smtp.cloud section's port key which is correctly stored as an integer property
		Integer checkVal = configUtil.getIntegerValue("smtp.cloud", "port");
		Integer ret = 465;
		assertEquals(ret, checkVal);
		
		//check integer value for ubidots.cloud section's host key which is not an integer and should return a null
		checkVal = configUtil.getIntegerValue("ubidots.cloud", "host");
		ret = null;
		assertEquals(ret, checkVal);
		
		//check integer value for ubidots.cloud section's 'hostt' key which is an error in typing and should return a null
		checkVal = configUtil.getIntegerValue("ubidots.cloud", "hostt");
		assertEquals(ret, checkVal);
	
		//check integer value for ubidots.cloudd section which is an error in typing and should return a null
		checkVal = configUtil.getIntegerValue("ubidots.cloudd", "host");
		assertEquals(ret, checkVal);
	}
	
	/**
	 * This method checks the getValue() function of ConfigUtil class. It checks valid values, checks
	 * for when an invalid key is sent as a parameter and when an invalid section is sent as a parameter
	 */
	@Test
	public void testGetProperty()
	{
		//log the test
		LOGGER.info("Testing testGetProperty()");
		
		//check value for smtp.cloud section's port key which is correctly stored
		String checkVal = configUtil.getValue("smtp.cloud", "host");
		assertTrue("Sent a valid key-value pair, didn't receive expected value", checkVal.equals("smtp.gmail.com"));
		
		//check value for ubidots.cloud section's 'hostt' key which is an error in typing and should return a null string
		checkVal = configUtil.getValue("smtp.cloud", "hostt");
		String nullString = "null";
		assertEquals("Sent an invalid key, should return null", nullString, checkVal);
		
		//check value for ubidots.cloudd section which is an error in typing and should return a false
		checkVal = configUtil.getValue("smtp.cloudd", "host");
		assertTrue("Sent an invalid key, should return null", nullString == checkVal);
				
	}
	
	/**
	 * This method checks the getIntegerValue(), getValue(), getBooleanValue()function of ConfigUtil class. It checks for valid
	 * keys, otherwise returns a null
	 */
	@Test
	public void testHasProperty()
	{
		//log the test
		LOGGER.info("Testing testHasProperty()");
		
		//check value for smtp.cloud section's port key which is correctly stored
		String checkVal = configUtil.getValue("smtp.cloud", "host");
		assertTrue("Sent a valid key-value pair, didn't receive expected value", checkVal.equals("smtp.gmail.com"));
		
		//check value for ubidots.cloud section's 'hostess' key and should return a false because hostess key doesn't exist
		checkVal = configUtil.getValue("ubidots.cloud", "hostess");
		assertTrue("Invalid key", checkVal.equals("null"));

		//check integer value for smtp.cloud section's 'portt' key which is an error in typing and should return a null
		Integer checkIntegerVal = configUtil.getIntegerValue("smtp.cloud", "portt");
		Integer retInt = null;
		assertEquals(retInt, checkIntegerVal);
		
		//check boolean value for ubidots.cloud section's 'useWebAccessst' key which is an error in typing and should return a null
		Boolean checkBooleanVal = configUtil.getBooleanValue("ubidots.cloud", "useWebAccesss");
		Integer retBool = null;
		assertEquals(retBool, checkBooleanVal);
		
	}
	
	/**
	 * This method checks the getIntegerValue(), getValue(), getBooleanValue()function of ConfigUtil class. It checks for valid
	 * section, otherwise returns a null
	 */
	@Test
	public void testHasSection()
	{
		//log the test
		LOGGER.info("Testing testHasSection()");
		
		//check value for smtp.cloud section's port key which is correctly stored
		String checkVal = configUtil.getValue("smtp.cloud", "host");
		assertTrue("Sent a valid key-value pair, didn't receive expected value", checkVal.equals("smtp.gmail.com"));
		
		//check value for ubidots.cloudd section and should return a false because section doesn't exist
		checkVal = configUtil.getValue("smtp.cloud", "hostess");
		assertTrue("Invalid key", checkVal.equals("null"));

		//check integer value for smtp.cloud sectionn which is an error in typing and should return a null
		Integer checkIntegerVal = configUtil.getIntegerValue("smtp.cloudd", "portt");
		Integer retInt = null;
		assertEquals(retInt, checkIntegerVal);
		
		//check boolean value for ubidots.cloud sectionn which is an error in typing and should return a null
		Boolean checkBooleanVal = configUtil.getBooleanValue("ubidots.cloudd", "useWebAccesss");
		Integer retBool = null;
		assertEquals(retBool, checkBooleanVal);
	}
	
	/**
	 * This method checks whether the configData is correctly loaded when the file is valid and for error when file is not valid
	 */
	@Test
	public void testIsConfigDataLoaded()
	{
		//log the test
		LOGGER.info("Testing testIsConfigDataLoaded()");
		
		//when config file is correctly loaded, the configFileLoaded should be true
		assertEquals(true, this.configUtil.configFileLoaded);
		
		//set a gibberish value as the config file, it should be false as the file does not exist
		this.configUtil.loadConfig("/config/blah.props");
		assertEquals(false, this.configUtil.configFileLoaded);
		
	}
	
}
