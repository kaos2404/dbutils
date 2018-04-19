package com.kaos.dbUtils;

import java.sql.SQLException;

/**
 * Connection class for DataSource
 * @author Kaos
 * @version v3.2
 * @since 08 April 2018
 */
public class JDBCConnection implements DBConnection{
	private Jdbc jdbc;
	public String IP="blank";
	public String USERNAME="blank";
	public String PASSWORD="blank";
	public String URL="jdbc:oracle:thin:@";
	public String JDBC_CLASS="oracle.jdbc.driver.OracleDriver";
	/**
	 * Create a Oracle thin JDBCConnection object from constructor values
	 * @param IP The IP and port of the database server example : IP:PORT:DBSID
	 * @param USERNAME The user for the database
	 * @param PASSWORD The password for the user
	 */
	public JDBCConnection(String IP, String USERNAME, String PASSWORD) {
		this.IP=IP;
		this.USERNAME=USERNAME;
		this.PASSWORD=PASSWORD;
		initialize();
	}
	/**
	 * Create a JDBCConnection object from constructor values
	 * @param IP The IP and port of the database server example : IP:PORT:DBSID
	 * @param USERNAME The user for the database
	 * @param PASSWORD The password for the user
	 * @param URL The URL prefix, for example : jdbc:oracle:thin:@
	 * @param JDBC_CLASS The driver class to load, for example : oracle.jdbc.driver.OracleDriver
	 */
	public JDBCConnection(String IP, String USERNAME, String PASSWORD, String URL, String JDBC_CLASS) {
		this.IP=IP;
		this.USERNAME=USERNAME;
		this.PASSWORD=PASSWORD;
		this.URL=URL;
		this.JDBC_CLASS=JDBC_CLASS;
		initialize();
	}
	/**
	 * Create a connection to the database
	 */
	public void createConnection() throws Exception {
		jdbc.createConnection();
	}
	/**
	 * Free all current resources
	 */
	public void freeResources() throws Exception{
		jdbc.freeResources();
	}
	/**
	 * @return The Connector object used to handle connections and resources
	 */
	public Connector connector(){
		return jdbc.connector();
	}
	/**
	 * Close the connections to the database
	 * @return True if the connection to the database was closed successfully
	 */
	public boolean closeConnection() throws Exception {
		return jdbc.closeConnection();
	}
	/**
	 * @return True if the connection object has an active connection to the database
	 */
	public boolean isLive() throws SQLException {
		return jdbc.isLive();
	}
	/**
	 * @return True if the connection object is not associated with an active DBUtils object
	 */
	public boolean isFree() {
		return jdbc.isFree();
	}
	/**
	 * Shutdown the JDBC pool associated with the connection parameters
	 */
	public void shutdownPool(){
		jdbc.shutdownPool();
	}
	public void initialize() {
		try{
			@SuppressWarnings("rawtypes")
			Class[] classes={String.class, String.class, String.class, String.class, String.class};
			Object[] args={IP, USERNAME, PASSWORD, URL, JDBC_CLASS};
			jdbc = (Jdbc) FactoryPlugin.getDBConnection("getJDBCInstance", classes, args);
		}
		catch(Exception e){
			e.printStackTrace();
			jdbc = new Jdbc(IP, USERNAME, PASSWORD, URL, JDBC_CLASS);
		}
	}
	public void setFree(boolean b) {
		jdbc.setFree(b);
	}
	
}