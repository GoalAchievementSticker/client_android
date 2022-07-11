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
import com.example.java_sticker.Fragment.Notifications;
import com.example.java_sticker.Fragment.Routin;
import com.example.java_sticker.Fragment.SearchCategory;
import com.example.java_sticker.Fragment.Study;
import com.example.java_sticker.Fragment.setup;
import com.example.java_sticker.Fragment.setup_pofile_img;
import com.example.java_sticker.group.CardviewFactor;
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
    Fragment fragment_nofi;
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
        fragment_nofi=new Notifications();

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
        }else if(index == 7){
            getSupportFragmentManager().beginTransaction().replace(R.id.group_layout, fragment_nofi).addToBackStack(null).commit();
        }

    }


}