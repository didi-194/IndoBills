package com.example.uas_mobprog.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uas_mobprog.R;

import java.util.Objects;

public class PayYourFragment extends Fragment {

    int pos;
    ImageView service_picture;
    TextView service_name, service_provider, service_number;
    EditText et_provider, et_number;
    Button btn_search;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Toast.makeText(getActivity(), "" + pos, Toast.LENGTH_SHORT).show();

        String [] name = {"Pay your Wifi", "Pay your Phone Bills", "Pay your Water Bills"};
        String [] services = {"Wifi", "Phone", "Water"};
        int [] imageId = {R.drawable.wifi, R.drawable.telephone, R.drawable.drop};

        service_picture = view.findViewById(R.id.service_picture);
        service_name = view.findViewById(R.id.service_name);
        service_provider = view.findViewById(R.id.service_provider);
        service_number = view.findViewById(R.id.service_number);
        et_provider = view.findViewById(R.id.et_provider);
        et_number = view.findViewById(R.id.et_number);
        btn_search = view.findViewById(R.id.btn_search);

        String provider = String.format("%s Provider", services[pos]);
        String number = String.format("%s Number", services[pos]);

        //Changing the name and logo according to the button pressed
        service_picture.setImageResource(imageId[pos]);
        service_name.setText(name[pos]);
        service_provider.setText(provider);
        service_number.setText(number);

        btn_search.setOnClickListener(v ->{
            String service_prov, service_num, combined;
            service_prov = et_provider.getText().toString();
            service_num = et_number.getText().toString();

            //Checking condition that the column cannot be empty
            if (service_prov.isEmpty() || service_num.isEmpty()){
                Toast.makeText(requireActivity(), "Provider and Number cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            //Combining provider and number to pass the value as string
            combined = services[pos] + "," + service_prov + ","+service_num + "," + name[pos];

            //Passing value as bundle to another fragment
            Bundle args = new Bundle();
            args.putInt("imageId", imageId[pos]);
            args.putString("combined", combined);
            //Fragment inside Fragment Transaction
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            //Change the new TransactionFragment()
            AmountFragment aFragment = new AmountFragment();
            aFragment.setArguments(args);
            ft.replace(R.id.frameLayout, aFragment).addToBackStack("myFragment").commit();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting the number value from another fragment
        pos = getArguments() != null ? getArguments().getInt("pos") : 0;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pay_your, container, false);
    }
}