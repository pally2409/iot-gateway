// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.common;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import schooldomain.neu.pallaksingh.connecteddevices.labs.module01.SystemPerformanceAdaptor;

public class SensorDataListener extends JedisPubSub {
	
	//instantiate redis client for reading data on the listener
	Jedis r_sensor = new Jedis(new HostAndPort("pallypi.lan", 6379));
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(SensorDataListener.class.getName());
	
	//instantiate ActuatorData
	ActuatorData actuatorData = new ActuatorData();
	
	public SensorDataListener() {
		super();
		// TODO Auto-generated constructor stub
		
		//set level to info
		LOGGER.setLevel(Level.INFO);
		

	}
	
	//fucntion to run when subscribed
	@Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        
		//to run when subscribed
    }

	//callback function for SensorData
	@Override
	public void onPMessage(String pattern, String channel, String message) {
        
		//instantiate DataUtil and Config Util
		DataUtil dUtil = new DataUtil();
		ConfigUtil cUtil = new ConfigUtil();
		
		//get the value stored at the key present at the channel section of the keyspace notification
		String s = this.r_sensor.get(channel.split(":",-1)[1]);
        
		//instantiate sensorData
		SensorData sensorData = new SensorData();
		
		//get the sensorData object from the JSON string
        sensorData = dUtil.toSensorDataFromJson(s);
        
        //log the readings
        String loggerText = "---------------------------------------------- \n New Sensor Readings";
        LOGGER.info(loggerText);
        
        try {
        	
        	//write to local filesystem
			dUtil.writeSensorDataToFile(sensorData);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //set the name
        this.actuatorData.setName("Temperature Actuator");
        
        //if the sensor is a Temperature Sensor
        if(sensorData.getName().contentEquals("Temperature Sensor")) {
        	
        	//get the nominal temp from the ConfigUtil
        	int nominalTemp = cUtil.getIntegerValue("device", "nominalTemp");
        	
        	//if the current temperature is less than the nominal temperature
        	if(sensorData.getCurrentValue() < nominalTemp) {
        		
        		//decrease the temp
        		this.actuatorData.setCommand("INCREASE TEMP");
        		this.actuatorData.setValue("DOWN");
        		
        		System.out.println(this.actuatorData.getCommand());
        		
        	} else if(sensorData.getCurrentValue() > nominalTemp) {

        		//increase the temp
        		this.actuatorData.setCommand("DECREASE TEMP");
        		this.actuatorData.setValue("UP");
        	}
        	
        	//instantiate PersistenceUtil 
        	PersistenceUtil pUtil = new PersistenceUtil();
        	
        	//write the actuator data to redis via PersistenceUtil
        	pUtil.writeActuatorDataToDbms(this.actuatorData);
        	
        }
        
      
        
        
    }

}
