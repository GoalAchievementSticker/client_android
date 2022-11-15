package com.example.java_sticker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.java_sticker.Account.Login;
import com.example.java_sticker.Account.Signup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class StartScreen extends AppCompatActivity {

    private static final String TAG =null ;
    Button signin;
    Button signup;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        //유저가 null이 아니라면 Login 화면으로 이동
        if (user != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent i = new Intent(this, Login.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

        signin=findViewById(R.id.start_signIn);
        signup=findViewById(R.id.start_signUp);

        //로그인 클릭 시
        signin.setOnClickListener(view ->{
            startActivity(new Intent(this, Login.class));
        });

        //회원가입 클릭 시
        signup.setOnClickListener(view ->{
            startActivity(new Intent(this, Signup.class));
        } );
    }

}
