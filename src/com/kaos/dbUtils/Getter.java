package com.kaos.dbUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Time;
import java.util.LinkedHashMap;

/**
 * Class to store and format the result set of a database query
 * @author Kaos
 * @version v4.1
 * @since 14 April 2018
 */
@SuppressWarnings("serial")
public class Getter extends LinkedHashMap<Object, Object>{
	public String get(Object key){
		if(super.get(key)==null)
			return null;
		return String.valueOf(super.get(key));
	}
	public String getString(Object key){
		return String.valueOf(super.get(key));
	}
	public BigDecimal getBigDecimal(Object key){
		return (BigDecimal)(super.get(key));
	}
	public Blob getBlob(Object key){
		return (Blob)(super.get(key));
	}
	public Clob getClob(Object key){
		return (Clob)(super.get(key));
	}
	public Date getDate(Object key){
		return (Date)(super.get(key));
	}
	public float getFloat(Object key){
		return (Float)(super.get(key));
	}
	public int getInt(Object key){
		return (Integer)(super.get(key));
	}
	public long getLong(Object key){
		return (Long)(super.get(key));
	}
	public NClob getNClob(Object key){
		return (NClob)(super.get(key));
	}
	public Time getTime(Object key){
		return (Time)(super.get(key));
	}
	public short getShort(Object key){
		return (Short)(super.get(key));
	}
}
