package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.BO.AccountBO;
import com.cigital.insecurepay.service.BO.TransferFundsBO;

public class TransferFundsDao extends BaseDao {

	public TransferFundsDao(Connection conn) {
		super(conn);
	}

	public Boolean transfer(TransferFundsBO transferFundsBO)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {

		Boolean fundsTransferred = false;
		AccountBO toAccountBO = transferFundsBO.getToAccount();
		AccountBO fromAccountBO = transferFundsBO.getFromAccount();

		float fromAfterAmt = fromAccountBO.getAccountBalance()
				- transferFundsBO.getTransferAmount();
		float toAfterAmt = toAccountBO.getAccountBalance()
				+ transferFundsBO.getTransferAmount();

		List<Object> params = new ArrayList<Object>();
		params = new ArrayList<Object>();
		params.add(fromAccountBO.getAccNo());
		params.add(fromAccountBO.getCustNo());
		params.add(toAccountBO.getAccNo());
		params.add(toAccountBO.getCustNo());
		params.add(transferFundsBO.getTransferAmount());
		params.add(fromAccountBO.getAccountBalance());
		params.add(fromAfterAmt);
		params.add(toAccountBO.getAccountBalance());
		params.add(toAfterAmt);
		params.add(transferFundsBO.getTransferDetails());

		int count = updateSql(Queries.INSERT_TRANSFER_TBL, params);
		if (count >= 1) {
			fundsTransferred = true;
			// Update sender's and receiver's current balance in account table
			params = new ArrayList<Object>();
			params.add(fromAfterAmt);
			params.add(fromAccountBO.getAccNo());
			count = updateSql(Queries.UPDATE_ACCOUNT_BALANCE, params);

			params = new ArrayList<Object>();
			params.add(toAfterAmt);
			params.add(toAccountBO.getAccNo());
			count = updateSql(Queries.UPDATE_ACCOUNT_BALANCE, params);
		}

		return fundsTransferred;

	}

}
