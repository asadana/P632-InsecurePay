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

public class TransactionAdapter extends ArrayAdapter {

    private List<TransactionVO> transactionVOList;
    private int resource;
    private LayoutInflater inflater;

    public TransactionAdapter(Context context, int resource, List<TransactionVO> transactionVOList) {
        super(context, resource, transactionVOList);
        this.resource = resource;
        this.transactionVOList = transactionVOList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.transaction_format, null);
        }

        int type;
        TextView tvActivityHistory_Description;
        TextView tvActivityHistory_Date;
        TextView tvActivityHistory_FinalAmount;
        TextView tvActivityHistory_TransactionAmount;

        tvActivityHistory_Description = (TextView) convertView.findViewById(R.id.tvActivityHistory_Description);
        tvActivityHistory_Date = (TextView) convertView.findViewById(R.id.tvActivityHistory_Date);
        tvActivityHistory_FinalAmount = (TextView) convertView.findViewById(R.id.tvActivityHistory_FinalAmount);
        tvActivityHistory_TransactionAmount = (TextView) convertView.findViewById(R.id.tvActivityHistory_TransactionAmount);

        tvActivityHistory_Description.setText(transactionVOList.get(position).getDescription());
        tvActivityHistory_Date.setText(transactionVOList.get(position).getDate());
        tvActivityHistory_FinalAmount.setText("$" + transactionVOList.get(position).getFinalAmount());
        tvActivityHistory_TransactionAmount.setText("$" + transactionVOList.get(position).getTransactionAmount());
        type = transactionVOList.get(position).getType();
        if (type == 1) //type 1 is debit and type 2 is credit
            tvActivityHistory_TransactionAmount.setTextColor(Color.RED);
        else
            tvActivityHistory_TransactionAmount.setTextColor(Color.BLUE);

        return convertView;
    }
}
