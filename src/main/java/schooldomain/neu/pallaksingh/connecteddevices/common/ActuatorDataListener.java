// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.common;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class ActuatorDataListener extends JedisPubSub {
	
	//instantiate redis client for reading data on the listener
		Jedis r_actuator = new Jedis(new HostAndPort("172.20.10.11", 6379));
		
		public ActuatorDataListener() {
			super();
			// TODO Auto-generated constructor stub
			
			//set the client db to 1 where actuatorData is stored
			this.r_actuator.select(1);
		}
		
		@Override
	    public void onPSubscribe(String pattern, int subscribedChannels) {
	        
			//if subscribed
			System.out.println("Subscribed");
	    }

		//callback function for ActuatorData
		@Override
		public void onPMessage(String pattern, String channel, String message) {
	        
			//instantiate DataUtil 
			DataUtil dUtil = new DataUtil();
			
			//get the value stored at the key present at the channel section of the keyspace notification
			String s = this.r_actuator.get(channel.split(":",-1)[1]);
	        
			//instantiate actuatorData
			ActuatorData actuatorData = new ActuatorData();
			
			//get the sensorData object from the JSON string
	        actuatorData = dUtil.toActuatorDataFromJson(s);
	  
	    }
	

}
