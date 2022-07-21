package com.example.java_sticker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.java_sticker.Account.Login;
import com.example.java_sticker.Account.Signup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class StartScreen extends AppCompatActivity {

    Button signin;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);


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
