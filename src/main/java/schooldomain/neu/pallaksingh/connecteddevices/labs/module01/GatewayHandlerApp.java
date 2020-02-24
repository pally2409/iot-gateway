package schooldomain.neu.pallaksingh.connecteddevices.labs.module01;
// import libraries
import com.labbenchstudios.iot.common.BaseDeviceApp;
import com.labbenchstudios.iot.common.DeviceApplicationException;

import schooldomain.neu.pallaksingh.connecteddevices.common.ActuatorData;
import schooldomain.neu.pallaksingh.connecteddevices.common.ConfigUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.DataUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.PersistenceUtil;
import schooldomain.neu.pallaksingh.connecteddevices.common.SensorData;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module02.TempEmulatorAdaptor;

public class GatewayHandlerApp extends BaseDeviceApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		/*
//		 * MODULE 1
//		 */
//		
//		// instantiate system performance adaptor
//		SystemPerformanceAdaptor systemPerformanceAdaptor = new SystemPerformanceAdaptor(15);
//		
//		// set the system performance adaptor to true
//		systemPerformanceAdaptor.enableAdaptor = false;
//		
//		// start the thread
//		//systemPerformanceAdaptor.start();
//		
//		/*
//		 * MODULE 2
//		 */
//		
//		//instantiate the temperature emulator adaptor
//		TempEmulatorAdaptor tempEmulatorAdaptor = new TempEmulatorAdaptor();
//		
//		//set the system performance adaptor to true
//		tempEmulatorAdaptor.getTempSensorEmulatorTask().setEnableEmulator(true);
//		
//		tempEmulatorAdaptor.start();
		
//		DataUtil dUtil  = new DataUtil();
//		SensorData sData = new SensorData();
//		sData.addValue(10);
//		String str1 = dUtil.toJsonFromSensorData(sData);
//		System.out.println(str1);
//		SensorData mData = dUtil.toSensorDataFromJson(str1);
//		System.out.println(mData.toString());
//		
//		ActuatorData aData = new ActuatorData();
//		aData.setName("Hello");
//		aData.setCommand("Its me");
//		aData.setValue("Increase me");
//		String str2 = dUtil.toJsonFromActuatorData(aData);
//		System.out.println(str2);
//		ActuatorData kData = dUtil.toActuatorDataFromJson(str2);
//		System.out.println(kData.toString());
		
		PersistenceUtil pUtil = new PersistenceUtil();
		pUtil.registerSensorDataDbmsListener();
		
		
				
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
