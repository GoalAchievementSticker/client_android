package com.example.java_sticker.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.java_sticker.CustomProgress;
import com.example.java_sticker.Custom_g_item_adapter;
import com.example.java_sticker.Custom_pAdapter;
import com.example.java_sticker.Custom_p_item_adapter;
import com.example.java_sticker.GridItem;
import com.example.java_sticker.GroupDialog;
import com.example.java_sticker.R;
import com.example.java_sticker.gGoalInput.First;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Objects;

public class FragJoin extends Fragment {
    private View view;
    Toolbar toolbar;
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

    //그리드뷰 데이터 저장
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference ds;
    GridItem gd;


    //ProgressBar circleProgressBar;
    com.example.java_sticker.CustomProgress customProgress;


    TextView custom_g_goal_tittle;

    ArrayList<GridItem> items;

    //dialog
    ArrayList<GroupDialog> gDialog;
    Dialog custom_dialog;

    //Recycler
    RecyclerView g_goal_recycler;
    Custom_g_item_adapter gAdapter;
    View cv;


    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try{
            UploadData();
        }catch (NullPointerException e){e.printStackTrace();}
        assert inflater != null;
        view = inflater.inflate(R.layout.fragjoin, container, false);



//        toolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        //다이얼로그 선언
        gDialog = new ArrayList<>();


        //리사이클러뷰 선언
        g_goal_recycler = view.findViewById(R.id.recyclerview_g_goal);
        customProgress = view.findViewById(R.id.customProgress);
        custom_g_goal_tittle = view.findViewById(R.id.custom_g_goal_tittle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        g_goal_recycler.setLayoutManager(linearLayoutManager);

        //리사이클러뷰 어댑터 연결
        gAdapter = new Custom_g_item_adapter(getContext(), gDialog);
        g_goal_recycler.setAdapter(gAdapter);
        //리사이클러뷰 클릭했을때 나오는 도장판 연결
        items = new ArrayList<>();
//        adapter = new Custom_pAdapter(this, items);

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
        try{
            UploadData();
        }catch (NullPointerException e){e.printStackTrace();}
        return view;

    }

    private void UploadData() {
        //fragment에서 데이터 받기

        Bundle bundle=this.getArguments();

        int count = Objects.requireNonNull(bundle).getInt("count");
        int limit = bundle.getInt("limit");
        String goal = bundle.getString("goal");
        String auth = bundle.getString("auth");
        String cate = bundle.getString("cate");


        Log.d("count", String.valueOf(count));
        Log.d("limit", String.valueOf(limit));
        Log.d("goal", goal);
        Log.d("auth", auth);
        Log.d("cate", cate);


        //파이어베이스 저장
        //고유키와 함께 저장히기 위한 장치
        String key = databaseReference.push().getKey();
        assert key != null;
        DatabaseReference keyRef = databaseReference.child(uid).child("dialog_group").child(key);
        //list에 추가
        GroupDialog groupDialog = new GroupDialog(count, goal, limit, auth, key, 0, cate);  //수,목표,제한,인증,카테고리
        gDialog.add(groupDialog);

        gAdapter.notifyDataSetChanged();


        //생성된 레코드 파이어베이스 저장
        keyRef.setValue(groupDialog);


        //도장판 gridview 데이터 저장
        ds = databaseReference.child(uid).child("goal_group").child(key).child("도장판");
        for (int i = 0; i < count; i++) {
            items.add(addGoal(i));
        }

        new Handler().postDelayed(this::ReadGroupDialog, 400);


    }

    private void toggleFab() {
        ObjectAnimator.ofFloat(fab_g, View.ROTATION, 0f, 45f).start();
        Fragment First=new First();
        // 플로팅 액션 버튼 열기
        //startActivity(new Intent(getContext(), First.class));
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.group_layout, First);
        //프래그먼트 트랜잭션을 백스택에 push
        transaction.addToBackStack(null);
        //프래그먼트 상태전환 최적화
        transaction.setReorderingAllowed(true);
        transaction.commit();

    }


    //다이얼로그 저장된 함수 가져오기
    private void ReadGroupDialog() {
        databaseReference.child(uid).child("dialog_group").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gDialog.clear();
                // Log.d("TAG", String.valueOf(snapshot));
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    GroupDialog read_g = dataSnapshot.getValue(GroupDialog.class);
                    assert read_g != null;
                    read_g.key = key;
                    Log.d("TAG", read_g.getgTittle());
                    Log.d("TAG", String.valueOf(read_g.getgCount()));
                    //Log.d("TAG", String.valueOf(read_g.getgGoal()));
                    //Log.d("TAG", key);

                    gDialog.add(read_g);

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

    //도장판칸 생성
    private GridItem addGoal(int i) {
        // Handle any errors
        storageRef.child("not.png").getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Got the download URL for 'plus.png'
                    gd = new GridItem(String.valueOf(i), uri.toString());
                    ds.child(String.valueOf(i)).setValue(gd);

                }).addOnFailureListener(Throwable::printStackTrace);

        return gd;
    }

}
