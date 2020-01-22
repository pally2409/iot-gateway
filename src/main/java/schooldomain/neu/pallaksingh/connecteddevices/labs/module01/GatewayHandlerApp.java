package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

import com.labbenchstudios.iot.common.BaseDeviceApp;
import com.labbenchstudios.iot.common.DeviceApplicationException;

public class GatewayHandlerApp extends BaseDeviceApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SystemPerformanceAdaptor systemPerformanceAdaptor = new SystemPerformanceAdaptor(5);
		systemPerformanceAdaptor.enableAdaptor = true;
		systemPerformanceAdaptor.start();
				
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
