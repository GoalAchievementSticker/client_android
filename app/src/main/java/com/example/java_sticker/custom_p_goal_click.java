package com.example.java_sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private Intent intent;
    CustomAdapter adapter;
    public ArrayList<GridItem> items;
    GridViewWithHeaderAndFooter gridView;
    //RecyclerView gridView;
    Dialog custom_dialog;
    String p_tittle;
    String key;
    String uid;
    int count;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("personalDialog");
    TextView sticker_img;
    DatabaseReference ds;
    String goal_key;
    String dss;
    //List<String> ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);


        items = new ArrayList<>();
        adapter = new CustomAdapter(this,items);
        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        //pDialog = new ArrayList<personalDialog>();
        //items = new ArrayList<>();
        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

       intent = getIntent();
       p_tittle = intent.getStringExtra("tittle");
       key = intent.getStringExtra("key");
       count = intent.getIntExtra("count", 5);


        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);
        //adapter = new CustomAdapter(items);
        //gridView.setAdapter(adapter);
        //gridView.setAdapter(adapter);

        //ds = new ArrayList<>();
        ds =databaseReference.child(uid).child("goal_personal").child(key).child("도장판");

        DatabaseReference ke = databaseReference.child(uid).child("goal_personal");

        //Log.d("TAG", ds.child(key).getKey());

        header_goal.setText(p_tittle);
        gridView.setAdapter(adapter);

        String vl = "도장판";


        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ReadPersonalDialog2();
                }
                else{
                    for(int i=0; i<count; i++){
                        items.add(addGoal());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        });


        adapter.notifyDataSetChanged();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ReadPersonalDialog2();
            }
        },400);


        //Log.d("TAG", String.valueOf(adapter));

        //adapter.notifyDataSetChanged();

        //adapter.notifyDataSetChanged();


    }

    private GridItem addGoal(){
        DatabaseReference goalRef = databaseReference.child(uid).child("goal_personal").child(key).child("도장판");
        String td = goalRef.push().getKey();
        GridItem gd = new GridItem(td, "test");
        goalRef.child(td).setValue(gd);
        return gd;
    }

    public String readRof(){
        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dss = snapshot.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return dss;
    }

    //다이얼로그 저장된 함수 가져오기
    private void ReadPersonalDialog2() {

        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    GridItem gd = dataSnapshot.getValue(GridItem.class);
                    gd.goal_id = key;
                   // Log.d("TAG", key);

                    items.add(gd);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(custom_p_goal_click.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }


}