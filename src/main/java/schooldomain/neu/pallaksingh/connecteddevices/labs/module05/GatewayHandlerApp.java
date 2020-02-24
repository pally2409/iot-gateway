// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.labs.module05;
import com.labbenchstudios.iot.common.BaseDeviceApp;
import com.labbenchstudios.iot.common.DeviceApplicationException;


public class GatewayHandlerApp extends BaseDeviceApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		/*
//		 * MODULE 5
//		 */
//		

		//instantiate GatewayDataManager
		GatewayDataManager gatewayDataManager = new GatewayDataManager();
		
		//start the manager
		gatewayDataManager.start();
		
		
				
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
