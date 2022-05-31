package com.example.java_sticker.gGoalInput;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.java_sticker.Fragment.FragInputCheck;
import com.example.java_sticker.Fragment.FragJoin;
import com.example.java_sticker.Fragment.w_FragJoin;
import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.group.GroupDialog;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Third extends Fragment implements View.OnClickListener {

    ToggleButton exercise;
    ToggleButton study;
    ToggleButton hobby;
    ToggleButton routine;
    Bundle bundle = new Bundle();
    Fragment FragInputCheck = new FragInputCheck();
    Fragment w_fragJoin = new w_FragJoin();


    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");
    DatabaseReference categoryReference = firebaseDatabase.getReference("Category");


    private View view;
    //viewpager
    //private ViewPager viewPager;

//    SharedPreferences sharedpreferences;
//    public static final String MyPREFERENCES = "MyPrefs";


    String cate_;
    public Third(){}

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        assert inflater != null;
        view = inflater.inflate(R.layout.custom_g_input3, container, false);


        // w_FragJoin fj = ((w_FragJoin) this.getParentFragment());
//        if (fj != null) {
//            viewPager = fj.getView().findViewById(R.id.viewPager);
//        }


        exercise = view.findViewById(R.id.exercise);
        study = view.findViewById(R.id.study);
        hobby = view.findViewById(R.id.hobby);
        routine = view.findViewById(R.id.routine);


        Button preBtn = view.findViewById(R.id.preBtn);
        Button OKBtn = view.findViewById(R.id.OKBtn);


        exercise.setOnClickListener(this);
        hobby.setOnClickListener(this);
        routine.setOnClickListener(this);
        study.setOnClickListener(this);

        //sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        preBtn.setOnClickListener(view -> {
                    assert getFragmentManager() != null;
                    getFragmentManager().popBackStack();
                }
        );
        OKBtn.setOnClickListener(view -> {
            if (!(exercise.isChecked() || hobby.isChecked() || routine.isChecked() || study.isChecked()))
                Toast.makeText(getContext(), "카테고리를 선택해주세요", Toast.LENGTH_SHORT).show();
            else
                ToFragjoin();
        });

        return view;
    }

    private void ToFragjoin() {

        // startActivity(new Intent(getContext(), Group_main.class));


        //fragment에서 데이터 받기
        Bundle bundle_g = this.getArguments();


        //First
        assert bundle_g != null;
        int cnt = bundle_g.getInt("count");
        String goal = bundle_g.getString("goal");
        int limit = bundle_g.getInt("limit");

        //Second
        String auth = bundle_g.getString("auth");


        //First에서 받은 데이터
        bundle.putInt("count", cnt);
        bundle.putString("goal", goal);
        bundle.putInt("limit", limit);

        //Second 정보
        bundle.putString("auth", auth);


        Log.d("여기", String.valueOf(cnt));
        Log.d("여기", String.valueOf(goal));
        Log.d("여기", String.valueOf(limit));
        Log.d("여기", String.valueOf(auth));


        w_fragJoin.setArguments(bundle);


//        //고유키와 함께 저장히기 위한 장치
//        String key = databaseReference.push().getKey();
//        assert key != null;
//        DatabaseReference keyRef = databaseReference.child(uid).child("dialog_group").child(key);
//        DatabaseReference categoryRef = categoryReference.child(cate_).child(key);
//        //list에 추가
//        GroupDialog groupDialog = new GroupDialog(cnt, goal, limit, auth, key, 0, cate_, 1);  //수,목표,제한,인증,카테고리
//       // gDialog.add(groupDialog);
//        Log.d("여기2", String.valueOf(cnt));
//        Log.d("여기2", String.valueOf(goal));
//        Log.d("여기2", String.valueOf(limit));
//        Log.d("여기2", String.valueOf(auth));
//        Log.d("여기2", String.valueOf(cate_));
//        Log.d("여기2", String.valueOf(groupDialog));
//
//       //gAdapter.notifyDataSetChanged();
//
//
//        //생성된 레코드 파이어베이스 저장
//        keyRef.setValue(groupDialog);
//        //uid 정보값 push()키로 저장하기
//        keyRef.child("uid").push().setValue(uid);
//
//        //카테고리 레코드 파이어베이스에도 저장
//        categoryRef.setValue(groupDialog);
//        categoryRef.child("uid").push().setValue(uid);

        //startActivity(new Intent(getContext(),Group_main.class));

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getActivity().finish();
//            }
//        },400);


        // FragJoin fragJoin=((FragJoin)this.getParentFragment());
//        assert fragJoin != null;

//        try {
//            viewPager.setCurrentItem(0, true);
//        }catch(NullPointerException e){
//            e.printStackTrace();
//        }

        w_FragJoin w_fragJoin = new w_FragJoin();
        FragJoin fragJoin = new FragJoin();


        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.input_framelayout, w_fragJoin);
        //프래그먼트 트랜잭션을 백스택에 push
        transaction.addToBackStack(null);
        //프래그먼트 상태전환 최적화
        transaction.setReorderingAllowed(true);
        transaction.commit();

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        //입력한값 형 변환
        String ex = exercise.getText().toString();
        String st = study.getText().toString();
        String hob = hobby.getText().toString();
        String rout = routine.getText().toString();

        // SharedPreferences.Editor editor = sharedpreferences.edit();


        switch (v.getId()) {
            case R.id.exercise:
                if (exercise.isChecked()) {
                    exercise.setChecked(true);
                    Toast.makeText(getContext(), "운동 카테고리 클릭", Toast.LENGTH_SHORT).show();
                    bundle.putString("cate", ex);

                    cate_=ex;
                    Log.d("여기", ex);
//                    editor.putString("cate", ex);
//                    editor.commit();

                    study.setChecked(false);
                    routine.setChecked(false);
                    hobby.setChecked(false);
                } else {
                    exercise.setChecked(false);
                }

                break;

            case R.id.study:
                if (study.isChecked()) {
                    study.setChecked(true);
                    Toast.makeText(getContext(), "공부 카테고리 클릭", Toast.LENGTH_SHORT).show();
                    bundle.putString("cate", st);
//                    editor.putString("cate", st);
//                    editor.commit();
                    cate_=ex;
                    routine.setChecked(false);
                    hobby.setChecked(false);
                    exercise.setChecked(false);
                } else {
                    study.setChecked(false);
                }

                break;
            case R.id.routine:
                if (routine.isChecked()) {
                    routine.setChecked(true);
                    Toast.makeText(getContext(), "루틴 카테고리 클릭", Toast.LENGTH_SHORT).show();
                    bundle.putString("cate", rout);

//                    editor.putString("cate", rout);
//                    editor.commit();
                    cate_=ex;
                    hobby.setChecked(false);
                    exercise.setChecked(false);
                    study.setChecked(false);
                } else {
                    routine.setChecked(false);
                }

                break;
            case R.id.hobby:
                if (hobby.isChecked()) {
                    hobby.setChecked(true);
                    Toast.makeText(getContext(), "취미 카테고리 클릭", Toast.LENGTH_SHORT).show();
                    bundle.putString("cate", hob);

//                    editor.putString("cate", hob);
//                    editor.commit();
                    cate_=ex;
                    exercise.setChecked(false);
                    study.setChecked(false);
                    routine.setChecked(false);
                } else {
                    hobby.setChecked(false);
                }

                break;

            default:
                break;
        }
    }

}

