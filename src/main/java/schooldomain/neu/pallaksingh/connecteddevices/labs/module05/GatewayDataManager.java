// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module05;
import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorDataListener;
import schooldomain.neu.pallaksingh.connecteddevices.common.PersistenceUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorDataListener;

public class GatewayDataManager {
	
	//instantiate SensorDataListener
	public SensorDataListener sensorDataListener = new SensorDataListener();
	
	//instantiate PersistenceUtil
	public PersistenceUtil pUtil = new PersistenceUtil(sensorDataListener);
	
	//this method starts the listener to listen for SensorData
	public boolean start() {
		
		//try to run the thread
		try {
			
			//pass the actuatorDataListener reference to the PersistanceUtil manager to register for listening
			Thread sensorDataListenerThread = pUtil.registerSensorDataDbmsListener();
			
			//start the thread
			sensorDataListenerThread.start();
		
			//if error occured 
		} catch(Exception e) {
			
			e.printStackTrace();
			
			//return False
			return false; 
		}
		
		//if runs successfully
		return true;
	}

}
