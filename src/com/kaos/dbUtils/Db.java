package com.kaos.dbUtils;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.Savepoint;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

class Db{
	private Logger log;
	private DBConnection connection;
	private Priority priority;
	private boolean verbose;
	protected Db(DBConnection connection, Logger log, Priority priority, boolean verbose){
		this.connection=connection;
		this.log=log;
		this.priority=priority;
		this.verbose=verbose;
	}
	private String verboseLogger(String query, Setter setter){
		for(int i : setter.keySet()){
			query=query.replaceFirst("\\?", String.valueOf(setter.get(i)));
		}
		return query;
	}
	protected void freeResources() throws Exception {
		connection.connector().freeResources();
	}
	protected boolean rollback() throws Exception {
		try{
			connection.connector().getConnection().rollback();
		}
		catch(Exception e){
			log.error("Error when rolling back connection");
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
		log.log(priority, "Connection rolled back");
		return true;
	}
	protected void finalize() throws Throwable {
		log.log(priority, "Clearing object from memory");
		try
		{
			connection.connector().getConnection().rollback();
		}
		catch(Exception e){
			log.log(priority, e.getMessage());
		}
		freeResources();
		super.finalize();
	}
	protected Connection connect() throws Exception {
		if(connection.connector().con==null || connection.connector().con.isClosed()){
			connection.createConnection();
			log.log(priority, "Connected to database");
		}
		return connection.connector().getConnection();
	}
	protected boolean close() throws Exception {
		try{
			connection.connector().getConnection().rollback();
			freeResources();
		}
		catch(Exception e){
			log.error("Error when closing connection");
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
		log.log(priority, "Connection closed");
		return true;
	}
	protected boolean commit() throws Exception {
		try{
			connection.connector().getConnection().commit();
		}
		catch(Exception e){
			log.error("Error when comitting connection");
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
		log.log(priority, "Connection committed");
		return true;
	}
	protected Connection forceReconnect() throws Exception {
		freeResources();
		return connect();
	}
	protected Getter simpleQuery(String query) throws Exception {
		Connection con=connect();
		connection.connector().ps=con.prepareStatement(query);
		log.log(priority, "Running query : "+query);
		connection.connector().rs=connection.connector().ps.executeQuery();
		ResultSetMetaData meta=connection.connector().rs.getMetaData();
		Getter result=new Getter();
		if(connection.connector().rs.next()){
			for(int i=1;i<=meta.getColumnCount();i++){
				if(result.get(meta.getColumnName(i))==null){
					result.put(meta.getColumnName(i), connection.connector().rs.getObject(i));
					result.put(i, connection.connector().rs.getString(i));
				}
				else{
					throw new RuntimeException("Multiple data will get mapped to same column name");
				}
			}
			if(connection.connector().rs.next()){
				throw new RuntimeException("Result set returns more than single row");
			}
		}
		connection.connector().ps.close();
		connection.connector().rs.close();
		return result;
	} 
	protected Getter simplePreparedQuery(String query, Setter setter) throws Exception {
		Connection con=connect();
		connection.connector().ps=con.prepareStatement(query);
		if(!verbose){
			log.log(priority, "Running query : "+query);
		}
		else{
			log.log(priority, "Running query : "+verboseLogger(query, setter));
		}
		if(setter!=null && !setter.isEmpty()){
			for(int i : setter.keySet()){
				connection.connector().ps.setObject(i, setter.get(i));
			}
		}
		connection.connector().rs=connection.connector().ps.executeQuery();
		ResultSetMetaData meta=connection.connector().rs.getMetaData();
		Getter result=new Getter();
		if(connection.connector().rs.next()){
			for(int i=1;i<=meta.getColumnCount();i++){
				if(result.get(meta.getColumnName(i))==null){
					result.put(meta.getColumnName(i), connection.connector().rs.getString(i));
					result.put(i, connection.connector().rs.getString(i));
				}
				else{
					throw new RuntimeException("Ambigious definition of column : "+meta.getColumnName(i));
				}
			}
			if(connection.connector().rs.next()){
				throw new RuntimeException("Result set returns more than single row, maybe you should use preparedQuery method");
			}
		}
		connection.connector().ps.close();
		connection.connector().rs.close();
		setter.clear();
		return result;
	}
	protected ArrayList<Getter> preparedQuery(String query, Setter setter) throws Exception {
		Connection con=connect();
		connection.connector().ps=con.prepareStatement(query);
		if(!verbose){
			log.log(priority, "Running query : "+query);
		}
		else{
			log.log(priority, "Running query : "+verboseLogger(query, setter));
		}
		if(setter!=null && !setter.isEmpty()){
			for(int i : setter.keySet()){
				connection.connector().ps.setObject(i, setter.get(i));
			}
		}
		connection.connector().rs=connection.connector().ps.executeQuery();
		ResultSetMetaData meta=connection.connector().rs.getMetaData();
		ArrayList<Getter> result=new ArrayList<Getter>();
		int count=0;
		while(connection.connector().rs.next()){
			result.add(new Getter());
			for(int i=1;i<=meta.getColumnCount();i++){
				if(result.get(count).get(meta.getColumnName(i))==null){
					result.get(count).put(meta.getColumnName(i), connection.connector().rs.getString(i));
					result.get(count).put(i, connection.connector().rs.getString(i));
				}
				else{
					throw new RuntimeException("Multiple data will get mapped to same column name");
				}
			}
			count++;
		}
		connection.connector().ps.close();
		connection.connector().rs.close();
		setter.clear();
		return result;
	}
	protected int simpleUpdate(String query, boolean commit) throws Exception {
		int k=0;
		Connection con=connect();
		connection.connector().ps=con.prepareStatement(query);
		log.log(priority, "Running query : "+query);
		k=connection.connector().ps.executeUpdate();
		log.log(priority, "Affected rows : "+k);
		if(commit){
			con.commit();
			log.log(priority, "Connection committed");
		}
		connection.connector().ps.close();
		return k;
	}
	protected int preparedUpdate(String query, Setter setter, boolean commit) throws Exception {
		int k=0;
		Connection con=connect();
		if(!verbose){
			log.log(priority, "Running query : "+query);
		}
		else{
			log.log(priority, "Running update : "+verboseLogger(query, setter));
		}
		connection.connector().ps=con.prepareStatement(query);
		if(setter!=null && !setter.isEmpty()){
			for(int i : setter.keySet()){
				connection.connector().ps.setObject(i, setter.get(i));
			}
		}
		k=connection.connector().ps.executeUpdate();
		log.log(priority, "Affected rows : "+k);
		if(commit){
			con.commit();
			log.log(priority, "Connection committed");
		}
		connection.connector().ps.close();
		setter.clear();
		return k;
	}
	protected boolean callProcedure(String query, Setter setter, boolean commit) throws Exception {
		boolean k=false;
		Connection con=connect();
		if(!verbose){
			log.log(priority, "Calling procedure : "+query);
		}
		else{
			log.log(priority, "Calling procedure : "+verboseLogger(query, setter));
		}
		connection.connector().cs=con.prepareCall(query);
		log.log(priority, "Affected rows : "+k);
		if(setter!=null && !setter.isEmpty()){
			for(int i : setter.keySet()){
				connection.connector().cs.setObject(i, setter.get(i));
			}
		}
		k=connection.connector().cs.execute();
		log.log(priority, "Affected rows : "+k);
		if(commit){
			con.commit();
			log.log(priority, "Connection committed");
		}
		connection.connector().cs.close();
		setter.clear();
		return k;
	}
	protected Clob createClob() throws Exception {
		return connect().createClob();
	}
	protected Blob createBlob() throws Exception {
		return connect().createBlob();
	}
	protected Savepoint setSavepoint() throws Exception {
		return connection.connector().getConnection().setSavepoint();
	}
	protected Savepoint setSavepoint(String name) throws Exception {
		return connection.connector().getConnection().setSavepoint(name);
	}
	protected void rollback(Savepoint savepoint) throws Exception{
		connection.connector().getConnection().rollback(savepoint);
	}
}
