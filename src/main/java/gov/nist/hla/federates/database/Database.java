/*
Database Wrapper for UCEF
Written over the summer of 2018 by SURF student James Arnold under the direction of Dr. Thomas Roth.
*/

package gov.nist.hla.federates.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

//import SensorAggregation.Aggregator.AggregationMethod;
import gov.nist.hla.gateway.GatewayCallback;
import gov.nist.hla.gateway.GatewayFederate;

public class Database implements GatewayCallback {
    private static final Logger log = LogManager.getLogger();
    
    // Initialize all of the variable related to the creation of the database
    static Connection connection = null;
	static String username = "";
	static String password = "";
	static String ipAddress = "";
	static String port = "";
	static String schemaName = "";
    private GatewayFederate gateway;
    boolean newTimeStep = true;
    HashMap<Integer, String> indexMap = new HashMap<Integer, String>();
    
    // The main() function is used to load the configuration file
    public static void main(String[] args)
            throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        if (args.length != 1) {
            log.error("Missing command line argument for JSON configuration file.");
            return;
        }
        GatewayConfiguration config = Database.readConfiguration(args[0]);
        Database databaseFederate = new Database(config);
        databaseFederate.run();
    }
    
    // Reads the configuration file
    private static GatewayConfiguration readConfiguration(String filepath)
            throws IOException {
        log.info("Reading JSON configuration file at " + filepath);
        File configFile = Paths.get(filepath).toFile();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(configFile, GatewayConfiguration.class);
    }
    
    // Creates the schema and index table in MySQL
    public Database(GatewayConfiguration configuration) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        this.gateway = new GatewayFederate(configuration, this);
        
        // Pull values from the configuration file
        username = configuration.getUsername();
        password = configuration.getPassword();
        ipAddress = configuration.getIpAddress();
        port = configuration.getPort();
        
        // Time stamp the schema name to avoid duplicates
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new java.util.Date());
        schemaName = configuration.getFederationId()+"_"+timeStamp; // Format for naming the schema
        
        // Create the schema and index table within the schema
        createSchema(schemaName);
        createIndexTable(schemaName);
    }
    
    public void run() {
        gateway.run();
    }

	@Override
	public void doTimeStep(Double timeStep) {
		newTimeStep = true;
	}

	@Override
	public void initializeSelf() {
		
	}

	@Override
	public void initializeWithPeers() {
		
	}

	
	@Override
	public void receiveInteraction(Double timeStep, String className, Map<String, String> parameters) {
		boolean newInteractionTable = true; // Used to determine when to add columns to new interaction tables
        if (indexMap.isEmpty() == true) {
        	// Create the first table and update the corresponding hash map, called indexMap
			try {
				createInteractionTable(schemaName, className, 1); // Create first interaction table
				indexMap.put(1, className);  // Represent index table in index map
				newInteractionTable = true;
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				log.info(e); // Interaction table cannot be created
			} finally {
    			try {
					connection.close(); // Close the connection to the database schema
				} catch (SQLException e) {
					log.info(e);  // Connection cannot be closed
				}
    		}
		} else {
			if(indexMap.containsValue(className) == true) {
				// Avoids creating a table that already exists for all tables after the creation of the first
				newInteractionTable = false;
			} else {
				// There are already other tables, but not one for this interaction, a new table is created
				int maxKeyInMap = (Collections.max(indexMap.keySet()));
		        for (Entry<Integer, String> entry : indexMap.entrySet()) { 
		            if (entry.getKey()==maxKeyInMap) {
		            	try {
		    				createInteractionTable(schemaName, className, maxKeyInMap+1); // Create new interaction table
		    				indexMap.put(maxKeyInMap+1, className); // Represent index table in index map
		    			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
		    				log.info(e); // Interaction table cannot be created
		    			} finally {
		        			try {
		    					connection.close(); // Close the connection to the database schema
		    				} catch (SQLException e) {
		    					log.info(e); // Connection cannot be closed
		    				}
		        		}
		            }
		        }
			}
		}
        
        // Determines the correct index id
     	int indexId = 1;
     	for (Entry<Integer, String> entry : indexMap.entrySet()) {
            if (entry.getValue().equals(className)) {
                 indexId = entry.getKey();
            }
        }
     	
     	// Adds columns to the new interaction tables
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            try {
    			 if (newInteractionTable == true) {
    				 // Since the interaction table is new, columns must be added for each parameter
    				 addColumnToInteractionTable(className, indexId, entry.getKey());
    			 }
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
    			log.info(e); // Columns could not be added to the interaction table
    		} finally {
    			try {
					connection.close(); // Close the connection to the database schema
				} catch (SQLException e) {
					log.info(e); // The connection could not be closed
				}
    		}
        }
        
        
        // Update the interaction table since a new interaction was received
        try {
        	// Adds a row to the interaction table
			updateInteractionTable(className, indexId, timeStep, parameters);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			log.info(e); // Interaction table could not be updated.
		}
    }
	
	
	@Override
	public void receiveObject(Double timeStep, String className, String instanceName, Map<String, String> attributes) {	
		if (indexMap.isEmpty() == true) {
			// Create the first table and update the corresponding hash map, called indexMap
			try {
				createObjectTable(schemaName, className, 1); // Create first object table
				indexMap.put(1, className); // Represent index table in index map
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
				log.info(e); // Object table cannot be created
			} finally {
    			try {
					connection.close(); // Close the connection to the database schema
				} catch (SQLException e) {
					log.info(e); // Connection cannot be closed
				}
    		}
		} else {
			if(indexMap.containsValue(className) == true) {
				// Avoids creating a table that already exists (repeat class)
			} else {
				// There are already other tables, but not one for this object, a new table is created
				int maxKeyInMap = (Collections.max(indexMap.keySet()));
		        for (Entry<Integer, String> entry : indexMap.entrySet()) { 
		            if (entry.getKey()==maxKeyInMap) {
		            	try {
		    				createObjectTable(schemaName, className, maxKeyInMap+1); // Create new object table
		    				indexMap.put(maxKeyInMap+1, className); // Represent index table in index map
		    			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
		    				log.info(e); // Object table cannot be created
		    			} finally {
		        			try {
		    					connection.close(); // Close the connection to the database schema
		    				} catch (SQLException e) {
		    					log.info(e); // Connection cannot be closed
		    				}
		        		}
		            }
		        }
			}
		}
		
		// Determines the correct index id
		int indexId = 1;
		for (Entry<Integer, String> entry : indexMap.entrySet()) {
            if (entry.getValue().equals(className)) {
            	indexId = entry.getKey();
            }
        }
		
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
            final String attribute = entry.getValue();
            final String description = entry.getKey();

		// Update the object table since a new object was received
		try {
			// Adds a row to the object table
			updateObjectTable(className, indexId, timeStep, instanceName, attribute, description);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			log.info(e);  // Object table could not be updated.
		} finally {
			try {
				connection.close(); // Close the connection to the database schema
			} catch (SQLException e) {
				log.info(e); // The connection could not be closed
			}
		}
        }
    }

    @Override
    public void prepareToResign() {
    }

	@Override
	public void terminate() {
		
	}
	
	/**
	 * Creates a schema within MySQL whose name is a concatenation of the federationID and a time stamp.
	 * @param schemaName
	 * 		Name of the schema which stores all of the created tables. It is a concatenation of the federationID and a time stamp.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createSchema(String schemaName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Connect to the general MySQL address
		String url = "jdbc:mysql://"+ipAddress+":"+port+"/";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		Statement s = connection.createStatement();
		
		// Send the query to MySQL to create the schema where all of the tables will be stored
		s.executeUpdate("CREATE SCHEMA IF NOT EXISTS `" + schemaName + "`");
		log.info("A schema named " + schemaName + "has been created in MySQL.");
	}
	
	/**
	 * Creates the index table that stores the "dataName" for each object and interaction
	 * @param schemaName
	 * 		Name of the schema where the index table will be placed.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createIndexTable(String schemaName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Connect to schema within MySQL
		String url = "jdbc:mysql://"+ipAddress+":"+port+"/"+schemaName+"?autoReconnect=true&useSSL=false";
        Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		Statement s = connection.createStatement();
		
		// Send the query to MySQL to create the index table
		s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`IndexTable` (`indexId` INT NOT NULL AUTO_INCREMENT,`dataName` TEXT,PRIMARY KEY(`indexId`));");
		log.info("The IndexTable has been created.");
	}
	
	/**
	 * Creates a new object table.
	 * Connects to the database schema defined in createSchema(), adds an entry to the index table, and creates a new object table.
	 * @param schemaName
	 * 		Name of the schema where all the tables are being stored.
	 * @param tableName
	 * 		Name of the table that is being created.
	 * @param indexId
	 * 		Number used to reference the object table in the index table.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createObjectTable(String schemaName, String tableName, int indexId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Connect to the created schema
		String url = "jdbc:mysql://"+ipAddress+":"+port+"/"+schemaName+"?autoReconnect=true&useSSL=false";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		
		// Query to add an entry to the index table
		String sql = "INSERT INTO `" + schemaName + "`.`IndexTable` (`dataName`) VALUES (?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, tableName);
		
		// Send the query to MySQL to add a row to the index table
		ps.executeUpdate();
		
		// Create a new table for the object
		String newTableName = "Table" + indexId + ".object";
		Statement s = connection.createStatement();
		
		// Send the query to MySQL to create the object table
		s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`" + newTableName + "` (`entryId` "
				+ "INT NOT NULL AUTO_INCREMENT,`indexId` INT,`timeStep` INT,`instanceName` TEXT,`attribute` "
				+ "TEXT,`description` TEXT,PRIMARY KEY (`entryId`),FOREIGN KEY (`indexId`) REFERENCES IndexTable(`indexId`));");
		log.info("An object table named " + tableName + "has been created in MySQL.");
	}
	
	/**
	 * Creates a new interaction table.
	 * Connects to the database schema defined in createSchema(), adds an entry to the index table, and creates a new interaction table.
	 * @param schemaName
	 * 		Name of the schema where all the tables are being stored.
	 * @param tableName
	 * 		Name of the table that is being created.
	 * @param indexId
	 * 		Number used to reference the interaction table in the index table.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createInteractionTable(String schemaName, String tableName, int indexId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Connect to the created schema
		String url = "jdbc:mysql://"+ipAddress+":"+port+"/"+schemaName+"?autoReconnect=true&useSSL=false";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		
		// Query to add an entry to the index table
		String sql = "INSERT INTO `" + schemaName + "`.`IndexTable` (`dataName`) VALUES (?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setString(1, tableName);
		
		// Send the query to MySQL to add a row to the index table
		ps.executeUpdate();
		
		// Create a new table for the interaction
		String newTableName = "Table" + indexId + ".interaction";
		Statement s = connection.createStatement();
		
		// Send the query to MySQL to create the interaction table
		s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`" + newTableName + "` (`entryId` INT NOT NULL AUTO_INCREMENT,"
				+ "`indexId` INT,`timeStep` INT, PRIMARY KEY (`entryId`),FOREIGN KEY (`indexId`) REFERENCES IndexTable(`indexId`));");
		log.info("An interaction table named " + tableName + "has been created in MySQL.");
	}
	
	/**
	 * Adds a column to an interaction table that corresponds to an attributes of an interactions, when new interaction tables are created.
	 * Connects to the database schema defined in createSchema() and then queries the table to add a new column.
	 * @param tableName
	 * 		Name of the table where columns are being added.
	 * @param indexId
	 * 		Number used to reference the interaction table in the index table.
	 * @param columnName
	 * 		Name of the column (attribute) that is being added to the table.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void addColumnToInteractionTable(String tableName, int indexId, String columnName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Connect to the created schema
		String url = "jdbc:mysql://"+ipAddress+":"+port+"/"+schemaName+"?autoReconnect=true&useSSL=false";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		
		String newTableName = "Table" + indexId + ".interaction";
		String sql = "ALTER TABLE `" + schemaName + "`.`" + newTableName + "` ADD " + columnName + " TEXT;";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		// Send the query to MySQL to add a column to the interaction table
		ps.executeUpdate();
	}
	
	/**
	 * Adds a row to the object table each time an instance of an object is received.
	 * Connects to the database schema defined in createSchema(), builds a query with the of the attribute and description for an instance of an object,
	 * and then executes this query. 
	 * @param tableName
	 * 		Name of the object table that is being updated.
	 * @param indexId
	 * 		Index id that describes this object in the index table.
	 * @param timeStep
	 * 		Number defining the current time step.
	 * @param instanceName
	 * 		Name of the instance of an object.
	 * @param attribute
	 * 		Value that the object contains.
	 * @param description
	 * 		Description of what is the meaning of the value being stored.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateObjectTable(String tableName, int indexId, double timeStep, String instanceName, String attribute, String description) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Connect to the created schema
		String url = "jdbc:mysql://"+ipAddress+":"+port+"/"+schemaName+"?autoReconnect=true&useSSL=false";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		
		String newTableName = "Table" + indexId + ".object";
		String sql = "INSERT INTO `" + schemaName + "`.`" + newTableName + "` (`indexId`,`timeStep`,`instanceName`,`attribute`,`description`) VALUES (?,?,?,?,?);";
		PreparedStatement ps = connection.prepareStatement(sql);
		
		// Add values into the prepared statement
		ps.setInt(1, indexId);
		ps.setDouble(2, timeStep);
		ps.setString(3, instanceName);
		ps.setString(4, attribute);
		ps.setString(5, description);
		
		// Send the query to MySQL to add a row to the object table
		ps.executeUpdate();
	}
	
	/**
	 * Adds a row to the interaction table each time an interaction is received.
	 * Connects to the database schema defined in createSchema(), builds a query specific to the number of parameters for the interaction,
	 * and then executes this query. 
	 * @param tableName
	 * 		Name of the interaction table that is being updated.
	 * @param indexId
	 * 		Index id that describes this interaction in the index table.
	 * @param timeStep
	 * 		Number defining the current time step.
	 * @param parameters
	 * 		Map of the parameters received by the interaction.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateInteractionTable(String tableName, int indexId, double timeStep, Map<String, String> parameters) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Connect to the created schema
		String url = "jdbc:mysql://"+ipAddress+":"+port+"/"+schemaName+"?autoReconnect=true&useSSL=false";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(url, username, password);
		
		String newTableName = "Table" + indexId + ".interaction";
		String sql = "INSERT INTO `" + schemaName + "`.`" + newTableName + "` (`indexId`,`timeStep`) VALUES (?,?);";
		String sql1 = "INSERT INTO `" + schemaName + "`.`" + newTableName + "` (`indexId`,`timeStep`";
		String sql2 = ") VALUES (?,?";
		
		// Build the query that is specific to the number of parameters, corresponding to number of columns in the database
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
            sql1 = sql1 + ",`" + entry.getKey() + "`";
            sql2 = sql2 + ",?";
            sql = sql1 + sql2 + ");";
        }
		PreparedStatement ps = connection.prepareStatement(sql);
		
		// Add values into the prepared statement
		ps.setInt(1, indexId);
		ps.setDouble(2, timeStep);
		int i = 3;
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			ps.setString(i, entry.getValue());
			i = i+1;
		}
		
		// Send the query to MySQL to add a row to the interaction table
		ps.executeUpdate();
	}
}
