package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.Logging;
import com.cigital.insecurepay.service.BO.TransactionBO;

/**
 * ActivityHistoryDao extends BaseDao.
 * This class handles the querying and retrieving Account History for the user.
 */
public class ActivityHistoryDao extends BaseDao {

	/**
	 * ActivityHistoryDao is a parameterized constructor to initialize the 
	 * super class.
	 */
	public ActivityHistoryDao(Connection connectionObj) {
		super(connectionObj);
	}

	/**
	 * getActivityHistory is a function that queries the database for the given
	 * account number.
	 * 
	 * @param	accountNo	Contains the account number given by the user.
	 * 
	 * @return	List<TransactionBO>	Returns a List of {@link TransactionBO} object.
	 */
	public List<TransactionBO> getActivityHistory(int accountNo)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		List<TransactionBO> resultList = new ArrayList<TransactionBO>();
		ResultSet resultSet = null;
		Date transferDate;
		
		List<Object> params = new ArrayList<Object>();
		params.add(accountNo);
		params.add(accountNo);
		
		Logging.logger.debug("getActivityHistory: Querying the database.");
		
		resultSet = querySql(Queries.GET_ACTIVITY_HISTORY, params);
		
		// while loop traverses resultSet and grabs individual details
		// for each object.
		while (resultSet.next()) {
			TransactionBO transaction = new TransactionBO();
			transaction.setDescription(resultSet.getString("transfer_details"));
			transferDate = resultSet.getDate("transfer_date");
			transaction.setDate(transferDate.toString());
			transaction.setFinalAmount(resultSet.getFloat("final_amount"));
			transaction.setTransactionAmount(resultSet.getFloat("transfer_amount"));
			transaction.setType(resultSet.getInt("type"));
			resultList.add(transaction);
		}
		close();

		return resultList;
	}
}
