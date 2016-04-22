package com.cigital.insecurepay.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cigital.insecurepay.common.Queries;
import com.cigital.insecurepay.service.Logging;
import com.cigital.insecurepay.service.BO.AccountBO;
import com.cigital.insecurepay.service.BO.TransferFundsBO;

/**
 * TransferFundsDao extends {@link BaseDao}. This class handles transferring
 * funds from one user to another.
 */
public class TransferFundsDao extends BaseDao {

	/**
	 * TransferFundsDao is a parameterized constructor to initialize the super
	 * class.
	 */
	public TransferFundsDao(Connection conn) {
		super(conn);
	}

	/**
	 * transfer is a function that queries the database to transfer funds
	 * between two users.
	 * 
	 * @param transferFundsBO
	 *            Contains the details about the transfer in form of
	 *            {@link TransferFundsBO} object.
	 * 
	 * @return Boolean Returns a boolean object depending on if the transfer was
	 *         successful.
	 */
	public Boolean transfer(TransferFundsBO transferFundsBO) {

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

		Logging.logger.debug("transfer: Querying the database.");

		try {
			// Querying the database to transfer funds.
			int count = updateSql(Queries.INSERT_TRANSFER_TBL, params);

			if (count >= 1) {
				fundsTransferred = true;
				// Update sender's and receiver's current balance in account
				// table
				params = new ArrayList<Object>();
				params.add(fromAfterAmt);
				params.add(fromAccountBO.getAccNo());

				Logging.logger.debug("transfer: Updating account balance.");
				count = updateSql(Queries.UPDATE_ACCOUNT_BALANCE, params);

				params = new ArrayList<Object>();
				params.add(toAfterAmt);
				params.add(toAccountBO.getAccNo());
				count = updateSql(Queries.UPDATE_ACCOUNT_BALANCE, params);
			}
			return fundsTransferred;
		} catch (SQLException e) {
			Logging.logger.error("transfer: " + e);
			return fundsTransferred;
		}
	}
}
