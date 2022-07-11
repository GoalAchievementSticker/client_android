package com.example.java_sticker;


import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.java_sticker.Fragment.Exercise;
import com.example.java_sticker.Fragment.FragHome;
import com.example.java_sticker.Fragment.FragJoin;
import com.example.java_sticker.Fragment.FragMypage;
import com.example.java_sticker.Fragment.Hobby;
import com.example.java_sticker.Fragment.Routin;
import com.example.java_sticker.Fragment.SearchCategory;
import com.example.java_sticker.Fragment.Study;
import com.example.java_sticker.Fragment.setup;
import com.example.java_sticker.Fragment.setup_pofile_img;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

import de.hdodenhof.circleimageview.CircleImageView;


public class Group_main extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION_REQUEST_CODE = 1234;
    BottomNavigationView bottomNavigationView;

    Fragment fragment_home;
    Fragment fragment_group_join;
    Fragment fragment_mypage;
    Fragment fragment_study;
    Fragment fragment_hobby;
    Fragment fragment_routin;
    Fragment fragment_exercise;
    Fragment SearchCategory;
    Fragment setup;
    Fragment setup_profile_img;
    //Fragment DetailFragment;

    Toolbar toolbar;


    @RequiresApi(api = 33)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);// ActionBar 타이틀(앱이름) 감추기


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        //네비게이션연결 프래그먼트
        fragment_home = new FragHome();
        fragment_group_join = new FragJoin();
        fragment_mypage = new FragMypage();

        //카테고리 프래그먼트
        fragment_study = new Study();
        fragment_exercise = new Exercise();
        fragment_hobby = new Hobby();
        fragment_routin = new Routin();

        //마이페이지 각 버튼
        setup = new setup();
        setup_profile_img = new setup_pofile_img();

        //서치 툴바 작동하기
        SearchCategory = new SearchCategory();

        getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_home).commitAllowingStateLoss();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_home).commitAllowingStateLoss();
                    return true;
                case R.id.join_group:
                    getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_group_join)
                            .addToBackStack(String.valueOf(fragment_group_join))
                            .commitAllowingStateLoss();
                    return true;

                case R.id.mypage:
                    getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_mypage).commitAllowingStateLoss();
                    return true;
            }
            return true;
        });

        runtimeEnableAutoInit();
        deviceGroupUpstream();
        sendUpstream();
        logRegToken();
        askNotificationPermission();
    }



    public void runtimeEnableAutoInit() {
        // [START fcm_runtime_enable_auto_init]
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        // [END fcm_runtime_enable_auto_init]
    }

    public void deviceGroupUpstream() {
        // [START fcm_device_group_upstream]
        String to = "a_unique_key"; // the notification key
        AtomicInteger msgId = new AtomicInteger();
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(to)
                .setMessageId(String.valueOf(msgId.get()))
                .addData("hello", "world")
                .build());
        // [END fcm_device_group_upstream]
    }

    public void sendUpstream() {
        final String SENDER_ID = "YOUR_SENDER_ID";
        final int messageId = 0; // Increment for each
        // [START fcm_send_upstream]
        FirebaseMessaging fm = FirebaseMessaging.getInstance();
        fm.send(new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
                .setMessageId(Integer.toString(messageId))
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .build());
        // [END fcm_send_upstream]
    }

    private void subscribeTopics() {
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(Group_main.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END subscribe_topics]
    }

    private void logRegToken() {
        // [START log_reg_token]
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = "FCM Registration token: " + token;
                        Log.d(TAG, msg);
                        Toast.makeText(Group_main.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END log_reg_token]
    }

    @RequiresApi(33)
    // [START ask_post_notifications]
    private void askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
            // FCM SDK (and your app) can post notifications.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            // TODO: display an educational UI explaining to the user the features that will be enabled
            //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
            //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
            //       If the user selects "No thanks," allow the user to continue without notifications.
        } else {
            // Directly ask for the permission
            requestPermissions(new String[] { Manifest.permission.POST_NOTIFICATIONS }, NOTIFICATION_REQUEST_CODE);
        }
    }
    // [END ask_post_notifications]

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        // [START handle_ask_post_notifications_request]
        switch (requestCode) {
            case NOTIFICATION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
        }
    }




    //카테고리 프래그먼트 전환
    //0:공부, 1:운동, 2:취미, 3:루틴
    //마이페이지 설정 버튼
    //5:설정, 6:프로필편집
    public void onFragmentChange(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_study).addToBackStack(null).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_exercise).addToBackStack(null).commit();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_hobby).addToBackStack(null).commit();
        } else if (index == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_routin).addToBackStack(null).commit();
        } else if(index == 4){
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, SearchCategory).addToBackStack(null).commit();
        } else if(index == 5){
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, setup).addToBackStack(null).commit();
        }else if(index == 6){
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, setup_profile_img).addToBackStack(null).commit();
        }

    }


}