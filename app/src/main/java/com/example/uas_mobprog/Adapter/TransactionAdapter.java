package com.example.uas_mobprog.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uas_mobprog.Class.Transaction;
import com.example.uas_mobprog.R;

import java.util.ArrayList;

public class TransactionAdapter extends ArrayAdapter <Transaction> {

    public TransactionAdapter(@NonNull Context context, ArrayList<Transaction> transactionList) {
        super(context, R.layout.transaction_item, transactionList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Transaction transaction = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_item, parent, false);
        }

        ImageView transaction_picture = convertView.findViewById(R.id.transaction_picture);
        TextView transaction_name = convertView.findViewById(R.id.transaction_name);

        transaction_picture.setImageResource(transaction.imageId);
        transaction_name.setText(transaction.id);

        return convertView;
    }
}
