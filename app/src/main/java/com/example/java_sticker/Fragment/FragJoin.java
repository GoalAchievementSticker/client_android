package com.example.java_sticker.Fragment;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
        custom_g_goal_tittle =view.findViewById(R.id.custom_g_goal_tittle);
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
                g_user_name.setText(name+"님");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cv= inflater.inflate(R.layout.custom_g_goal, container, false);
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
        ReadPersonalDialog();
        return view;

    }

    private void toggleFab() {
        // 플로팅 액션 버튼 열기
        showDialog();
        ObjectAnimator.ofFloat(fab_g, View.ROTATION, 0f, 45f).start();
    }

    private void showDialog() {
        custom_dialog = new Dialog(getContext());
        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.custom_g_dialog);
        //다이얼로그를 보여준다
        custom_dialog.show();


        //다이얼로그에 있는 텍스트들 연결
        EditText sticker_count = (EditText) custom_dialog.findViewById(R.id.sticker_count);
        EditText sticker_goal = (EditText) custom_dialog.findViewById(R.id.sticker_goal);
        EditText authentication = custom_dialog.findViewById(R.id.auth);
        EditText limitNOP = custom_dialog.findViewById(R.id.limitNOP);

        //예스, 노 버튼 연결
        Button noBtn = custom_dialog.findViewById(R.id.noBtn);
        Button yesBtn = custom_dialog.findViewById(R.id.yesBtn);

        custom_dialog.setOnDismissListener(view -> {
                    ObjectAnimator.ofFloat(fab_g, View.ROTATION, 45f, 0f).start();
                }
        );
        //노버튼 클릭시 다이얼로그 닫기
        noBtn.setOnClickListener(view -> {
            custom_dialog.dismiss();
        });

        //예스 버튼 클릭시 다이얼로그 동작
        yesBtn.setOnClickListener(view -> {

            //다이얼로그에 입력한값 형 변환
            int vi = Integer.parseInt(sticker_count.getText().toString());
            String goal = sticker_goal.getText().toString();
            String auth = authentication.getText().toString();
            String limit = limitNOP.getText().toString();


            //파이어베이스 저장
            //고유키와 함께 저장히기 위한 장치
            String key = databaseReference.push().getKey();
            assert key != null;
            DatabaseReference keyRef = databaseReference.child(uid).child("dialog_group").child(key);
            //list에 추가
            GroupDialog groupDialog = new GroupDialog(vi,goal, limit, auth, key, 0);  //수,목표,제한,인증,
            gDialog.add(groupDialog);

            gAdapter.notifyDataSetChanged();


            //생성된 레코드 파이어베이스 저장
            keyRef.setValue(groupDialog);


            //도장판 gridview 데이터 저장
            ds = databaseReference.child(uid).child("goal_group").child(key).child("도장판");
            for (int i = 0; i < vi; i++) {
                items.add(addGoal(i));
            }

            new Handler().postDelayed(this::ReadPersonalDialog, 400);


            custom_dialog.dismiss();


        });
    }

    //다이얼로그 저장된 함수 가져오기
    private void ReadPersonalDialog() {
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
