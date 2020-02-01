package schooldomain.neu.pallaksingh.connecteddevices.common;

import org.apache.commons.configuration.*;

import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Set;

public class ConfigUtil {
	
	// set the default configuration file in case no config file provided
	String DEFAULT_CONFIG_FILE = "config/ConnectedDevicesConfig.props";
	
	String fileName; //path of configuration file
	HierarchicalINIConfiguration config; //configuration file 
	boolean configFileLoaded = false; //#initially set the flag of whether the config file is loaded to false
	
	//this constructor sets the fileName to default fileName
	public ConfigUtil() {
		this.fileName = DEFAULT_CONFIG_FILE;
		loadConfig();
	}
	
	//this constructor sets the fileName to the one being passed in the constructor
	public ConfigUtil(String fileName) {
		this.fileName = fileName;
		loadConfig();
	}
	
	//getters and setters
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HierarchicalINIConfiguration getConfig() {
		return config;
	}

	public void setConfig(HierarchicalINIConfiguration config) {
		this.config = config;
	}

	public boolean isConfigFileLoaded() {
		return configFileLoaded;
	}

	public void setConfigFileLoaded(boolean configFileLoaded) {
		this.configFileLoaded = configFileLoaded;
	}

	//this method returns the value for the given section and the key otherwise returns null string
	public String getValue(String section, String key) {
		
		// check if the config file is loaded
		if(this.isConfigFileLoaded() == true) {
			
			//get the string value from given sectioon and key
			String val = String.valueOf(this.config.getSection(section).getString(key));
			
			//the getSection(section).getString(key) returns a null if the section or key aren't valid
			//check if val is not null
			if(val != null) {
				
				//return the string
				return val;
			} 
			
			//if the section or key do not exist in the file
			else {
				
				//log the error
				
				//return a null string ;
				return null;		
			}
			
		}  else { //if the config file is not loaded
			
			//if the config file is not loaded, return an null string
			return null;
		}
	}
	
	//this method returns the integer value for the given section and the key otherwise returns a null
	public Integer getIntegerValue(String section, String key) {
		
		// check if the config file is loaded
		if(this.isConfigFileLoaded() == true) {
			
			//get the value in string
			String val = String.valueOf(this.config.getSection(section).getString(key));
			
			
			//the getSection(section).getString(key) returns a null if the section or key aren't valid
			//check if val is not null
			if(val!=null) {
				
				//try converting the string to int
				try {
					
					//convert the string to int
					return Integer.parseInt(val);
					
				} catch(NumberFormatException n) { //it will throw NumberFormatException if you try to convert an invalid String
					
					//return a null
					return null;
					
				} 
			} else { //if the section or key do not exist
				
				//return a null
				return null;
			}
		} else { //if the config file is not loaded
			
			return null;
		}
			
	}
		
	//this method returns the boolean value for the given section and the key otherwise returns a null
	public Boolean getBooleanValue(String section, String key) {
		
		// check if the config file is loaded
		if(this.isConfigFileLoaded() == true) {
			
			//get the value in string
			String val = String.valueOf(this.config.getSection(section).getString(key));
			
			
			//the getSection(section).getString(key) returns a null if the section or key aren't valid
			//check if val is not null
			if(val!=null) {
				
				//try converting the string to boolean
				
				//if it is true
				if(val.equals("true") || val.equals("True")) {
					
					//return true
					return true;
					
				} else if(val.equals("false") || val.equals("false")) { //if it is false 
					
					//return false
					return false;
				
				} else { //if it is not a valid boolean
					
					//return null
					return null;
				}
	
			} else { //if the section or key do not exist
				
				//return a null
				return null;
			}
		} else { //if the config file is not loaded
			
			//return a null
			return null;
		}
			
	}	
	//this method checks if the config file has any valid key value pairs
	public boolean hasConfigData() {
		
		//if the config file is loaded
		if(this.configFileLoaded) {
			
			//get a set of all the sections in the file
			Set<String> sections = this.config.getSections();
			
			//if the set of sections is not empty
			if(sections.size() != 0) {
				
				//run a loop to traverse through each section
				for(String section : sections) {
					
					//get an iterator for all the keys in the section
					Iterator i = this.config.getSection(section).getKeys();
					
					//if the section is not empty
					if(i.hasNext()!= false) {
						
						//there are valid key-value pairs
						return(true);
					}
				}
				
				//if there are no sections 
				return(false);
			}
		} 
		
		//if config file is not loaded
		return(false);
	}
	
	//this method loads the default config file
	public boolean loadConfig() {
		
		//get the absolute file path 
		File f = new File(this.fileName).getAbsoluteFile();
		
		//if the file exists 
		if(f.exists()) {
			
			//try if the configuration is loaded 
			try {
				
				//read the configuration (INI configuration is stored in hierarchical format as section and keys
				this.config = new HierarchicalINIConfiguration(fileName);
				
			} 
			
			//if there was an error while loading the configuration file
			catch (ConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				//change configFileLoaded to false
				this.configFileLoaded = false;
				
				//return false
				return false;
			}
			
			//change configFileLoaded to true because loaded properly
			this.configFileLoaded = true;
			
			//return true
			return true;
			} 
		
		//if file doesn't exist
		else {
			
			//return false
			return false;
		}
	}
	
	//this method loads the config file that is sent as the parameter
	public boolean loadConfig(String fileName) {
		
		//get the absolute file path 
		File f = new File(fileName).getAbsoluteFile();
		
		//if the file exists 
		if(f.exists()) {
			
			//try if the configuration is loaded 
			try {
				
				//read the configuration (INI configuration is stored in hierarchical format as section and keys
				this.config = new HierarchicalINIConfiguration(fileName);
		} 
			
			//if there was an error while loading the configuration file
			catch (ConfigurationException e) {
		
			//change configFileLoaded to false
			this.configFileLoaded = false;
			
			//return false
			return false;
		}
			
		//no errors encountered while loading
		this.configFileLoaded = true;
		
		//return true
		return true;
		} 
		
		//if file does not exist
		else {
			
			//change configFileLoaded to false
			this.configFileLoaded = false;
			
			//return false
			return false;
		}	
	}
	
		
}
