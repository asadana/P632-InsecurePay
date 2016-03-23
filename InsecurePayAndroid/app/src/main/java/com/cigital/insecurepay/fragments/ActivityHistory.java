package com.cigital.insecurepay.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.cigital.insecurepay.R;
import com.cigital.insecurepay.VOs.TransactionVO;

import java.util.ArrayList;
import java.util.List;


public class ActivityHistory extends Fragment {

    private ListView lvTransactionList;
    private EditText etAccountNumber;
    private TransactionAdapter adapter;

    public ActivityHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewObj = inflater.inflate(R.layout.fragment_activity_history, container, false);
        etAccountNumber = (EditText) viewObj.findViewById(R.id.etActivityHistory_AccountNo);
        lvTransactionList = (ListView) viewObj.findViewById(R.id.lvActivityHistory_transactionList);
        List result = new ArrayList<TransactionVO>();
        result.add(new TransactionVO("shopping", "25/02/92", 300, 5));
        result.add(new TransactionVO("test", "25/01/92", 305, 25));
        result.add(new TransactionVO("payment", "25/02/92", 330, 15));
        result.add(new TransactionVO("bill payment", "15/4/92", 345, 55));
        result.add(new TransactionVO("shopping", "25/02/92", 300, 5));
        result.add(new TransactionVO("test", "25/01/92", 305, 25));
        result.add(new TransactionVO("payment", "25/02/92", 330, 15));
        result.add(new TransactionVO("bill payment", "15/4/92", 345, 55));
        Log.i("Activity History", "calling transaction adapter");
        adapter = new TransactionAdapter(this.getContext(), R.layout.row, result);
        lvTransactionList.setAdapter(adapter);
        return viewObj;
    }

}

