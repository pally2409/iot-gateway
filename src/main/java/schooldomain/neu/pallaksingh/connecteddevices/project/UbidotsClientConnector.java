package schooldomain.neu.pallaksingh.connecteddevices.project;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.labbenchstudios.iot.common.CertManagementUtil;
import com.ubidots.*;
import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;

/**
 * UbidotsClientConnector class provides abstraction for all Ubidots related tasks such as updating value of variables via
 * the Ubidots API 
 */
public class UbidotsClientConnector {
		
	//Declare ConfigUtil to take care of API credentials
	ConfigUtil cUtil;
	
	//Declare the API client that will communicate with the API for publishing humidity sensor readings
	ApiClient api;
	
	//Declare the data source for the constrained device
	DataSource dataSource;
	
	//Declare the variable IDs for the IMU sensors and general sensors
	String CONSTRAINED_MEM_ID; 		//Ubidots label ID for constrained device memory utilization variable
	String CONSTRAINED_CPU_ID; 		//Ubidots label ID for constrained device CPU utilization variable
	String GATEWAY_MEM_ID; 			//Ubidots label ID for gateway device memory utilization variable
	String GATEWAY_CPU_ID;			//Ubidots label ID for gateway device CPU utilization variable
	String EMERGENCY_BUTTON_ID;		//Ubidots label ID for emergency button variable
	String GYROSCOPE_X_ID;			//Ubidots label ID for gyroscope x attribute variable
	String GYROSCOPE_Y_ID;			//Ubidots label ID for gyroscope y attribute variable
	String GYROSCOPE_Z_ID;			//Ubidots label ID for gyroscope z attribute variable
	String ACCEL_X_ID;				//Ubidots label ID for accelerometer x attribute variable
	String ACCEL_Y_ID;				//Ubidots label ID for accelerometer y attribute
	String ACCEL_Z_ID;				//Ubidots label ID for accelerometer z attribute
	
	//Declare MqttClientConnector for publishing temperature sensor readings and register a listener for actuator value changes
	MqttClientConnector mqttClientConnector;
	
	//Declare a String for MqttTopic for ubidots
	String MQTT_TOPIC;
	
	//Declare DataUtil for conversions to and from JSON string
	DataUtil dUtil;
	
	
	//Boolean value to indicate whether using HTTPs (Ubidots API) for publishing sensor data
	boolean useUbidotsAPI = false;
	
	//Get the logger for the class
		private final static Logger LOGGER = Logger.getLogger(UbidotsClientConnector.class.getName());
	/*
	 * Constructor used to instantiate the classes used for this class and also set the variables for Mqtt connection with TLS support
	 */
	public UbidotsClientConnector(ConfigUtil confUtil, DataUtil dUtil) {
		super();
		
		CertManagementUtil certManagementUtil = CertManagementUtil.getInstance();
		
		//Initialize ConfigUtil to take care of API credentials and Ubidots device parameters
		cUtil 							= confUtil;
		
		//Initialize dUtil for conversions to and from JSON string
		this.dUtil						= dUtil;
		
		//Instantiate the API client using the user specific API key
		api								= new ApiClient(this.cUtil.getValue("ubidots.cloud", "apiKey"));
	
		//Instantiate MqttClientConnector for publishing temperature sensor readings and register a listener for actuator value changes
		mqttClientConnector 			= new MqttClientConnector("ssl://" + this.cUtil.getValue("ubidots.cloud", "host") + ":" + "8883", dUtil);
		
		//Get the data source for the constrained device
		dataSource = api.getDataSource("iot-device");
		
		//Define the variable IDs for the TempSensor and TempActuator
		CONSTRAINED_MEM_ID 				= confUtil.getValue("ubidots.cloud", "constrainedMemID"); 					//Ubidots label ID for constrained device memory utilization variable
		CONSTRAINED_CPU_ID 				= confUtil.getValue("ubidots.cloud", "constrainedCpuID");					//Ubidots label ID for constrained device CPU utilization variable
		GATEWAY_MEM_ID     				= confUtil.getValue("ubidots.cloud", "gatewayMemID");						//Ubidots label ID for gateway device memory utilization variable
		GATEWAY_CPU_ID	   				= confUtil.getValue("ubidots.cloud", "gatewayCpuID");							//Ubidots label ID for gateway device CPU utilization variable
		EMERGENCY_BUTTON_ID				= confUtil.getValue("ubidots.cloud", "emergencyButtonID");					//Ubidots label ID for emergency button variable
		GYROSCOPE_X_ID     				= confUtil.getValue("ubidots.cloud", "gyroscopeXID");							//Ubidots label ID for gyroscope x attribute variable
		GYROSCOPE_Y_ID     				= confUtil.getValue("ubidots.cloud", "gyroscopeYID");							//Ubidots label ID for gyroscope y attribute variable
		GYROSCOPE_Z_ID     				= confUtil.getValue("ubidots.cloud", "gyroscopeZID");			//Ubidots label ID for gyroscope z attribute variable
		ACCEL_X_ID         				= confUtil.getValue("ubidots.cloud", "accelerometerXID");			//Ubidots label ID for accelerometer x attribute variable
		ACCEL_Y_ID         				= confUtil.getValue("ubidots.cloud", "accelerometerYID");				//Ubidots label ID for accelerometer y attribute
		ACCEL_Z_ID         				= confUtil.getValue("ubidots.cloud", "accelerometerZID");			//Ubidots label ID for accelerometer z attribute
		
		//Initialize the MQTT topic for Ubidots  
		MQTT_TOPIC 				= this.cUtil.getValue("ubidots.cloud", "devicePath");
		
		//Set the socket factory obtained from the SSL certificate
		mqttClientConnector.conOpt.setSocketFactory(certManagementUtil.loadCertificate(this.cUtil.getValue("ubidots.cloud", "certFile")));
		
		//Set the user name token for ubidots
		mqttClientConnector.conOpt.setUserName(this.cUtil.getValue("ubidots.cloud", "userNameToken"));
	}
	
	/**
	 * Getter for the ConfigUtil instance
	 * @return		The current ConfigUtil instance for the class
	 */
	public ConfigUtil getcUtil() {
		
		//Return the current ConfigUtil instance
		return cUtil;
	}

	/**
	 * Method to publish incoming SensorData to Ubidots cloud
	 * 
	 * @param sensorData
	 * 				The SensorData object to be published
	 * @param qos				
	 * 				The Quality of Service level we want for publishing
	 * 
	 * @returns A boolean value to indicate success in publishing
	 */
	public boolean publishSensorData(SensorData sensorData, int qos) {
		
		//Declare the variableID;
		String variableParam = "";
		
		//Check for the sensor type
		//If constrained device memory utilization
		if(sensorData.getName().contentEquals("Constrained Mem")) {
			
			//Set the variable ID appropriately
			variableParam = this.CONSTRAINED_MEM_ID;	
		}

		//If constrained device CPU utilization
		if(sensorData.getName().contentEquals("Constrained CPU")) {
			
			//Set the variable ID appropriately
			variableParam = this.CONSTRAINED_CPU_ID;
			
		}
		
		//If gateway device memory utilization
		if(sensorData.getName().contentEquals("Gateway Mem")) {
			
			//Set the variable ID appropriately
			variableParam = this.GATEWAY_MEM_ID;
		}
		
		//If gateway device cpu utilization
		if(sensorData.getName().contentEquals("Gateway CPU")) {
			
			//Set the variable ID appropriately
			variableParam = this.GATEWAY_CPU_ID;
		}
		
		//If emergency button
		if(sensorData.getName().contentEquals("Emergency")) {
			
			//Set the variable ID appropriately
			variableParam = this.EMERGENCY_BUTTON_ID;
		}
			
		//Try to send the sensor data using the Ubidots API
		try {
				
			//Get the variable from ubidots according to the sensor
			Variable tempVariable = this.api.getVariable(variableParam);
					
			//Publish to the variable
			tempVariable.saveValue(sensorData.getCurrentValue());		
		}
			
		//If an error occurs
		catch(Exception e) {
				
			//Print the error
			e.printStackTrace();
				
				//Log a message
				LOGGER.info("Could not publish SensorData using API to ubidots");
				
				//return false to indicate error
				return false;
			}
			
			//If successful in publishing
			//Log a message
			LOGGER.info("Published SensorData using API to ubidots");
			
			//If succeeded without error
			return true;
	}
	
	/**
	 * Method to publish incoming SensorData to Ubidots cloud
	 * 
	 * @param imuSensorData		
	 * 				The IMUSensorData object to be published
	 * @param qos				
	 * 				The Quality of Service level we want for publishing
	 * 
	 * @returns A boolean value to indicate success in publishing
	 */
	public boolean publishIMUSensorData(IMUSensorData imuSensorData, int qos) {
		
		//Declare the variableID list containing X, Y and Z variable IDs for the IMU sensor
		String[] variableParam = new String[3];
		variableParam[0] = "";
		variableParam[1] = "";
		variableParam[2] = "";
		
		//Check the sensor
		//If gyroscope
		if(imuSensorData.getName().contentEquals("Gyroscope")) {
			//Set the variable ID appropriately 
			variableParam[0] = this.GYROSCOPE_X_ID;
			variableParam[1] = this.GYROSCOPE_Y_ID;
			variableParam[2] = this.GYROSCOPE_Z_ID;

		}

		if(imuSensorData.getName().contentEquals("Accelerometer")) {
			//Set the variable ID to the humidity sensor ID
			variableParam[0] = this.ACCEL_X_ID;
			variableParam[1] = this.ACCEL_Y_ID;
			variableParam[2] = this.ACCEL_Z_ID;
		}	
			
		//Try to send the IMUSensorData via the Ubidots API
		try {
				
			//Get the X variable from Ubidots according to the sensor
			Variable variable = this.api.getVariable(variableParam[0]);	
			
			//Publish X value to ubidots
			variable.saveValue(imuSensorData.getCurrentValue().get("x"));
			
			//Get the Y variable from Ubidots according to the sensor
			variable = this.api.getVariable(variableParam[1]);
			
			//Publish Y value to ubidots
			variable.saveValue(imuSensorData.getCurrentValue().get("y"));
			
			//Get the Z variable from Ubidots according to the sensor
			variable = this.api.getVariable(variableParam[2]);
			
			//Publish Z value to ubidots
			variable.saveValue(imuSensorData.getCurrentValue().get("z"));
		}
			
		//If an error occurs
		catch(Exception e) {
				
			//Print the error
			e.printStackTrace();
				
				//Log a message
				LOGGER.info("Could not publish SensorData using API to ubidots");
				
				//return false to indicate error
				return false;
		}
			
			//Log a message
			LOGGER.info("Published SensorData using API to ubidots");
			
			//If succeeded without error
			return true;
	}
}