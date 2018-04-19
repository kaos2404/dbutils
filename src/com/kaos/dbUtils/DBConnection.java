package com.kaos.dbUtils;

import java.sql.SQLException;

/**
 * Connection class for DataSource
 * @author Kaos
 * @version v4.1
 * @since 14 April 2018
 */
public interface DBConnection{
	/**
	 * Create a connection to the database
	 * @throws Exception
	 */
	public void createConnection() throws Exception;
	/**
	 * @return The Connector object used to handle connections and resources
	 */
	public Connector connector();
	/**
	 * Free all resources and close connections
	 * @throws Exception
	 */
	public void freeResources() throws Exception;
	/**
	 * Close the database connection
	 * @return True if the connection was closed successfully
	 * @throws Exception
	 */
	public boolean closeConnection() throws Exception;
	/**
	 * Check if the connection to the database is open
	 * @return True if the connection is open
	 * @throws SQLException
	 */
	public boolean isLive() throws SQLException;
	/**
	 * Check if the connection is associated with an active DBUtils object
	 * @return True if the connection is active
	 */
	public boolean isFree();
	/**
	 * Shutdown the pool associated with the connection
	 */
	public void shutdownPool();
	/**
	 * Basic initialization of the connection
	 */
	public void initialize();
	/**
	 * Internally used to set the connection as free
	 * @param b true if object is to be freed
	 */
	public void setFree(boolean b);
}