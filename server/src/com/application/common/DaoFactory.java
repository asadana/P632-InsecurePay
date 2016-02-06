package com.application.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.application.dao.BaseDao;
import com.mysql.jdbc.Connection;

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
