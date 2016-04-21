package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.BO.TransactionBO;

public class ActivityHistoryDao extends BaseDao {

	public ActivityHistoryDao(Connection conn) {
		super(conn);
	}

	public List<TransactionBO> getActivityHistory(int accountNo)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		List<TransactionBO> resultList = new ArrayList<TransactionBO>();
		ResultSet rs = null;
		Date transferdt;
		List<Object> params = new ArrayList<Object>();
		params.add(accountNo);
		params.add(accountNo);
		rs = querySql(Queries.GET_ACTIVITY_HISTORY, params);
		
		while (rs.next()) {
			TransactionBO transaction = new TransactionBO();
			transaction.setDescription(rs.getString("transfer_details"));
			transferdt = rs.getDate("transfer_date");
			transaction.setDate(transferdt.toString());
			transaction.setFinalAmount(rs.getFloat("final_amount"));
			transaction.setTransactionAmount(rs.getFloat("transfer_amount"));
			transaction.setType(rs.getInt("type"));
			resultList.add(transaction);

		}
		close();

		return resultList;
	}

}
