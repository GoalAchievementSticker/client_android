package com.example.java_sticker.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;
import com.example.java_sticker.group.GroupDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragMypage extends Fragment {
    private View view;

    Toolbar mypage_toolbar;
    TextView mypage_goal_j;
    TextView mypage_goal_w;
    TextView mypage_goal_close;
    TextView user_email;
    Button mypage_setup;
    Button mypage_logout;

    CircleImageView mypage_image;
    TextView mypage_name;

    //참여중
    int j_goal;
    //대기중
    int w_goal;
    //완료
    int goal_close;

    Group_main group_main;

    //FB
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        group_main = (Group_main) getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        group_main = null;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragmypage, container, false);

        mypage_toolbar = (Toolbar) view.findViewById(R.id.mypage_toolbar);
        mypage_goal_j = (TextView) view.findViewById(R.id.mypage_goal_j);
        mypage_goal_w = (TextView) view.findViewById(R.id.mypage_goal_w);
        mypage_goal_close = (TextView) view.findViewById(R.id.mypage_goal_close);
        user_email=view.findViewById(R.id.user_email);

        mypage_setup = (Button) view.findViewById(R.id.mypage_setup_button);
        mypage_logout = (Button) view.findViewById(R.id.mypage_logout_button);

        mypage_image = (CircleImageView) view.findViewById(R.id.mypage_image);
        mypage_name = (TextView) view.findViewById(R.id.mypage_name);

        //파이어베이스 로그인 유저 가져오기기
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        //이름,이메일,이미지 가져오기
        profile_databaseReference.child("user").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("userName").getValue(String.class);
                String image = snapshot.child("profileImageUrl").getValue(String.class);
                String email=snapshot.child("userEmail").getValue(String.class);
                mypage_name.setText(name + " 님");
                user_email.setText(email);
                Glide.with(view).load(image).into(mypage_image);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //참가중, 대기중, 완료 가져오기

        GroupUserdata();
        new Handler().postDelayed(() -> {
            mypage_goal_close.setText(String.valueOf(goal_close));
            mypage_goal_j.setText(String.valueOf(j_goal));
            mypage_goal_w.setText(String.valueOf(w_goal));
        },400);


        //각 버튼 클릭 -> 프래그먼트 연결 or 기능연결
        //설정(프래그먼트 전환)
        mypage_setup.setOnClickListener(view -> group_main.onFragmentChange(5));

        //로그아웃
        mypage_logout.setOnClickListener(view -> {
                FirebaseAuth.getInstance().signOut();
                //로그인 프래그먼트로 전환 해줘야함...(만들기..!)
        });

        return view;
    }

    private void GroupUserdata(){
        databaseReference.child(uid).child("dialog_group").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                j_goal = 0;
                w_goal = 0;
                goal_close = 0;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    GroupDialog groupDialog = dataSnapshot.getValue(GroupDialog.class);
                    assert  groupDialog !=null;
                    groupDialog.key = key;

                    if(groupDialog.isClose() == true && (groupDialog.getgGoal() != groupDialog.getgCount())){
                        //참가중
                        j_goal++;
                    }
                    if(groupDialog.isClose() == false){
                        //대기중
                        w_goal++;
                    }
                    //완료
                    if(groupDialog.getgGoal() == groupDialog.getgCount()){
                        goal_close++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
