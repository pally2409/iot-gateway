package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;

// import libraries
import com.labbenchstudios.iot.common.BaseDeviceApp;
import com.labbenchstudios.iot.common.DeviceApplicationException;

import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module02.TempEmulatorAdaptor;

public class GatewayHandlerApp extends BaseDeviceApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		 * MODULE 1
		 */
		
		// instantiate system performance adaptor
		SystemPerformanceAdaptor systemPerformanceAdaptor = new SystemPerformanceAdaptor(15);
		
		// set the system performance adaptor to true
		systemPerformanceAdaptor.enableAdaptor = false;
		
		// start the thread
		//systemPerformanceAdaptor.start();
		
		/*
		 * MODULE 2
		 */
		
		//instantiate the temperature emulator adaptor
		TempEmulatorAdaptor tempEmulatorAdaptor = new TempEmulatorAdaptor();
		
		//set the system performance adaptor to true
		tempEmulatorAdaptor.getTempSensorEmulatorTask().setEnableEmulator(true);
		
		//start the thread
		tempEmulatorAdaptor.start();
		
		
				
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
