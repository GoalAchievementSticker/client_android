package com.example.java_sticker;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private Intent intent;
    CustomAdapter adapter;
    public ArrayList<GridItem> items;
    //public GridItem gd;
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
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    //List<String> ds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);


        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        //pDialog = new ArrayList<personalDialog>();
        items = new ArrayList<>();
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
        header_goal.setText(p_tittle);
        adapter = new CustomAdapter(items);
        gridView.setAdapter(adapter);
        //gridView.setAdapter(adapter);

        //ds = new ArrayList<>();
        DatabaseReference ds = databaseReference.child(uid).child("goal_personal").child(key).child("도장판");
        ds.keepSynced(true);
        //Log.d("TAG", ds);

        ds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Toast.makeText(custom_p_goal_click.this, "실패", Toast.LENGTH_SHORT).show();
                    items.clear();
                    GenericTypeIndicator<ArrayList<GridItem>> t = new GenericTypeIndicator<ArrayList<GridItem>>() {};
                    items = snapshot.getValue(t);
                    adapter.notifyDataSetChanged();
                    //Toast.makeText(custom_p_goal_click.this, stringBuilder,Toast.LENGTH_LONG).show();
                    //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(custom_p_goal_click.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });
        //ReadPersonalDialog2();

        //adapter.notifyDataSetChanged();


    }


    //다이얼로그 저장된 함수 가져오기
    private void ReadPersonalDialog2() {


    }

}