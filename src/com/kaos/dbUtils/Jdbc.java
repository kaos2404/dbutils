package com.kaos.dbUtils;

import java.sql.DriverManager;
import java.sql.SQLException;
public class Jdbc implements DBConnection{
	private String IP;
	private String USERNAME;
	private String PASSWORD;
	private String JDBC_CLASS;
	private String URL;
	private Connector connector;
	public Jdbc(String IP, String USERNAME, String PASSWORD, String URL, String JDBC_CLASS) {
		if(IP.equals("blank") || USERNAME.equals("blank") || PASSWORD.equals("blank")){
			throw new RuntimeException("Please set IP, USERNAME and PASSWORD to class");
		}
		this.IP = IP;
		this.USERNAME = USERNAME;
		this.PASSWORD = PASSWORD;
		this.URL=URL;
		this.JDBC_CLASS=JDBC_CLASS;
		connector=new Connector();
	}
	public void createConnection() throws Exception {
		Class.forName(JDBC_CLASS);
		connector.con=DriverManager.getConnection(URL+IP, USERNAME, PASSWORD);
		connector.con.setAutoCommit(false);
		connector.setFree(false);
	}
	public void freeResources() throws Exception{
		connector.freeResources();
	}
	public Connector connector(){
		return connector;
	}
	public boolean isLive() throws SQLException{
		return connector.isLive();
	}
	public boolean isFree(){
		return connector.isFree();
	}
	public void setFree(boolean isFree){
		connector.setFree(isFree);
	}
	public boolean closeConnection() throws Exception{
		return connector.closeConnection();
	}
	public void shutdownPool() {
		@SuppressWarnings("rawtypes")
		Class[] classes={String.class, String.class, String.class, String.class, String.class};
		Object[] args={IP, USERNAME, PASSWORD, URL, JDBC_CLASS};
		FactoryPlugin.callFactory("shutdownJdbcPool", classes, args);
	}
	@Override
	public void initialize() {

	}
}