package schooldomain.neu.pallaksingh.connecteddevices.labs.module02;

public class TempEmulatorAdaptor extends Thread {
	
	//initialize the emulatorTask
	TempSensorEmulatorTask tempSensorEmulatorTask = new TempSensorEmulatorTask();
	
	//variable that denotes whether the adaptor ran the task successfully
	boolean success;
	
	//getter for tempSensorEmulatorTask
	public TempSensorEmulatorTask getTempSensorEmulatorTask() {
		
		//return reference to the tempSensorEmulatorTask
		return tempSensorEmulatorTask;
	}
	

	//getter for the success of the run function of the adaptor
	public boolean isSuccess() {
		return success;
	}
	
	
	//run method that is run when the thread is started
	public void run() {
		
		//get the success of the task
		this.success = tempSensorEmulatorTask.generateRandomTemperature();
		
	}
	

}
