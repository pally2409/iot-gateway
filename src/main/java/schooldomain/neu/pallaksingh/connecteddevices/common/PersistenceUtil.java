// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.common;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;
import java.util.logging.Logger;

public class PersistenceUtil {
	
	//insantiate DataUtil
	DataUtil dataUtil = new DataUtil();
	
	//instantiate redis client for both sensor and actuator
	public Jedis jedis_sensor;
	Jedis jedis_actuator;
	
	//instantiate listeners
	SensorDataListener sensorDataListener;
	ActuatorDataListener actuatorDataListener;
	
	//get the logger for the class
	private final static Logger LOGGER = Logger.getLogger(SensorDataListener.class.getName());

	public PersistenceUtil() {
		super();
		
		//try to connect to the redis server
		try {
			
			//initialize redis for reading and writing sensor and actuator data
			this.jedis_actuator = new Jedis(new HostAndPort("pallypi.lan", 6379));
			this.jedis_sensor = new Jedis(new HostAndPort("pallypi.lan", 6379));
			
			//select databases for corresponding data
			jedis_sensor.select(0);
			jedis_actuator.select(1);
			
			
		//if error occured
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public PersistenceUtil(ActuatorDataListener actuatorDataListener) {
		super();
		
		//try to connect to the redis server
		try {
			
			//initialize redis for reading and writing sensor and actuator data
			this.jedis_actuator = new Jedis(new HostAndPort("pallypi.lan", 6379));
			this.jedis_sensor = new Jedis(new HostAndPort("pallypi.lan", 6379));
			
			//select databases for corresponding data
			jedis_sensor.select(0);
			jedis_actuator.select(1);
			
			//instantiate the sensorDataListener
			this.sensorDataListener = new SensorDataListener();
			this.actuatorDataListener = actuatorDataListener;	
			
			
		//if error occurred
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public PersistenceUtil(SensorDataListener sensorDataListener) {
		super();
		
		//try to connect to the redis server
		try {
			
			//initialize redis for reading and writing sensor and actuator data
			this.jedis_actuator = new Jedis(new HostAndPort("pallypi.lan", 6379));
			this.jedis_sensor = new Jedis(new HostAndPort("pallypi.lan", 6379));
			
			//select databases for corresponding data
			jedis_sensor.select(0);
			jedis_actuator.select(1);
			
			//instantiate the sensorDataListener
			this.sensorDataListener = sensorDataListener;
			this.actuatorDataListener = new ActuatorDataListener();	
			
			
		//if error occurred
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	//register actuator data DBMS listener
    public Thread registerActuatorDataDbmsListener() {
    	
    	//check if it has been instantiated
    	if(this.actuatorDataListener == null) {
    		
    		//if not, instantiate
    		this.actuatorDataListener = new ActuatorDataListener();
    	}
    	
    	//create a new thread for listening for sensorData
    	Thread actuatorListenerThread = new Thread(new Runnable() {
    		
    		//run method for the thread
            public void run() {
            	
            	//subscribe for keyspace notifications for db 0
                jedis_actuator.psubscribe(actuatorDataListener, "__keyspace@1__:*");
            }
        });
    	
    	//return the thread
    	return actuatorListenerThread;
    }
        
    
    //register sensor data DBMS listener
    public Thread registerSensorDataDbmsListener() {
    	
    	//check if it has been instantiated
    	if(this.sensorDataListener == null) {
    		
    		//if not, instantiate
    		this.sensorDataListener = new SensorDataListener();
    	}

    	//create a new thread for listening for sensorData
    	Thread sensorListenerThread = new Thread(new Runnable() {
    		
    		//run method for the thread
            public void run() {
            	
            	//subscribe for keyspace notifications for db 0
                jedis_sensor.psubscribe(sensorDataListener, "__keyspace@0__:*");
            }
        });
    	
    	//return the thread
    	return sensorListenerThread;
    	
   }
        
    //write actuator data to DBMS 
    public boolean writeActuatorDataToDbms(ActuatorData actuatorData) {
    	
    	
    	//try write and send to redis
    	try {
    		
			//convert the actuarData reference to JSON string using DataUtil
			String jsonStr = this.dataUtil.toJsonFromActuatorData(actuatorData);
			
			//create a unique key by combining device name and uuid
			UUID uuid = UUID.randomUUID();
			String keyUUIDString = uuid.toString();
			
			//add the key-value to redis
			this.jedis_actuator.set("actuatorData" + keyUUIDString , jsonStr);
			
		//if error occured	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			//return false
			return false;
		}
    	
    	//if ran successfully  
    	return true;
    	
    } 
    
    //write sensor data to DBMS 
    public boolean writeSensorDataToDbms(SensorData sensorData) {
    	
    	//try write and send to redis
    	try {
    		
			//convert the actuarData reference to JSON string using DataUtil
			String jsonStr = this.dataUtil.toJsonFromSensorData(sensorData);
			
			//create a unique key by combining device name and uuid
			UUID uuid = UUID.randomUUID();
			String keyUUIDString = uuid.toString();
			
			//add the key-value to redis
			this.jedis_sensor.set("sensorData" + keyUUIDString , jsonStr);
			
		//if error occured	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			//return false
			return false;
		} 
    	
    	//if ran successfully  
    	return true;
    	
    }
        

}
