package schooldomain.neu.pallaksingh.connecteddevices.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SensorData {
	
	//set the attributes for the sensor data
	float currentValue = 0;
	float average = 0;
	int totalCount = 0;
	float totalValue = 0;
	float maxValue = -99;
	float minValue = 99;
	String sensorName = "Not Set";
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
	LocalDateTime now = LocalDateTime.now();
	public String timestamp = dtf.format(now);
	

	//add value to the sensor data
	public void addValue(float val) {
		
		//if value passed is not null
		if(new Float(val).equals(null) != true) {
			
			//add to the aggregate value of the readings
			this.totalValue += val;
			
			//increment the count
			this.totalCount += 1;
			
			//change the current value to this new value
			this.currentValue = val;
			 
			//update the timestamp
			LocalDateTime now = LocalDateTime.now();
			this.timestamp = dtf.format(now);
			
			//if the value is the first one, or if greater than previous maxValue, update maxValue
			if((this.totalCount == 1) || (val > this.maxValue)) {
				this.maxValue = val;
			}
			
			//if the value is the first one, or if less than previous minValue, update minValue    
			if((this.totalCount == 1) || (val < this.minValue)) {
				this.minValue = val;
			}
		} 
	}
	
	//get the average value of all the sensorData readings till now
	public float getAverageValue() {
		
		//average = totalValue/Count
		return this.totalValue/this.totalCount;
	}
	
	//get total number of readings till now
	public int getCount() {
		return this.totalCount;
	}
	
	//get the current value
	public float getCurrentValue() {
		return this.currentValue;
	}
	
	//get the max value
	public float getMaxValue() {
		
		//if there are no readings till now, return bogus value
		if(this.getCount() == 0) {
			return -99;
		}
		 //return the maxValue if readings exist
		else {
			return this.maxValue;
		}
	}
	
	public float getMinValue() {
		
		//if there are no readings till now, return bogus value
		if(this.getCount() == 0) {
			return 99;
		} 
		
		//return the minValue if readings exist
		else {
			return this.minValue;
		}
	}
	
	//get the name of the sensor
	public String getName() {
		return sensorName;
	}
	
	//set the name of the sensor
	public void setName(String name) {
		
		//if name passed is not null
		if(name == null) {
			
			//if the name passed is null, simply set it to 'Not Set'
			this.sensorName = "Not Set";
		} 
		
		//if name is valid
		else {
			
			this.sensorName = name;
		}
		
	}
	
}
