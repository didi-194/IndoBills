package com.example.uas_mobprog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uas_mobprog.Fragment.HomeFragment;
import com.example.uas_mobprog.Fragment.MapsFragment;
import com.example.uas_mobprog.Fragment.TransactionFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    Boolean isHome;

    TextView tv_home_list;
    CardView cv_back, cv_home_list, cv_logout;

    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //No Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        isHome = true;

        tv_home_list = findViewById(R.id.tv_home_list);

        cv_back = findViewById(R.id.cv_back);
        cv_home_list = findViewById(R.id.cv_home_list);
        cv_logout = findViewById(R.id.cv_logout);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Making Default Fragment
        checkIsHome();
        if (isHome){
            replaceFragment(new HomeFragment());
            cv_back.setVisibility(View.INVISIBLE);
        }

        cv_back.setOnClickListener(view -> {
            onBackPressed();
//            Toast.makeText(this, "BACK BUTTON PRESSED", Toast.LENGTH_SHORT).show();
        });

        cv_home_list.setOnClickListener(view -> {
            checkIsHome();
            if (isHome){
                isHome = false;
                replaceFragment(new TransactionFragment());
                tv_home_list.setText("Home");
                cv_back.setVisibility(View.VISIBLE);
            }
            else {
                isHome = true;
                replaceFragment(new HomeFragment());
                tv_home_list.setText("Transaction List");
                cv_back.setVisibility(View.INVISIBLE);
            }
        });

        cv_logout.setOnClickListener(view -> {
            mAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        });

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();

        }
        else{
            super.onBackPressed();
        }

        checkIsHome();
        if (isHome){
            tv_home_list.setText("Transaction List");
            cv_back.setVisibility(View.INVISIBLE);
        }
        else {
                tv_home_list.setText("Home");
                cv_back.setVisibility(View.VISIBLE);
        }
    }

    private void checkIsHome() {

        isHome = getSupportFragmentManager().findFragmentById(R.id.frameLayout) == null ||
                getSupportFragmentManager().findFragmentById(R.id.frameLayout) instanceof HomeFragment;
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).addToBackStack("myFragment").commit();

    }

}