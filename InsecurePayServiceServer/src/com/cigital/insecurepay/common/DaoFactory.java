package com.cigital.insecurepay.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import com.cigital.insecurepay.dao.BaseDao;

/**
 * DaoFactory is a class that instantiates the Dao 
 * and passes the connection to BaseDao class.
 */
public class DaoFactory {
	public static  <T  extends BaseDao> T getInstance(Class<T> classObj, 
														Connection connectionObj)
			throws 	InstantiationException, IllegalAccessException, 
					NoSuchMethodException, SecurityException, IllegalArgumentException, 
					InvocationTargetException {
		
		Constructor<T> constructorObj = classObj.getConstructor(Connection.class);
		return constructorObj.newInstance(connectionObj);
	}
}
