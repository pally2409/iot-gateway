// import libraries and packages
package schooldomain.neu.pallaksingh.connecteddevices.common;

public class ActuatorData {
	
	//initialize name to 'Not Set
	String name = "Not Set";
	
	//initialize command to 'Not Set
	String command = "Not Set";
	
	//initialize current value to 'Not Set
	Object value = "Not Set";
	
	//this method returns the command as a string
	public String getCommand() {
		return this.command;
	}
	
	//this method returns the command as a string
		public String getName() {
			return this.name;
		}
	
	//this method gets the actuator value
	public Object getValue() {
		return this.value;
	}
	
	//this method sets the actuator value
	public void setValue(Object value) {
		
		//if the value passed was not null
		if(value!=null) {
			
			//set the value 
			this.value = value;
			
		}

			
	}
	
	//this method sets the command
	public void setCommand(String command) {
		
		//if the command passed was not null
		if(command!=null) {
					
			//set the command 
			this.command = command;
					
		}
				
	}
	
	//this method sets the name
	public void setName(String name) {
				
		//if the name passed was not null
		if(name!=null) {
							
			//set the name
			this.name = name;
							
		}
					
	}

	@Override
	public String toString() {
		return "ActuatorData [name=" + name + ", command=" + command + ", value=" + value + "]";
	}
	
	 

}
