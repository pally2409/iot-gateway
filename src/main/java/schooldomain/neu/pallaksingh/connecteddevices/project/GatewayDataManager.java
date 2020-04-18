//Import packages and modules
package schooldomain.neu.pallaksingh.connecteddevices.project;
import java.util.logging.Logger;
import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;

/**
 * Class responsible for starting all operations of the Fall Detection System project. These include 
 * listening for new sensor data from the constrained device, processing it and publishing it to Ubidots, 
 * triggering appropriate actuation and publishing it to the constrained device
 * 
 * @author pallaksingh
 */
public class GatewayDataManager {
	
	//Declare UbidotsClientConnector for sending IMUSensorData to Ubidots Cloud 
	static UbidotsClientConnector 				ubidotsClientConnectorSensorData;
	
	//Declare UbidotsClientConnector for sending Gateway and Constrained device system performance Data to Ubidots Cloud
	static UbidotsClientConnector 				ubidotsClientConnectorSystemPerformance;
	
	//Declare SystemPerformanceAdaptor to gather system performance data for the gateway device
	static SystemPerformanceAdaptor 			sysPerformanceAdaptor;
	
	//Declare SmtpClientConnector for sending mails using the SMTP protocol
	static SmtpClientConnector					smtpClientConnector;
	
	//Declare MQTTClientConnector for publishing data to communicate actuation to the constrained device
	static MqttClientConnector 					mqttClientConnector;
	
	//Declare CoapServerManager for listening for new SensorData and IMUSensorData from constrained device
	static CoapServerManager 					coapServerManager;
	
	//Declare DataUtil for handling communication to and from gateway device to Ubidots and Constrained device
	static DataUtil 							dUtil;
	
	//Declare ConfigUtil for reading configurations from the config file
	static ConfigUtil							confUtil;

	//Get the logger for the class
	private final static Logger LOGGER 			= Logger.getLogger(GatewayDataManager.class.getName());

	//Threads required for the two communication tasks
	Thread sensorListenerThread;			//Thread for listening to incoming SensorData from Constrained Device and publishing to Ubidots
	Thread ubiDataListenerThread;			//Thread for listening to incoming ActuatorData from Ubidots and publishing to Constrained Device
	Thread systemPerformanceDataThread;		//Thread for reporting gateway device system performance data to Ubidots
	
	/**
	 * The constructor instantiates the classes needed for operations of this class that were declared above
	 */
	public GatewayDataManager() {
		super();
		// TODO Auto-generated constructor stub
		//Instantiate ConfigUtil for reading configurations from the config file
		confUtil										= new ConfigUtil();
		
		//InstantiateSmtpClientConnector for sending mails using the SMTP protocol
		smtpClientConnector								= new SmtpClientConnector(confUtil);	
		
		//Instantiate UbidotsClientConnector for sending IMUSensorData to Ubidots Cloud 
		ubidotsClientConnectorSensorData 				= new UbidotsClientConnector(confUtil, dUtil);
		
		//Instantiate UbidotsClientConnector for sending Gateway and Constrained device system performance Data to Ubidots Cloud
		ubidotsClientConnectorSystemPerformance 		= new UbidotsClientConnector(confUtil, dUtil);
		
		//Instantiate MQTTClientConnector for publishing data to communicate actuation to the constrained device
		mqttClientConnector 							= new MqttClientConnector("tcp://broker.mqttdashboard.com:1883", dUtil);
		
		//Instantiate SystemPerformanceAdaptor to gather system performance data for the gateway device
		sysPerformanceAdaptor 							= new SystemPerformanceAdaptor(ubidotsClientConnectorSystemPerformance);
		
		//Instantiate DataUtil for handling communication to and from gateway device to Ubidots and Constrained device
		dUtil											= new DataUtil();
		
		//Instantiate CoapServerManager for listening for new SensorData and IMUSensorData from constrained device 
		coapServerManager								= new CoapServerManager();
		
		//Sleep for a certain amount of time to establish connection to hosts 
		try {
			
			//Sleep for 1 second
			Thread.sleep(1000);
			
		//If an error occured
		} catch (InterruptedException e) {
			
			//Report the error stack trace
			e.printStackTrace();
		}
		
		//Add SystemPerformanceResourceHandler to CoapServerManager to handle System performance data for Constrained device
		coapServerManager.addResource(new SystemPerformanceResourceHandler("sysPerf", dUtil, ubidotsClientConnectorSystemPerformance));
				
		//Add IMUSensorDataResourceHandler to CoapServerManager to handle IMUSensorData 
		coapServerManager.addResource(new IMUSensorDataResourceHandler("imuData", dUtil, ubidotsClientConnectorSensorData, mqttClientConnector, smtpClientConnector));
		
		//Add ActuatorResponseResourceHandler to CoapServerManager to handle a response to actuation on the constrained device
		coapServerManager.addResource(new ActuatorResponseResourceHandler("fallDetect", dUtil, ubidotsClientConnectorSensorData));	
	}
	
	/**
	 * Method to create and start threads for constrained device to gateway device communication and gateway device to Ubidots communication
	 */
	public void run() {
		
		//Instantiate the thread for listening to incoming SensorData from Constrained Device and publishing to Ubidots
		this.sensorListenerThread = new Thread(new Runnable() {

			/**
			 * The run method of the Runnable for the SensorListenerThread subscribes to SensorData 
			 */
			public void run() {
				
				//Log the message
				LOGGER.info("Initialized sensorListenerThread");
				
				//Subscribe to SensorData from constrained device using Mqtt
				coapServerManager.startServer();
			}
		});
		
		//Instantiate the thread for gathering and reporting the system performance data to Ubidots
		this.systemPerformanceDataThread = new Thread(new Runnable() {

			/**
			 * The run method of the Runnable for the SystemPerformanceDataThread enables the adaptor to
			 * fetch system performance data for the gateway device 
			 */
			public void run() {
				
				//Enable the adaptor for fetching the system performance data
				sysPerformanceAdaptor.enableAdaptor = true;
				
				//Run the adaptor 
				sysPerformanceAdaptor.run();	
			}	
		});
		
		//Start the threads
		sensorListenerThread.start();			//For listening to new SensorData and IMUSensorData
		systemPerformanceDataThread.start();	//For reporting gateway device system performance data
	}
}
