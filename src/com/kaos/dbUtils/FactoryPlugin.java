package com.kaos.dbUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class FactoryPlugin {
	protected static Object callFactory(String methodName, @SuppressWarnings("rawtypes") Class[] classes, Object[] args) {
		try {
			Class<?> clazz=Thread.currentThread().getContextClassLoader().loadClass("com.kaos.dbFactory.DBFactory");
			Method m = clazz.getDeclaredMethod(methodName, classes);
			return m.invoke(null, args);
		}
		catch(ClassNotFoundException e){
			throw new RuntimeException("DBFactory is not available");
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} 
		return null;
	}
	protected static Object getDBConnection(String methodName, @SuppressWarnings("rawtypes") Class[] classes, Object[] args) throws ClassNotFoundException {
		try {
			Class<?> clazz=Thread.currentThread().getContextClassLoader().loadClass("com.kaos.dbFactory.DBFactory");
			Method m = clazz.getDeclaredMethod(methodName, classes);
			return m.invoke(null, args);
		}
		catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
