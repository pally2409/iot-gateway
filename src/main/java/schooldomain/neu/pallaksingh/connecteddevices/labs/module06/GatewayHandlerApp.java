// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module06;
import com.labbenchstudios.iot.common.BaseDeviceApp;
import com.labbenchstudios.iot.common.DeviceApplicationException;


public class GatewayHandlerApp extends BaseDeviceApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		/*
//		 * MODULE 6
//		 */
//		
		//instantiate MQTTClientConnector
		MqttClientConnector mqttClientConnector = new MqttClientConnector();
		
		//connect the mqtt client to the broker
		mqttClientConnector.connect("tcp://broker.mqttdashboard.com:1883", "Subscriber_Pallak");
		
		//subscribe to the SensorData topic
		mqttClientConnector.subscribeToSensorData(1);
	}

	@Override
	protected void start() throws DeviceApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stop() throws DeviceApplicationException {
		// TODO Auto-generated method stub
		
	}

}
