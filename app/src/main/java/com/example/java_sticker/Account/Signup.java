package com.example.java_sticker.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.java_sticker.R;
import com.example.java_sticker.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseStorage mstorage;
    private StorageReference storageReference;
    private FirebaseDatabase mDatabase;

    Button register_check;
    EditText user_id;
    EditText user_password;
    EditText user_password_check;
    EditText user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //초기화
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mstorage = FirebaseStorage.getInstance();
        storageReference = mstorage.getReference();

        register_check = (Button) findViewById(R.id.register_check);

        register_check.setOnClickListener(view -> signUp());
    }

    private void signUp() {
        user_id = (EditText) findViewById(R.id.user_id);
        user_password = (EditText) findViewById(R.id.user_password);
        user_password_check = (EditText) findViewById(R.id.user_password_check);
        user_name = (EditText) findViewById(R.id.user_name);

        String id = user_id.getText().toString();
        String password = user_password.getText().toString();
        String password_check = user_password_check.getText().toString();
        String name = user_name.getText().toString();

        //이메일 정규식
        Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;

        if (id.length() > 0 && password.length() > 0 && password_check.length() > 0) {
            if (pattern.matcher(id).matches()) {
                if (password.equals(password_check)) {
                    mAuth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            final String uid = task.getResult().getUser().getUid();
                            storageReference.child("profile_img").child("profile_green_or.png").getDownloadUrl()
                                    .addOnSuccessListener(uri -> {
                                        // Got the download URL for 'plus.png'
                                        String path = uri.toString();

                                        UserRegister userRegister = new UserRegister();
                                        userRegister.userName = name;
                                        userRegister.uid = uid;
                                        userRegister.profileImageUrl = path;
                                        userRegister.userEmail=id;


                                        mDatabase.getReference().child("user").child(uid).setValue(userRegister);
                                    }).addOnFailureListener(Throwable::printStackTrace);

                            Toast.makeText(Signup.this, "성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Signup.this, Login.class);
                            startActivity(intent);
                            finish();

                        } else {
                            if (task.getException().toString() != null) {
                                Toast.makeText(Signup.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(Signup.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Signup.this, "이메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(Signup.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}
