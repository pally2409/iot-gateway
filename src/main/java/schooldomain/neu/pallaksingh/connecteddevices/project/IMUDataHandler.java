package schooldomain.neu.pallaksingh.connecteddevices.project;

/**
 * Class for handling IMU Data received from Constrained Device. It is used to convert
 * accelerometer X, Y and Z values to calculate roll, pitch and yaw and detect a fall based
 * on the algorithm that uses roll, pitch and yaw
 * 
 * @author pallaksingh
 *
 */
public class IMUDataHandler {
	
	//Declare a static final immutable value of Pi
	static final double PI = 3.14159265358979323846;
	
	//Declare a variable for holding previous value of yaw used for comparison of movement
	static double yawOld;
	
	//Declare a counter to hold the number of falls encountered so as to keep control of false positives
	static int counter = 0;
	
	/**
	 * This method uses x, y and z attributes of accelerometer data to find the roll, pitch
	 * and yaw of the device
	 * 
	 * @param x
	 * 			The x attribute of accelerometer data
	 * @param y
	 * 			The y attribute of accelerometer data
	 * @param z
	 * 			The z attribute of accelerometer data
	 * @return An array containing the value of roll, pitch and yaw
	 */
	public double[] convertToRPY(double x, double y, double z) {
		
		//Formula for pitch is	= 180 * arctan(x/sqrt(y^2 + z^2))/3.14
		double pitch 			= 180 * Math.atan (x/Math.sqrt((y*y) + (z*z)))/PI;
		
		//Formula for roll is 	= 180 * arctan(y/sqrt(x^2 + z^2))/3.14
		double roll 			= 180 * Math.atan (y/Math.sqrt(x*x + z*z))/PI;
		
		//Formula for yaw  is	= 180 * arctan(z/sqrt(x^2 + y^2))/3.14
		double yaw 				= 180 * Math.atan (z/Math.sqrt(x*x + z*z))/PI;
		
		//Create an array containing the roll, pitch and yaw
		double[] rph = {roll, pitch, yaw};
		
		//Return the array
		return rph;
	}
	
	/**
	 * This method uses x, y and z attributes to determine whether the current accelerometer data
	 * indicates a fall
	 * @param x
	 * 			The x attribute of accelerometer data
	 * @param y
	 * 			The y attribute of accelerometer data
	 * @param z
	 * 			The z attribute of accelerometer data
	 * @return A boolean value indicating whether the x, y and z values indicate a fall 
	 */
	public boolean checkFall(Double x, Double y, Double z) {
		
		//Get the roll, pitch and yaw values from the x, y and z values of accelerometer
		double[] rph = convertToRPY(x, y, z);
		
		//Compare against old yaw value to indicate a change against a threshold
		if(yawOld - rph[2] >= 5)  {
			
			//Compare against old roll and pitch value to indicate a change against a threshold
			if(rph[1] > 20 && rph[1] < 340 || rph[0] > 20 && rph[1] < 340) {
				
				//Increment the counter for number of positives for a fall
				counter += 1;
				
				//Store the current value of yaw to act as the old value of yaw
				yawOld = rph[2];
				
				//Return a true to indicate a fall
				return true;
			}	
		}
		
		//Store the current value of yaw to act as the old value of yaw
		yawOld = rph[2];
		
		//Return a false to indicate no fall detected
		return false;
	}
}
