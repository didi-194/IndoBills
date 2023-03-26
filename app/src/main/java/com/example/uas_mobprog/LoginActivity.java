package com.example.uas_mobprog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText et_username, et_password;
    Button  btn_login;

    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //No Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        //If user has logged in
        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }

        btn_login.setOnClickListener(view -> loginUser());

    }

    void loginUser(){
        String email, pass;

        email = et_username.getText().toString();
        pass = et_password.getText().toString();

        if (email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Email / Pass Cannot be Empty !", Toast.LENGTH_SHORT).show();
            return;
        }
        //To use Firebase Email Auth (Change to RTD ?)
        email = email  + "@mail.com";

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               Toast.makeText(this, "User Logged in", Toast.LENGTH_SHORT).show();
               finish();
               startActivity(new Intent(this, HomeActivity.class));
           }
           else {
               Toast.makeText(this, "Failed Logging in", Toast.LENGTH_SHORT).show();
               et_username.setText("");
               et_password.setText("");
           }
        }).addOnFailureListener(e -> {
            String []error_msg = Objects.requireNonNull(e.getMessage()).split("\\.");
            Toast.makeText(this, "" + error_msg[0], Toast.LENGTH_SHORT).show();
        });

    }

}