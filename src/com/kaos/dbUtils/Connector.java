package com.kaos.dbUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connector{
	protected Connection con;
	protected ResultSet rs;
	protected PreparedStatement ps;
	protected CallableStatement cs;
	protected boolean isFree;
	protected Connector(){
		isFree=false;
	}
	protected Connection getConnection() {
		return con;
	}
	protected void setConnection(Connection con) {
		this.con=con;
	}
	protected void freeResources() throws Exception{
		if(con!=null && !con.isClosed()){
			isFree=true;
			con.close();
		}
		if(rs!=null && !rs.isClosed()){
			rs.close();
		}
		if(ps!=null && !ps.isClosed()){
			ps.close();
		}
	}
	protected boolean closeConnection() throws Exception{
		if(con!=null && !con.isClosed()){
			con.rollback();
			con.close();
			return true;
		}
		return false;
	}
	protected boolean isLive() throws SQLException {
		if(con!=null && !con.isClosed()){
			return true;
		}
		return false;
	}
	protected boolean isFree() {
		return isFree;
	}
	protected void setFree(boolean isFree) {
		this.isFree=isFree;
	}
}
