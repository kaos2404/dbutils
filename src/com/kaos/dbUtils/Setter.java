package com.kaos.dbUtils;

import java.util.LinkedHashMap;
/**
 * Class to store the objects to set to a statement
 * @author Kaos
 * @version v4.1
 * @since 14 April 2018
 */
@SuppressWarnings("serial")
public class Setter extends LinkedHashMap<Integer, Object>{
	public void set(Integer key, Object value){
		super.put(key, value);
	}
	@Override
	public Object put(Integer key, Object value){
		return super.put(key, value);
	}
}
