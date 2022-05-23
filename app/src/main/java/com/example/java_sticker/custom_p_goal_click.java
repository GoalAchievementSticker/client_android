package com.example.java_sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private Intent intent;
    CustomAdapter adapter;
    GridItem gd;
    private ArrayList<GridItem> items = null;
    GridViewWithHeaderAndFooter gridView;
    //RecyclerView gridView;
    String p_tittle;
    String key;
    String uid;
    int count;
    int goal_count;
    int p;

    //파이어베이스
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("personalDialog");
    private ImageView sticker_img;
    DatabaseReference ds;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    private ValueEventListener postListener;



    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.goal_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");



        // Create a storage reference from our app
        sticker_img = findViewById(R.id.sticker_img);
        items = new ArrayList<>();
        adapter = new CustomAdapter(this, items);
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();
        intent = getIntent();
        p_tittle = intent.getStringExtra("tittle");
        key = intent.getStringExtra("key");
        count = intent.getIntExtra("count", 5);
        goal_count = intent.getIntExtra("goal_count",0);



        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);



        ds = databaseReference.child(uid).child("goal_personal").child(key).child("도장판");
        header_goal.setText(p_tittle);


        // 도장판이 존재한다면 읽어오기, 없다면 for문 만큼 생성
        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (int i = 0; i < count; i++)
                        ReadPersonalDialog2(i);
                } else {
                    for (int i = 0; i < count; i++) {
                        items.add(addGoal(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


        adapter.notifyDataSetChanged();


        //그리드뷰 각 칸 클릭시, 데이터 수정
        gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            //Log.d("TAG", String.valueOf(i));
            ds.child(String.valueOf(i)).child("goal_id").setValue("GOALID");
            storageRef.child("sprout.png").getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        ds.child(String.valueOf(i)).child("test").setValue(uri.toString());
                       // Log.d("TAG", uri.toString());

                    }).addOnFailureListener(Throwable::printStackTrace);
            //도장을 클릭했다면 프로그래스바 숫자를 늘린다
            goal_count();

        });


        //0으로초기화 방지
        ReadPersonalDialog();
        gridView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

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


    //그리드뷰 도장판 저장된 함수 가져오기
    private void ReadPersonalDialog2(int i) {

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
               // sticker_img.setImageResource(0);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    GridItem gridItem = dataSnapshot.getValue(GridItem.class);


                    //test
                    assert gridItem != null;
                    gridItem.goal_id = String.valueOf(i);


                    items.add(gridItem);

                }
                adapter.notifyDataSetChanged();
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(custom_p_goal_click.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        };
        ds.addValueEventListener(postListener);
    }

    //프로그래스바 숫자 늘리기
    private void goal_count(){
        ReadPersonalDialog();
        databaseReference.child(uid).child("dialog_personal").child(key).child("pGoal").setValue(++p);
    }

    //다이얼로그 저장된 함수 가져오기
    private int ReadPersonalDialog() {
        databaseReference.child(uid).child("dialog_personal").child(key).child("pGoal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               p = snapshot.getValue(Integer.class);
               Log.d("TAG", String.valueOf(p));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                //Toast.makeText(MainActivity.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

        return p;

    }
}