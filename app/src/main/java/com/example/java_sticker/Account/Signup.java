package com.example.java_sticker.Account;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.java_sticker.R;
import com.example.java_sticker.model.User;
import com.example.java_sticker.retrofit.RetrofitService;
import com.example.java_sticker.retrofit.UserApi;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {

//    private FirebaseAuth mAuth;
//    FirebaseStorage mstorage;
//    private StorageReference storageReference;
//    private FirebaseDatabase mDatabase;

    Button register_check;
    EditText user_id;
    EditText user_password;
    EditText user_password_check;
    EditText user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//
//        //초기화
//        mAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance();
//        mstorage = FirebaseStorage.getInstance();
//        storageReference = mstorage.getReference();

        register_check = (Button) findViewById(R.id.register_check);

        register_check.setOnClickListener(view -> addUser());
    }

    public void addUser() {
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);


        user_id = (EditText) findViewById(R.id.user_id);
        user_password = (EditText) findViewById(R.id.user_password);
        user_password_check = (EditText) findViewById(R.id.user_password_check);
        user_name = (EditText) findViewById(R.id.user_name);

        String id = user_id.getText().toString();
        String password = user_password.getText().toString();
        String password_check = user_password_check.getText().toString();
        String name = user_name.getText().toString();

        User user = new User();
        user.setNickname(name);
        user.setEmail(id);
        user.setPassword(password);

        userApi.addUser(user)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(Signup.this, "save successful!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(Signup.this, "save fail!", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, "Error Occurred!", t);
                    }
                });
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.219.103:9080/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();


//        try {
//            UserApi service = RetrofitService.getClient().create(UserApi.class);
//            Call<User> call = service.addUser(map);
//            call.enqueue(mRetrofitCallback);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        //이메일 정규식
//        Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
//
//        if (id.length() > 0 && password.length() > 0 && password_check.length() > 0) {
//            if (pattern.matcher(id).matches()) {
//                if (password.equals(password_check)) {
//                    mAuth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(this, task -> {
//                        if (task.isSuccessful()) {
//                            final String uid = task.getResult().getUser().getUid();
//                            storageReference.child("profile_img").child("profile_green_or.png").getDownloadUrl()
//                                    .addOnSuccessListener(uri -> {
//                                        // Got the download URL for 'plus.png'
//                                        String path = uri.toString();
//
//                                        UserRegister userRegister = new UserRegister(name,path,id,uid);
//                                        userRegister.userName = name;
//                                        userRegister.uid = uid;
//                                        userRegister.profileImageUrl = path;
//                                        userRegister.userEmail=id;
//
//
//                                        mDatabase.getReference().child("user").child(uid).setValue(userRegister);
//                                    }).addOnFailureListener(Throwable::printStackTrace);
//
//                            Toast.makeText(Signup.this, "성공", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(Signup.this, Login.class);
//                            startActivity(intent);
//                            finish();
//
//                        } else {
//                            if (task.getException().toString() != null) {
//                                Toast.makeText(Signup.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                } else {
//                    Toast.makeText(Signup.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(Signup.this, "이메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(Signup.this, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
//        }
    }

//    // 통신 요청 및 응답 콜백
//    private final Callback<User> mRetrofitCallback = new Callback<User>() {
//
//        @Override
//        public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
//            if (TextUtils.isEmpty(user_name.getText()) ||
//                    TextUtils.isEmpty(user_id.getText()) ||
//                    TextUtils.isEmpty(user_password.getText())) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
//                AlertDialog dialog = builder.setMessage("공백이 있습니다")
//                        .setNegativeButton("확인", null)
//                        .create();
//                dialog.show();
//            } else {
//
//                if (response.isSuccessful()) {
//                    startToast("등록되었습니다");
////                    List<User> data = (List<User>) response.body();
////                    // 정상 응답
////                    Intent Main = new Intent(getApplicationContext(), MainActivity.class);
////                    startActivity(Main);
////
//
//                } else {
//                    if (response.code() == 404) {
//                        onFailure(call, new Throwable(response.raw().toString()));
//                    }
////                    startToast("등록 실패!");
////                   throw new RuntimeException(response.raw().toString());
//
//                }
//
//            }
//        }
//
//        @Override
//        public void onFailure(@NonNull Call<User> call, Throwable t) {
//            Log.d(TAG, "Fail msg : " + t.getMessage());
//            throw new RuntimeException(t.getMessage());
//
//        }
//    };
//
//    private void startToast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
}


