package com.example.java_sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Person;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private Intent intent;
    CustomAdapter adapter;
    public ArrayList<GridItem> items;
    public GridItem gd;
    public ArrayList<personalDialog> pDialog;
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

    DatabaseReference ds;
    //List<String> ds;
    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);


        gridView = findViewById(R.id.gridView);
        //pDialog = new ArrayList<personalDialog>();
        items = new ArrayList<>();
        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

       intent = getIntent();
       p_tittle = intent.getStringExtra("tittle");
       key = intent.getStringExtra("key");
       count = intent.getIntExtra("count", 5);


        @SuppressLint("InflateParams")
        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);
        header_goal.setText(p_tittle);
        adapter = new CustomAdapter(items);
        gridView.setAdapter(adapter);
        //gridView.setAdapter(adapter);

        //ds = new ArrayList<>();
        ds = databaseReference.child(uid).child("dialog_personal").child(key).child("도장판");

        //Log.d("TAG", ds);

        ReadPersonalDialog2();

        //adapter.notifyDataSetChanged();


    }
    heroine
    //다이얼로그 저장된 함수 가져오기
    private void ReadPersonalDialog2() {

        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //items.clear();
                //Toast.makeText(custom_p_goal_click.this, "실패", Toast.LENGTH_SHORT).show();
                //items.clear();
                //GenericTypeIndicator<ArrayList<GridItem>> t = new GenericTypeIndicator<ArrayList<GridItem>>() {};
                //adapter.items = items;
                //adapter.notifyDataSetChanged();
                //Toast.makeText(custom_p_goal_click.this, t, Toast.LENGTH_SHORT).show();
                //Toast.makeText(custom_p_goal_click.this, stringBuilder,Toast.LENGTH_LONG).show();
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(custom_p_goal_click.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }


}