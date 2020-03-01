// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module06;

/*
 * This class calls the MQTTClientConnector to subscribe for 
 * new sensor data readings from IoT-Device
 */
public class GatewayHandlerApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		/*
//		 * MODULE 6
//		 */
//		
		//Instantiate MQTTClientConnector
		MqttClientConnector mqttClientConnector = new MqttClientConnector();
		
		//Connect the MQTT client with given host and client ID
		mqttClientConnector.connect("tcp://broker.mqttdashboard.com:1883", "Subscriber_Pallak");
		
		//Subscribe to the SensorData topic with QoS level 1
		mqttClientConnector.subscribeToSensorData(1);
	}
}