package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

// import libraries
import com.labbenchstudios.iot.common.BaseDeviceApp;
import com.labbenchstudios.iot.common.DeviceApplicationException;

public class GatewayHandlerApp extends BaseDeviceApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// instantiate system performance adaptor
		SystemPerformanceAdaptor systemPerformanceAdaptor = new SystemPerformanceAdaptor(15);
		
		// set the system performance adaptor to true
		systemPerformanceAdaptor.enableAdaptor = true;
		
		// start the thread
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
