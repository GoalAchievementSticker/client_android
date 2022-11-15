package com.example.java_sticker.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.java_sticker.Group_main;
import com.example.java_sticker.gGoalInput.gGoalInputActivity;
import com.example.java_sticker.group.Custom_g_item_adapter;
import com.example.java_sticker.group.g_GridItem;
import com.example.java_sticker.group.GroupDialog;
import com.example.java_sticker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class w_FragJoin extends Fragment {
    private View view;
    //FAB
    boolean isFabOpen;
    FloatingActionButton fab_g;

    //툴바유저네임
    TextView g_user_name;
    //g_goal_iv
    ImageView g_pics;

    //FB
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();
    DatabaseReference databaseReference = firebaseDatabase.getReference("GroupDialog");
    DatabaseReference categoryReference = firebaseDatabase.getReference("Category");

    //그리드뷰 데이터 저장
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference ds;
    g_GridItem gd;


    //ProgressBar circleProgressBar;
    com.example.java_sticker.CustomProgress customProgress;


    TextView custom_g_goal_tittle;

    ArrayList<g_GridItem> items;

    //dialog
    ArrayList<GroupDialog> gDialog;
    Dialog custom_dialog;


    //Recycler
    RecyclerView g_goal_recycler;
    Custom_g_item_adapter gAdapter;
    View cv;

    Group_main group_main;


    //Tablayout
    private ArrayList<String> tabNames = new ArrayList<>();
    TabLayout tabLayout;

    ViewPager viewPager;


    //public static final String MyPREFERENCES = "MyPrefs";


//    public w_FragJoin() {
//        // Required empty public constructor
//    }

    @Override
    public void onResume() {
        super.onResume();
        ReadGroupDialog();
    }

    //class의 객체 생성
    //=인수 없는 생성자 호출
    public static w_FragJoin newInstance() {
        return new w_FragJoin();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        group_main = null;
    }


    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.w_fragjoin, container, false);

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
//        assert user != null;
        if (user != null) {
            uid = user.getUid();
        }

        //다이얼로그 선언
        gDialog = new ArrayList<>();


        //리사이클러뷰 선언
        g_goal_recycler = view.findViewById(R.id.recyclerview_g_goal);
        customProgress = view.findViewById(R.id.customProgress_g_goal_c);
        custom_g_goal_tittle = view.findViewById(R.id.custom_g_goal_tittle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        g_goal_recycler.setLayoutManager(linearLayoutManager);

        //리사이클러뷰 어댑터 연결
        gAdapter = new Custom_g_item_adapter(getContext(), gDialog);
        g_goal_recycler.setAdapter(gAdapter);
        //리사이클러뷰 클릭했을때 나오는 도장판 연결
        items = new ArrayList<g_GridItem>();



        //툴바 유저 네임 설정
        profile_databaseReference.child("user").child(uid).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                g_user_name = view.findViewById(R.id.userN);
                g_user_name.setText(name + "님");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cv = inflater.inflate(R.layout.custom_g_goal, container, false);
        //그룹방 이미지(일단 프사로 설정) -> 어댑터에서 적용함
        profile_databaseReference.child("user").child(uid).child("profileImageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              /*  String uri = snapshot.getValue(String.class);
                g_pics = cv.findViewById(R.id.g_pic);
                Log.d("g_pics", String.valueOf(g_pics));
                Log.d("프사 url",uri);
                Glide.with(view)
                        .load(uri)
                        .into(g_pics);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //FAT
        fab_g = view.findViewById(R.id.fab_g);
        isFabOpen = false; // Fab 버튼 default는 닫혀있음

        //FAB 클릭 시
        fab_g.setOnClickListener(view -> toggleFab());


        //항상 카드뷰 읽어오기
        ReadGroupDialog();
        return view;

    }



    private void toggleFab() {


        startActivity(new Intent(getActivity(), gGoalInputActivity.class));

    }


    //다이얼로그 저장된 함수 가져오기
    private void ReadGroupDialog() {
        databaseReference.child(uid).child("dialog_group").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gDialog.clear();
                List<String> uid = new ArrayList<>();
                //Log.d("TAG", String.valueOf(snapshot));
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    GroupDialog read_g = dataSnapshot.getValue(GroupDialog.class);
                    assert read_g != null;
                    read_g.key = key;

                    if(read_g.isClose() == false){
                        gDialog.add(read_g);
                    }


                }
                gAdapter.notifyDataSetChanged();
                g_goal_recycler.setAdapter(gAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
