package com.example.uas_mobprog.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.uas_mobprog.Adapter.TransactionAdapter;
import com.example.uas_mobprog.Class.Transaction;
import com.example.uas_mobprog.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class TransactionFragment extends Fragment {

    //Firebase
    DatabaseReference db;

    ListView listView;
    ArrayList <Transaction> transactionList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Firebase
        db = FirebaseDatabase
                .getInstance("https://uas-mobprog-c2a7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Order");

        listView = view.findViewById(R.id.transaction_list);
        transactionList = new ArrayList<>();

        listView.setClickable(true);
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            String combined, kind;

            //Getting value of the class that was clicked
            Transaction tr = transactionList.get(i);

            kind = tr.id.charAt(0) == 'W' ? "Wifi" : tr.id.charAt(0) == 'P' ? "Phone" : "Water";

            //Combining as one String
            combined = kind + "/" + tr.id + "/" + tr.name + "/" + tr.number + "/" + tr.amount + "/" + tr.via + "/" + tr.imageId;

            //Passing value as bundle to another fragment
            Bundle args = new Bundle();
            args.putString("combine", combined);
            //Fragment inside Fragment Transaction
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            //Change the new TransactionFragment()
            AmountFragment aFragment = new AmountFragment();
            aFragment.setArguments(args);
            ft.replace(R.id.frameLayout, aFragment).addToBackStack("myFragment").commit();
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Empty the array list
                transactionList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Transaction transaction = dataSnapshot.getValue(Transaction.class);
                    transactionList.add(transaction);
                }

                Collections.reverse(transactionList);

                TransactionAdapter adapter = new TransactionAdapter(requireActivity(), transactionList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }
}