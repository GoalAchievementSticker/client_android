package com.example.java_sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button register_check;
    EditText user_id;
    EditText user_password;
    EditText user_password_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        register_check = (Button) findViewById(R.id.register_check);

        register_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signUp();
            }
        });
    }

    private void signUp(){
        user_id = (EditText) findViewById(R.id.user_id);
        user_password = (EditText) findViewById(R.id.user_password);
        user_password_check = (EditText) findViewById(R.id.user_password_check);

        String id = user_id.getText().toString();
        String password = user_password.getText().toString();
        String password_check = user_password_check.getText().toString();


        if(id.length()>0 && password.length()>0 && password_check.length()>0){
            if(password.equals(password_check)){
                mAuth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this, "성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Signup.this,Login.class);
                            startActivity(intent);
                            finish();

                        }else{
                            if (task.getException().toString()!=null){
                                Toast.makeText(Signup.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }else{
                Toast.makeText(Signup.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Signup.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}