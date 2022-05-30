package com.example.java_sticker.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.java_sticker.personal.MainActivity;
import com.example.java_sticker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String TAG = null;
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

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        //자동 로그인
//        if (user != null) {
//            // User is signed in (getCurrentUser() will be null if not signed in)
//            Intent i = new Intent(Login.this, MainActivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//        } else {
//            // User is signed out
//            Log.d(TAG, "onAuthStateChanged:signed_out");
//        }

        login_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_user_id.getText().toString().trim();
                String pwd = login_user_password.getText().toString().trim();

                firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(Login.this, task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });


        register_change.setOnClickListener(view -> {
            Intent intent2 = new Intent(Login.this, Signup.class);
            startActivity(intent2);
        });
    }
}