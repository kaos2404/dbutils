package com.kaos.dbUtils;

import java.sql.SQLException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Datasource implements DBConnection{
	private String SERVER_IP;
	private String JNDI_NAME;
	private String CONTEXT_FACTORY;
	private Connector connector;
	private DataSource dataSource;
	public Datasource(String SERVER_IP, String JNDI_NAME, String CONTEXT_FACTORY){
		if(SERVER_IP.equals("blank") || JNDI_NAME.equals("blank")){
			throw new RuntimeException("Please set SERVER_IP and JNDI_NAME to class");
		}
		this.CONTEXT_FACTORY=CONTEXT_FACTORY;
		this.SERVER_IP=SERVER_IP;
		this.JNDI_NAME=JNDI_NAME;
		connector=new Connector();
	}
	public void createConnection() throws Exception{
		if(dataSource==null){
			Hashtable<String, String> env=new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, CONTEXT_FACTORY);
			env.put(Context.PROVIDER_URL, SERVER_IP);
			dataSource=(DataSource)(new InitialContext(env)).lookup(JNDI_NAME);
		}
		connector.con=dataSource.getConnection();
		connector.con.setAutoCommit(false);
		connector.setFree(false);
	}
	public void freeResources() throws Exception{
		connector.freeResources();
	}
	public Connector connector(){
		return connector;
	}
	public void setFree(boolean isFree){
		connector.setFree(isFree);
	}
	public boolean isLive() throws SQLException {
		return connector.isLive();
	}
	public boolean isFree() {
		return connector.isFree();
	}
	public boolean closeConnection() throws Exception{
		return connector.closeConnection();
	}
	public void shutdownPool() {
		@SuppressWarnings("rawtypes")
		Class[] classes={String.class, String.class, String.class};
		Object[] args={SERVER_IP,JNDI_NAME,CONTEXT_FACTORY};
		FactoryPlugin.callFactory("shutdownDatasourcePool", classes, args);
	}
	public void initialize() {
		
	}
}