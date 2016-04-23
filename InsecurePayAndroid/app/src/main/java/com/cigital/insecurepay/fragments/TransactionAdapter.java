package com.cigital.insecurepay.fragments;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.TransactionVO;

import java.util.List;

/**
 * TransactionAdapter extends {@link ArrayAdapter}.
 * This class is called for making a {@link View} for each transfer in the transaction list.
 */
public class TransactionAdapter extends ArrayAdapter {

    /*
     * transactionVOList is a list of {@link TransactionVO} objects
     */
    private List<TransactionVO> transactionVOList;

    private int resource;
    private String dollarSymbol = "$";

    private LayoutInflater layoutInflater;

    /**
     * TransactionAdapter parametrized constructor
     *
     * @param context   Contains the context of the parent activity.
     * @param resource  Contains the resource of the parent.
     * @param transactionVOList     Contains the list of {@link TransactionVO}.
     */
    public TransactionAdapter(Context context, int resource,
                              List<TransactionVO> transactionVOList) {
        super(context, resource, transactionVOList);

        this.resource = resource;
        this.transactionVOList = transactionVOList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * getView is an overridden function that is called to retrieve the {@link View} object.
     *
     * @param position        Contains the position of the view
     * @param convertView     Contains the view object being used
     * @param viewGroupParent Contains the {@link ViewGroup} of the parent.
     * @return View     Return the new {@link View} object.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroupParent) {

        // If condition checks if the convertView being passed is null
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.transaction_format, null);
        }

        int type;

        //Text placeholders
        TextView tvActivityHistory_Description;
        TextView tvActivityHistory_Date;
        TextView tvActivityHistory_FinalAmount;
        TextView tvActivityHistory_TransactionAmount;

        //Adapter uses convertView to recycle old view objects no longer being used
        tvActivityHistory_Description = (TextView) convertView
                .findViewById(R.id.tvActivityHistory_Description);
        tvActivityHistory_Date = (TextView) convertView
                .findViewById(R.id.tvActivityHistory_Date);
        tvActivityHistory_FinalAmount = (TextView) convertView
                .findViewById(R.id.tvActivityHistory_FinalAmount);
        tvActivityHistory_TransactionAmount = (TextView) convertView
                .findViewById(R.id.tvActivityHistory_TransactionAmount);

        //Transaction details are placed in appropriate textView holders
        tvActivityHistory_Description.setText(transactionVOList.get(position).getDescription());
        tvActivityHistory_Date.setText(transactionVOList.get(position).getDate());
        tvActivityHistory_FinalAmount.setText(dollarSymbol +
                transactionVOList.get(position).getFinalAmount());
        tvActivityHistory_TransactionAmount.setText(dollarSymbol +
                transactionVOList.get(position).getTransactionAmount());
        type = transactionVOList.get(position).getType();

        // type 1 is debit and type 2 is credit
        if (type == 1)
            tvActivityHistory_TransactionAmount.setTextColor(Color.RED);
        else
            tvActivityHistory_TransactionAmount.setTextColor(Color.BLUE);

        return convertView;
    }
}
