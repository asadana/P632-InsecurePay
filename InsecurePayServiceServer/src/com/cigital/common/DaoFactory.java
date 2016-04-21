package com.cigital.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;

import com.cigital.dao.BaseDao;

/*
 * Instantiates the Dao and passes the connection to BaseDao
 */

public class DaoFactory {
	public static  <T  extends BaseDao> T getInstance(Class<T> cl, Connection conn)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		Constructor<T> cons = cl.getConstructor(Connection.class);
		return cons.newInstance(conn);
	}
}
