package com.example.java_sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Button login_check;
    private Button register_change;
    private EditText login_user_id;
    private EditText login_user_password;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_check = (Button) findViewById(R.id.login_check);
        register_change = (Button) findViewById(R.id.register_change);
        login_user_id = (EditText) findViewById(R.id.login_user_id);
        login_user_password = (EditText) findViewById(R.id.login_user_password);

        firebaseAuth = firebaseAuth.getInstance();


        login_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_user_id.getText().toString().trim();
                String pwd = login_user_password.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
        });


        register_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}