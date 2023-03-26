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

import com.example.uas_mobprog.Class.Home;
import com.example.uas_mobprog.R;

import java.util.ArrayList;

public class HomeAdapter extends ArrayAdapter<Home> {

    public HomeAdapter(Context context, ArrayList <Home> homeList) {
        super(context, R.layout.home_item, homeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Home home = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.service_picture);
        TextView name = convertView.findViewById(R.id.service_name);

        imageView.setImageResource(home.getImageId());
        name.setText(home.getName());

        return convertView;
    }
}
