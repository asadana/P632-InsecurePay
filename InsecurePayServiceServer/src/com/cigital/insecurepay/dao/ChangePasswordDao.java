package com.cigital.insecurepay.dao;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.Logging;
import com.cigital.insecurepay.service.BO.ChangePasswordBO;

/**
 * ChangePasswordDao extends {@link BaseDao}. This class queries the database to
 * update the user's password.
 */
public class ChangePasswordDao extends BaseDao {

	/**
	 * ChangePasswordDao is a parameterized constructor to initialize the super
	 * class.
	 */
	public ChangePasswordDao(Connection conn) {
		super(conn);
	}

	/**
	 * ChangePasswordDao is a function that queries the database and updates the
	 * password for the user.
	 * 
	 * @param changePasswordBOObj
	 *            Contains an object of ChangePassword class.
	 * 
	 * @return Boolean Return a boolean value depending if the query was
	 *         successful.
	 */
	public Boolean changePassword(ChangePasswordBO changePasswordBOObj) {

		Boolean password_changed = false;

		List<Object> params = new ArrayList<Object>();
		params.add(changePasswordBOObj.getPassword());
		params.add(changePasswordBOObj.getUsername());

		Logging.logger.debug("Querying the database.");
		try {
			int count = updateSql(Queries.UPDATE_PASSWORD, params);

			if (count >= 1) {
				Logging.logger.debug("Password change successful.");
				password_changed = true;
			} else {
				Logging.logger.debug("Password change failed.");
				password_changed = false;
			}
			close();
			
			return password_changed;
		} catch (SQLException e) {
			Logging.logger.error(e);
			return password_changed;
		}
	}
}
