package com.example.java_sticker.Account;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.personal.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private static final String TAG = null;
    private EditText login_user_id;
    private EditText login_user_password;
    private TextView changePw;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login_check = (Button) findViewById(R.id.login_check);
        TextView signup = findViewById(R.id.signup);
        login_user_id = (EditText) findViewById(R.id.login_user_id);
        login_user_password = (EditText) findViewById(R.id.login_user_password);
        changePw = findViewById(R.id.change_pw);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //유저가 null 이 아니라면 Group_main 화면으로 이동
        if (user != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent i = new Intent(Login.this, Group_main.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

        login_check.setOnClickListener(view -> {
            String email = login_user_id.getText().toString().trim();
            String pwd = login_user_password.getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(Login.this, task -> {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Login.this, Group_main.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(e->
                    Toast.makeText(Login.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_LONG).show());
        });


        signup.setOnClickListener(view -> {
            Intent intent2 = new Intent(Login.this, Signup.class);
            startActivity(intent2);
        });

        changePw.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("이메일 주소 입력하기");
            LinearLayout linearLayout = new LinearLayout(this);
            final EditText emailet = new EditText(this);

            //가입한 이메일 입력
            emailet.setHint("example@naver.com");
            emailet.setMinEms(16);
            emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            linearLayout.addView(emailet);
            linearLayout.setPadding(10, 10, 10, 10);
            builder.setView(linearLayout);

            // 입력 버튼 누르면 이메일 전송
            builder.setPositiveButton("입력", (dialog, which) -> {
                String email = emailet.getText().toString().trim();
                beginRecovery(email);
            });

            builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
            builder.create().show();

        });
    }

    private void beginRecovery(String email) {
        ProgressDialog loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("이메일 발송 중....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // 비밀번호 재설정 이메일 발송
        // 이메일에 접속해서 새 비밀번호 입력
        // 새 비밀번호로 로그인 가능
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            loadingBar.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(Login.this, "비밀번호 재설정 이메일이 "+email+"로 발송됐습니다", Toast.LENGTH_LONG).show();
            } 
        }).addOnFailureListener(e -> {
            loadingBar.dismiss();
            Toast.makeText(Login.this, "이메일 형식을 확인해주세요", Toast.LENGTH_LONG).show();
        });
    }
}