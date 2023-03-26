package com.example.uas_mobprog.Fragment;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uas_mobprog.Class.Transaction;
import com.example.uas_mobprog.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Random;

public class AmountFragment extends Fragment {

    String CHANNEL_ID = "1";
    int notificationId = 1;
    
    String combined, combine;
    String [] divide;
    int imageId;
    Boolean ovo_selected, gopay_selected;

    LinearLayout linear_pay, linear_success;

    TextView service_provider, service_number, service_name;
    ImageView service_picture;
    EditText et_amount;
    MaterialCardView cv_ovo, cv_gopay;
    Button btn_submit;

    //Firebase DB
    DatabaseReference db;

    //The Success Layout
    TextView idNumber, service_provider_success, service_number_success, service_amount_success, service_via_success;
    TextView tv_success, tv_detail, tv_transaction_number;
    ImageView img_map;


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Creating Notification Channel
        createNotificationChannel();

        //Dividing the String into array of String
        divide = combined != null ? combined.split(",") : null;

        service_provider = view.findViewById(R.id.service_provider);
        service_number = view.findViewById(R.id.service_number);
        service_name = view.findViewById(R.id.service_name);
        service_picture = view.findViewById(R.id.service_picture);
        cv_ovo = view.findViewById(R.id.cv_ovo);
        cv_gopay = view.findViewById(R.id.cv_gopay);
        btn_submit = view.findViewById(R.id.btn_submit);
        et_amount = view.findViewById(R.id.et_amount);
        linear_pay = view.findViewById(R.id.linear_pay);
        linear_success = view.findViewById(R.id.linear_success);

        //Firebase
        db = FirebaseDatabase
                .getInstance("https://uas-mobprog-c2a7a-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Order");

        //Success Layout
        idNumber = view.findViewById(R.id.idNumber);
        service_provider_success = view.findViewById(R.id.service_provider_success);
        service_number_success = view.findViewById(R.id.service_number_success);
        service_amount_success = view.findViewById(R.id.service_amount_success);
        service_via_success = view.findViewById(R.id.service_Via_success);
        tv_detail = view.findViewById(R.id.tv_detail);
        tv_success = view.findViewById(R.id.tv_success);
        tv_transaction_number = view.findViewById(R.id.tv_transaction_num);
        img_map = view.findViewById(R.id.img_map);

        //Getting called from Transaction Fragment
        if (combine != null){
            checkTransaction();
            return;
        }

        String provider = String.format("%s Provider : %s", divide[0], divide[1]);
        String number = String.format("%s Number : %s", divide[0], divide[2]);

        service_provider.setText(provider);
        service_number.setText(number);
        service_name.setText(divide[3]);
        service_picture.setImageResource(imageId);

        cv_ovo.setOnClickListener(v -> selectOvo());

        cv_gopay.setOnClickListener(v -> selectGopay());

        btn_submit.setOnClickListener(v ->{
            //Checking the user at least choose one payment method
            if (!gopay_selected && !ovo_selected) {
                Toast.makeText(getActivity(), "Pilih salah satu metode pembayaran", Toast.LENGTH_SHORT).show();
                return;
            }

            String amount = et_amount.getText().toString();
            StringBuilder read_amount = new StringBuilder();
            String id = generateId();

            //Making amount more readable
            int j = 0;
            for (int i = amount.length()-1; i >= 0; i--){
                if (j % 3 == 0){
                    read_amount.append(",");
                }
                read_amount.append(amount.charAt(i));
                j++;
            }

            read_amount.reverse();
            read_amount.append("-");

            //Open app according to user choice
            String appChoice = gopay_selected ? "com.gojek.app" : "ovo.id";
            String Choice = gopay_selected ? "Gopay" : "OVO";

            Intent launchIntent = requireActivity().getPackageManager().getLaunchIntentForPackage(appChoice);
            if (launchIntent != null) {
                startActivity(launchIntent);
            }
            else {
                Toast.makeText(getActivity(), "App not Installed", Toast.LENGTH_SHORT).show();
                //Open if Failed
                failedCase(null);
                return;
            }

            //Checking if network is available
            if (!isNetworkAvailable()){
                failedCase("Internet");
                return;
            }

            //Change Layout
            linear_pay.setVisibility(View.GONE);
            linear_success.setVisibility(View.VISIBLE);

            //Simulate Loading
            tv_success.setText("Please Wait...");
            tv_transaction_number.setText("Your Transaction's Still");
            idNumber.setText("Being Processed");
            service_provider_success.setVisibility(View.GONE);
            service_number_success.setVisibility(View.GONE);
            tv_detail.setVisibility(View.GONE);
            service_amount_success.setVisibility(View.GONE);
            service_via_success.setVisibility(View.GONE);

            //Creating object to insert to DB
            Transaction transaction = new Transaction(id, divide[1], divide[2], read_amount.toString(), Choice, imageId);
            //Inserting object into DB
            String db_id = db.push().getKey();
            assert db_id != null;
            db.child(db_id).setValue(transaction).addOnCompleteListener(task -> {
               if (task.isSuccessful()){
                   Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                   //Setting Visibility to visible
                   service_provider_success.setVisibility(View.VISIBLE);
                   service_number_success.setVisibility(View.VISIBLE);
                   tv_detail.setVisibility(View.VISIBLE);
                   service_amount_success.setVisibility(View.VISIBLE);
                   service_via_success.setVisibility(View.VISIBLE);

                   //Setting up the view
                   tv_success.setText("Success!");
                   idNumber.setText(id);
                   service_provider_success.setText(String.format("%s Provider : %s", divide[0], divide[1]));
                   service_number_success.setText(String.format("%s Number : %s", divide[0], divide[2]));
                   service_amount_success.setText(String.format("Amount : Rp. %s", read_amount));
                   service_via_success.setText(String.format("Via : %s", Choice));

                   //Make the Notification

                   NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), CHANNEL_ID)
                           .setSmallIcon(R.drawable.info)
                           .setContentTitle("Your Billing is Successfully Paid!")
                           .setContentText("You can check on the IndoBills' transaction list")
                           .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                           .setAutoCancel(true);

                   NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireActivity());

                   notificationManager.notify(notificationId, builder.build());
               }
               else {
                   Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
               }
            }).addOnFailureListener(e -> {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    private void checkTransaction() {
        //Change Layout
        linear_pay.setVisibility(View.GONE);
        linear_success.setVisibility(View.VISIBLE);

        //Order : kind, id, name, number, amount, via
        divide = combine.split("/");

        //Setting up title
        String bills = divide[0].equals("Wifi") ? "" : " Bills";
        service_name.setText(String.format("Pay your %s%s", divide[0], bills));
        service_picture.setImageResource(Integer.parseInt(divide[6]));

        //Setting up the view
        idNumber.setText(divide[1]);
        service_provider_success.setText(String.format("%s Provider : %s", divide[0], divide[2]));
        service_number_success.setText(String.format("%s Number : %s", divide[0], divide[3]));
        service_amount_success.setText(String.format("Amount : Rp. %s", divide[4]));
        service_via_success.setText(String.format("Via : %s", divide[5]));
    }

    @SuppressLint("SetTextI18n")
    private void failedCase(String cause) {

        //Changing Layout
        linear_pay.setVisibility(View.GONE);
        linear_success.setVisibility(View.VISIBLE);

        //Setting up the view
        tv_success.setText("Failed");
        tv_detail.setText("CS Contact");

        if (cause != null){
            tv_transaction_number.setText("Check Your Internet");
            idNumber.setText("Connection");
        }
        else {
            tv_transaction_number.setText("The Application is not");
            idNumber.setText("Installed");
        }

        service_provider_success.setText("Mobile : +1 8764 324");
        service_number_success.setText("Location : ");
        img_map.setVisibility(View.VISIBLE);
        service_amount_success.setVisibility(View.GONE);
        service_via_success.setVisibility(View.GONE);

        //Redirect to google map fragment
        img_map.setOnClickListener(view -> {
            //Fragment inside Fragment Transaction
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            //Change the new TransactionFragment()
            MapsFragment mFragment = new MapsFragment();
            ft.replace(R.id.frameLayout, mFragment).addToBackStack("myFragment").commit();
        });

    }

    private String generateId() {
        String id;

        char firstLetter = divide[0].equals("Water") ? 'A' : divide[0].charAt(0);
        id = firstLetter + random() + random() + random() + random() + random();

        return id;
    }

    private String random() {
        Random r = new Random();
        int low = 0;
        int high = 9;
        int res = r.nextInt(high-low) + low;
        return "" + res;
    }

    private void selectOvo() {
        if (ovo_selected){
            ovo_selected = false;
            cv_ovo.setStrokeWidth(0);
        }

        else if (gopay_selected){
            gopay_selected = false;
            ovo_selected = true;
            cv_gopay.setStrokeWidth(0);
            cv_ovo.setStrokeWidth(10);
        }

        else {
            ovo_selected = true;
            cv_ovo.setStrokeWidth(10);
        }

    }

    private void selectGopay() {
        if (gopay_selected){
            gopay_selected = false;
            cv_gopay.setStrokeWidth(0);
        }

        else if (ovo_selected){
            ovo_selected = false;
            gopay_selected = true;
            cv_gopay.setStrokeWidth(10);
            cv_ovo.setStrokeWidth(0);
        }

        else {
            gopay_selected = true;
            cv_gopay.setStrokeWidth(10);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting value from another fragment
        imageId = getArguments() != null ? getArguments().getInt("imageId") : 0;
        combined = getArguments() != null ? getArguments().getString("combined") : null;
        combine = getArguments() != null ? getArguments().getString("combine") : null;

        gopay_selected = false;
        ovo_selected = false;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amount, container, false);
    }
}