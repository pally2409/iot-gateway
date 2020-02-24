// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module05;
import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorDataListener;
import schooldomain.neu.pallaksingh.connecteddevices.common.PersistenceUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorDataListener;

public class GatewayDataManager {
	
	//instantiate SensorDataListener
	SensorDataListener sensorDataListener = new SensorDataListener();
	
	//instantiate PersistenceUtil
	PersistenceUtil pUtil = new PersistenceUtil(sensorDataListener);
	
	//this method starts the listener to listen for SensorData
	public void start() {
		
		//pass the actuatorDataListener reference to the PersistanceUtil manager to register for listening
		Thread sensorDataListenerThread = pUtil.registerSensorDataDbmsListener();
		
		//start the thread
		sensorDataListenerThread.start();
	
		
	}

}
