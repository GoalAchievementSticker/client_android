package com.example.java_sticker.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class setup_name extends Fragment {
    private TextView updateOk;
    private EditText newName;

    //FB
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();
    DatabaseReference namePath;

    Toolbar setup_toolbar;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup_name, container, false);

        updateOk = view.findViewById(R.id.updateOk);
        newName = view.findViewById(R.id.updatedName);


        //파이어베이스 로그인 유저 가져오기기
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        
        //툴바 뒤로가기
        setup_toolbar = (Toolbar) view.findViewById(R.id.setup_profile_img_toolbar);
        setup_toolbar.setNavigationOnClickListener(view1 -> requireActivity().onBackPressed());

        //확인 버튼 클릭시 이름 바꾸기
        updateOk.setOnClickListener(view12 -> {
            String newName_=newName.getText().toString();
            if(!newName_.isEmpty()){
                namePath = profile_databaseReference.child("user").child(uid).child("userName");
                namePath.setValue(newName_);
                Log.d("new", newName_);
                startActivity(new Intent(getContext(), Group_main.class));
                Toast.makeText(getContext(), "이름을 변경했습니다", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            }

        });
        return view;
    }
}
