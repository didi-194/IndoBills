package com.example.uas_mobprog.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uas_mobprog.Adapter.HomeAdapter;
import com.example.uas_mobprog.Class.Home;
import com.example.uas_mobprog.R;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    ArrayList <Home> homeList;

    ListView home_list;
    HomeAdapter homeAdapter;

    TextView tv_home_list;
    CardView cv_back;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //List View
        home_list = view.findViewById(R.id.home_list);
        String [] name = {"Pay your Wifi", "Pay your Phone Bills", "Pay your Water Bills"};
        int [] imageId = {R.drawable.wifi, R.drawable.telephone, R.drawable.drop};

        homeList = new ArrayList<>();

        for (int i = 0; i < name.length; i++){
            homeList.add(new Home(name[i], imageId[i]));
        }

        //Setting the Adapter
        homeAdapter = new HomeAdapter(getActivity(), homeList);

        home_list.setAdapter(homeAdapter);
        home_list.setClickable(true);
        home_list.setOnItemClickListener((adapterView, view1, i, l) -> {
            //Passing value as bundle to another fragment
            Bundle args = new Bundle();
            args.putInt("pos", i);
            //Fragment inside Fragment Transaction
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            //Change the new TransactionFragment()
            PayYourFragment pFragment = new PayYourFragment();
            pFragment.setArguments(args);
            ft.replace(R.id.frameLayout, pFragment).addToBackStack("myFragment").commit();

            tv_home_list = requireActivity().findViewById(R.id.tv_home_list);
            cv_back = requireActivity().findViewById(R.id.cv_back);

            tv_home_list.setText("Home");
            cv_back.setVisibility(View.VISIBLE);
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}