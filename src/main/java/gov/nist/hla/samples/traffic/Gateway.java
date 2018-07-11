package gov.nist.hla.samples.traffic;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import gov.nist.hla.gateway.GatewayCallback;
import gov.nist.hla.gateway.GatewayFederate;
import gov.nist.hla.gateway.GatewayFederateConfig;

public class Gateway implements GatewayCallback {
    private static final Logger log = LogManager.getLogger();

    // Initialize all of the variable related to the creation of the database
    static Connection connection = null;
    
	static String username = "root";
	static String password = "root";
	static String ipAdress = "192.168.10.23";
	static String port = "3306";
	static String databaseName = "";
	HashMap<Integer, String> dataMap = new HashMap<Integer, String>();

    private GatewayFederate gateway;
    
    // Variables
    boolean newTimeStep = true;
    int objectNumber = 1;
    
    public static void main(String[] args)
            throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        if (args.length != 1) {
            log.error("Missing command line argument for JSON configuration file.");
            return;
        }
        
        GatewayFederateConfig config = GatewayFederate.readConfiguration(args[0]);
        Gateway gatewayFederate = new Gateway(config);
        gatewayFederate.run();
    }
    
    public Gateway(GatewayFederateConfig configuration) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        this.gateway = new GatewayFederate(configuration, this);
        log.info("Federation ID:");
        log.info(configuration.getFederationId());
        
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new java.util.Date());
        log.info(configuration.getFederationId()+"_"+timeStamp);
        databaseName = configuration.getFederationId()+"_"+timeStamp;
        createSchema(databaseName);
        createDataModelTable(databaseName);
    }
    
    public void run() {
        log.trace("run");
        
        gateway.run();
    }

	@Override
	public void doTimeStep(Double timeStep) {
		// TODO Auto-generated method stub
		log.info("NEW TIME STEP");
		newTimeStep = true;

	}

	@Override
	public void initializeSelf() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeWithPeers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveInteraction(Double timeStep, String className, Map<String, String> parameters) {
        //log.trace(String.format("receiveInteraction %f %s %s", timeStep, className, parameters.toString()));
		log.info("START OF RECIEVE INTERACTION");
        
        if (dataMap.isEmpty() == true) {
			log.info("New Interaction discoverd.");
			
			
			try {
				createInteractionTable(databaseName, className);
				dataMap.put(1, className);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				log.info("Error from catch.");
			}
			
		} else {
			if(dataMap.containsValue(className) == true) {
				log.info("Avoided adding a repeat class.");
			} else {
				int maxKeyInMap = (Collections.max(dataMap.keySet()));
		        for (Entry<Integer, String> entry : dataMap.entrySet()) { 
		            if (entry.getKey()==maxKeyInMap) {
		            	try {
		    				createInteractionTable(databaseName, className);
		    				dataMap.put(maxKeyInMap+1, className);
		    			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
		    				log.info("Error from catch.");
		    			}
		            }
		        }
			}
		}
        
        
        // Initialize the data
     	int dataId = 69;
     	// Find the dataId that corresponds to the entry in the dataMap
     	for (Entry<Integer, String> entry : dataMap.entrySet()) {
            if (entry.getValue().equals(className)) {
                 dataId = entry.getKey();
            }
        }
     		
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            //log.info(entry.getKey() + ":" + entry.getValue());
        	log.info("dataName: " + className);
        	log.info("dataId: " + dataId);
        	log.info("timeStep: " + timeStep);
        	log.info("attribute: " + entry.getValue());
        	log.info("description: " + entry.getKey());
        	
            try {
    			log.info("updateInteractionTable has been called!");
    			updateInteractionTable(className, dataId, timeStep, entry.getValue(), entry.getKey());
    			log.info("SUCCESS! Added entry to table.");
            } catch (InstantiationException e) {
            	log.info("CATCH! InstantiationException");
    		} catch (IllegalAccessException e) {
    			log.info("CATCH! IllegalAccessException");
    		} catch  (ClassNotFoundException e) {
    			log.info("CATCH! ClassNotFoundException");
    		} catch (SQLException e) {
    			log.info("CATCH! SQLException");
    			log.info(e);
    		} finally {
    			try {
					connection.close();
					log.info("Connection closed!");
				} catch (SQLException e) {
					log.info("CATCH INSIDE OF FINNALY!");
				}
    		}
        }
        
        log.info("END OF RECIEVE INTERACTION");
    }

	@Override
	public void receiveObject(Double timeStep, String className, String instanceName, Map<String, String> attributes) {
        //log.trace(String.format("receiveObject %f %s %s %s", timeStep, className, instanceName, attributes.toString()));
		log.info("START OF RECIEVE OBJECT");
		log.info(dataMap);
		
		// Create the table if necessary and update the corresponding hash map, called dataMap
		if (dataMap.isEmpty() == true) {
			log.info("New object discoverd.");
			dataMap.put(1, className);
			
			try {
				createObjectTable(databaseName, className);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				log.info("Error from catch.");
			}
			
		} else {
			if(dataMap.containsValue(className) == true) {
				log.info("Avoided adding a repeat class.");
			} else {
				int maxKeyInMap = (Collections.max(dataMap.keySet()));
		        for (Entry<Integer, String> entry : dataMap.entrySet()) { 
		            if (entry.getKey()==maxKeyInMap) {
		            	dataMap.put(maxKeyInMap+1, className);
		            	try {
		    				createObjectTable(databaseName, className);
		    			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
		    				log.info("Error from catch.");
		    			}
		            }
		        }
			}
		}
		
		// Create the substring used for the description
		String mapName = attributes.toString();
		int iend = mapName.indexOf("=");
		String description = null;
		if (iend != -1) {
			description= mapName.substring(0 , iend); 
		}
		description = description.substring(1); // Remove the bracket at the beginning
		
		// Initialize the data
		int dataId = 69;
		// Find the dataId that corresponds to the entry in the dataMap
		for (Entry<Integer, String> entry : dataMap.entrySet()) {
            if (entry.getValue().equals(className)) {
            	dataId = entry.getKey();
            }
        }
		
		// Print all of the values that will be put into the table
		//log.info("className: " + className);
		//log.info("dataId: " + dataId);
		//log.info("timeStep: " + timeStep);
		//log.info("instanceName: " + instanceName);
		//log.info("attribute: " + attributes.values().toString());
		//log.info("description: " + description);
		
		String attribute = attributes.values().toString();
		
		try {
			log.info("updateObjectTable has been called!");
			updateObjectTable(className, dataId, timeStep, instanceName, attribute, description);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			log.info("Error in catch.");
		}
		
		log.info("END OF RECIEVE OBJECT");
    }

	@Override
	public void terminate() {
		// TODO Auto-generated method stub
		
	}
	
	
	// All of the following functions are not auto-generated and are specific to creating database
	
	// Create a schema within MySQL whose name is a concatenation of the federationID and a time stamp
	public void createSchema(String databaseName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://"+ipAdress+":"+port+"/";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		Statement s = connection.createStatement();
		s.executeUpdate("CREATE SCHEMA IF NOT EXISTS `" + databaseName + "`");
		log.info("A schema named " + databaseName + "has been created in MySQL.");
	}
	
	
	public void createDataModelTable(String databaseName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://"+ipAdress+":"+port+"/"+databaseName;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		Statement s = connection.createStatement();
		s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + databaseName + "`.`DataModelTable` (`data_id` INT NOT NULL AUTO_INCREMENT,`data_name` TEXT,PRIMARY KEY(`data_id`));");
		log.info("The DataModelTable has been created.");
	}
	
	
	public void createObjectTable(String databaseName, String dataName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		//Add an entry in the data model table
		String url = "jdbc:mysql://"+ipAdress+":"+port+"/"+databaseName;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		String sql = "INSERT INTO `" + databaseName + "`.`DataModelTable` (`data_name`) VALUES (?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, dataName);
		ps.executeUpdate();
		//Create a new table for the object
		Statement s = connection.createStatement();
		s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + databaseName + "`.`" + dataName + "` (`entry_id` INT NOT NULL AUTO_INCREMENT,`data_id` INT,`time_step` INT,`instance_name` TEXT,`attribute` TEXT,`description` TEXT,PRIMARY KEY (`entry_id`),FOREIGN KEY (`data_id`) REFERENCES DataModelTable(`data_id`));");
	}
	
	public void createInteractionTable(String databaseName, String dataName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://"+ipAdress+":"+port+"/"+databaseName;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		String sql = "INSERT INTO `" + databaseName + "`.`DataModelTable` (`data_name`) VALUES (?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, dataName);
		ps.executeUpdate();
		//Create a new table for the interaction
		Statement s = connection.createStatement();
		s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + databaseName + "`.`" + dataName + "` (`entry_id` INT NOT NULL AUTO_INCREMENT,`data_id` INT,`time_step` INT, `attribute` TEXT,`description` TEXT,PRIMARY KEY (`entry_id`),FOREIGN KEY (`data_id`) REFERENCES DataModelTable(`data_id`));");
	}
	
	public void updateObjectTable(String dataName, int dataId, double timeStep, String instanceName, String attribute, String description) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://"+ipAdress+":"+port+"/"+databaseName;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		String sql = "INSERT INTO `" + databaseName + "`.`" + dataName + "` (`data_id`,`time_step`,`instance_name`,`attribute`,`description`) VALUES (?,?,?,?,?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, dataId);
		ps.setDouble(2, timeStep);
		ps.setString(3, instanceName);
		ps.setString(4, attribute);
		ps.setString(5, description);
		ps.executeUpdate();
	}
	
	public void updateInteractionTable(String dataName, int dataId, double timeStep, String attribute, String description) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://"+ipAdress+":"+port+"/"+databaseName;
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		String sql = "INSERT INTO `" + databaseName + "`.`" + dataName + "` (`data_id`,`time_step`,`attribute`,`description`) VALUES (?,?,?,?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, dataId);
		ps.setDouble(2, timeStep);
		ps.setString(3, attribute);
		ps.setString(4, description);
		ps.executeUpdate();
	}
	
}