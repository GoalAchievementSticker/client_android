package com.example.java_sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/*도장판 그리드뷰*/
public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private Intent intent;
    private CustomAdapter adapter = null;
    ArrayList<GridItem> items;
    ArrayList<personalDialog> pDialog;
    private GridViewWithHeaderAndFooter gridView = null;
    String p_tittle;
    String key;
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);


        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        pDialog = new ArrayList<>();


        //리사이클러뷰 클릭했을때 나오는 도장판 연결
        items = new ArrayList<GridItem>();
        adapter = new CustomAdapter();


        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

       intent = getIntent();
       p_tittle = intent.getStringExtra("tittle");
       key = intent.getStringExtra("key");


        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        header_goal = (TextView) header.findViewById(R.id.header_goal);
        gridView.addHeaderView(header);
        header_goal.setText(p_tittle);
        gridView.setAdapter(adapter);

        ReadPersonalDialog();
    }


    //다이얼로그 저장된 함수 가져오기
    private void ReadPersonalDialog() {

        databaseReference.child(uid).child("dialog_personal").child(key).child("도장판").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    GridItem read_click = dataSnapshot.getValue(GridItem.class);
                    assert read_click != null;
                    assert key != null;
                    read_click.goal_id = Integer.parseInt(key);
                    Toast.makeText(custom_p_goal_click.this,key,Toast.LENGTH_SHORT).show();
                    //String tittle = read_p.getpTittle();
                    //Toast.makeText(MainActivity.this, tittle,Toast.LENGTH_SHORT).show();
                    items.add(read_click);
                }

                adapter.items = items;
                adapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(custom_p_goal_click.this, "불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        });

    }

}