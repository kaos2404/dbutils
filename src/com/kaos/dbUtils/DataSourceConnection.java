package com.kaos.dbUtils;

import java.sql.SQLException;

/**
 * Connection class for DataSource
 * @author Kaos
 * @version v3.2
 * @since 08 April 2018
 */
public class DataSourceConnection implements DBConnection{
	public String SERVER_IP="blank";
	public String JNDI_NAME="blank";
	public String CONTEXT_FACTORY="weblogic.jndi.WLInitialContextFactory";
	private Datasource datasource;
	/**
	 * Create a DataSourceConnection from the constructor values
	 * @param SERVER_IP The IP and port of the datasource server for example : t3:\\IP:PORT
	 * @param JNDI_NAME The JNDI name of the datasource for context lookup
	 */
	public DataSourceConnection(String SERVER_IP, String JNDI_NAME){
		this.SERVER_IP=SERVER_IP;
		this.JNDI_NAME=JNDI_NAME;
		initialize();
	}
	/**
	 * Create a DataSourceConnection from the constructor values
	 * @param SERVER_IP The IP and port of the datasource server for example : t3:\\IP:PORT
	 * @param JNDI_NAME The JNDI name of the datasource for context lookup
	 * @param CONTEXT_FACTORY The context factory class for the datasource
	 */
	public DataSourceConnection(String SERVER_IP, String JNDI_NAME, String CONTEXT_FACTORY){
		this.SERVER_IP=SERVER_IP;
		this.JNDI_NAME=JNDI_NAME;
		this.CONTEXT_FACTORY=CONTEXT_FACTORY;
		initialize();
	}
	public void initialize(){
		try{
			@SuppressWarnings("rawtypes")
			Class[] classes={String.class, String.class, String.class};
			Object[] args={SERVER_IP,JNDI_NAME,CONTEXT_FACTORY};
			datasource = (Datasource) FactoryPlugin.getDBConnection("getDatasourceInstance", classes, args);
		}
		catch(Exception e){
			e.printStackTrace();
			datasource = new Datasource(SERVER_IP, JNDI_NAME, CONTEXT_FACTORY);
		}
	}
	/**
	 * Create a connection to the database
	 */
	public void createConnection() throws Exception{
		datasource.createConnection();
	}
	/**
	 * Free all current resources
	 */
	public void freeResources() throws Exception{
		datasource.freeResources();
	}
	/**
	 * @return The Connector object used to handle connections and resources
	 */
	public Connector connector(){
		return datasource.connector();
	}
	/**
	 * Close the connections to the database
	 */
	public boolean closeConnection() throws Exception {
		return datasource.closeConnection();
	}
	/**
	 * @return True if the connection object has an active connection to the database
	 */
	public boolean isLive() throws SQLException {
		return datasource.isLive();
	}
	/**
	 * @return True if the connection object is not associated with an active DBUtils object
	 */
	public boolean isFree() {
		return datasource.isFree();
	}
	/**
	 * Shutdown the datasource pool associated with the connection parameters
	 */
	public void shutdownPool(){
		datasource.shutdownPool();
	}
	/**
	 * Set to true to indicate that the connection is free for allocation
	 */
	public void setFree(boolean b) {
		datasource.setFree(b);
	}
}

