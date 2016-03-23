package com.cigital.insecurepay.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.TransactionVO;
import com.cigital.insecurepay.VOs.TransferFundsVO;
import com.google.gson.Gson;

import java.util.List;


public class ActivityHistory extends Fragment {

    private Gson gson = new Gson();
    private TextView tvAccountNumber;
    private EditText etAccountNumber;
    private TransferFundsVO transferfundsVO;

    public ActivityHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewObj = inflater.inflate(R.layout.fragment_home, container, false);
        etAccountNumber = (EditText) viewObj.findViewById(R.id.etAccountNumber);
        tvAccountNumber = (TextView) viewObj.findViewById(R.id.tvAccountNumber);
        return inflater.inflate(R.layout.fragment_activity_history, container, false);
    }

}

class TransactionAdapter extends ArrayAdapter {

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
            convertView = inflater.inflate(R.layout.row, null);
        }

        TextView tvActivityHistory_Description;
        TextView tvActivityHistory_Date;
        TextView tvActivityHistory_FinalAmount;
        TextView tvActivityHistory_TransactionAmount;

        tvActivityHistory_Description = (TextView) convertView.findViewById(R.id.tvActivityHistory_Description);
        tvActivityHistory_Date = (TextView) convertView.findViewById(R.id.tvActivityHistory_Date);
        tvActivityHistory_FinalAmount = (TextView) convertView.findViewById(R.id.tvActivityHistory_FinalAmount);
        tvActivityHistory_TransactionAmount = (TextView) convertView.findViewById(R.id.tvActivityHistory_TransactionAmount);

        //tvActivityHistory_Description.setText(transactionVOList.get(position).getDescription());
        //tvActivityHistory_Date.setText(transactionVOList.get(position).getDate());
        //tvActivityHistory_FinalAmount.setText(transactionVOList.get(position).getFinalAmount());
        //tvActivityHistory_TransactionAmount.setText(transactionVOList.get(position).getTransactionAmount());

        return convertView;
    }
}
