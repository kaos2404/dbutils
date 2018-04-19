package com.kaos.dbUtils;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Savepoint;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * Class for operation of most transactions on a database connection
 * @author Kaos
 * @version v4.1
 * @since 14 April 2018
 */
public class DBUtils {
	private Db db;
	/**
	 * Initialize the object with a connection object and log
	 * @param connection
	 * @param log
	 */
	public DBUtils(DBConnection connection, Logger log, Priority priority, boolean verbose){
		db=new Db(connection, log, priority, verbose);
	}
	/**
	 * Fetch the SQL Connection object in use
	 * @return SQL Connection object
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception{
		return db.connect();
	}
	/**
	 * Free the resources that are in use
	 * @throws Exception
	 */
	public void freeResources() throws Exception{
		db.freeResources();
	}
	/**
	 * Rollback the transactions made on the connection
	 * @return true if the rollback was successfull
	 * @throws Exception
	 */
	public boolean rollback() throws Exception{
		return db.rollback();
	}
	@Override
	protected void finalize() throws Throwable {
		db.finalize();
		super.finalize();
	}
	/**
	 * Close the active connection
	 * @return true if the connection was closed successfully
	 * @throws Exception
	 */
	public boolean close() throws Exception{
		return db.close();
	}
	/**
	 * Commit the transactions made on the active connection
	 * @return true if the commit could be made
	 * @throws Exception
	 */
	public boolean commit() throws Exception{
		return db.commit();
	}
	/**
	 * Connect to the database forcefully. If there is an active connection, close and then re-connect
	 * @return true if the connection was re-established
	 * @throws Exception
	 */
	public Connection forceReconnect() throws Exception{
		return db.forceReconnect();
	}
	/**
	 * Run a single line query on the database
	 * @param query The query to be executed
	 * @return A single row of the database mapped to a hashmap. Result can be extracted from the map either with the column/alias name or the column number 
	 * @throws Exception
	 */
	public Getter simpleQuery(String query) throws Exception{
		return db.simpleQuery(query);
	}
	/**
	 * Run a prepared query from the database
	 * @param query The query to be executed
	 * @param setter The setter object to be prepared with the query
	 * @return A single row of the database mapped to a hashmap. Result can be extracted from the map either with the column/alias name or the column number
	 * @throws Exception
	 */
	public Getter simplePreparedQuery(String query, Setter setter) throws Exception{
		return db.simplePreparedQuery(query, setter);
	}
	/**
	 * Run a prepared query from the database
	 * @param query The query to be executed
	 * @param setter The setter object to be prepared with the query
	 * @return A multiple rows of the database stored in an array list, each row mapped to a hashmap. Result can be extracted from the map either with the column/alias name or the column number
	 * @throws Exception
	 */
	public ArrayList<Getter> preparedQuery(String query, Setter setter) throws Exception{
		return db.preparedQuery(query, setter);
	}
	/**
	 * Run a single line update statement on the database
	 * @param query The update statement to be executed
	 * @param commit true if the connection is to be committed after the update
	 * @return The number of rows that were affected
	 * @throws Exception
	 */
	public int simpleUpdate(String query, boolean commit) throws Exception{
		return db.simpleUpdate(query, commit);
	}
	/**
	 * Run a prepared update on the database
	 * @param query The update statement to be executed
	 * @param setter The setter object to be prepared with the query
	 * @param commit true if the connection is to be committed after the update
	 * @return The number of rows that were affected
	 * @throws Exception
	 */
	public int preparedUpdate(String query, Setter setter, boolean commit) throws Exception{
		return db.preparedUpdate(query, setter, commit);
	}
	/**
	 * Call a stored procedure
	 * @param query The query to be executed
	 * @param setter The setter object to be prepared with the query
	 * @param commit 
	 * @return
	 * @throws Exception
	 */
	public boolean callProcedure(String query, Setter setter, boolean commit) throws Exception{
		return db.callProcedure(query, setter, commit);
	}
	/**
	 * Call the method to create a Blob
	 * @return A java.sql.Blob object from the current connection
	 * @throws Exception 
	 */
	public Blob createBlob() throws Exception{
		return db.createBlob();
	}
	/**
	 * Call the method to create a Clob
	 * @return A java.sql.Clob object from the current connection
	 * @throws Exception 
	 */
	public Clob createClob() throws Exception{
		return db.createClob();
	}
	/**
	 * Create a no name savepoint
	 * @return A java.sql.Savpoint object from the current connection
	 * @throws Exception
	 */
	public Savepoint setSavepoint() throws Exception {
		return db.setSavepoint();
	}
	/**
	 * Create a named savepoint
	 * @return A java.sql.Savpoint object from the current connection
	 * @throws Exception
	 */
	public Savepoint setSavepoint(String name) throws Exception {
		return db.setSavepoint(name);
	}
	/**
	 * Rollback to a particular savepoint
	 * @param savepoint Savepoint to which operations need to be rolled back
	 * @throws Exception
	 */
	public void rollback(Savepoint savepoint) throws Exception{
		db.rollback(savepoint);
	}
}