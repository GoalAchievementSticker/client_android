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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

/*도장판 액티비티*/
public class custom_p_goal_click extends AppCompatActivity {

    private TextView header_goal;
    private CustomAdapter adapter = null;
    ArrayList<GridItem> items;
    ArrayList<personalDialog> pDialog;
    private GridViewWithHeaderAndFooter gridView = null;
    Dialog custom_dialog;
    String p_tittle;
    int p_count;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pgoal_click);


        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        adapter = new CustomAdapter();
        items = new ArrayList<GridItem>();
        pDialog = new ArrayList<personalDialog>();


        //Intent intent = getIntent();

        readPersonalDialog();


        //다이얼로그 값 불러오고 딜레이를 줘서 타이틀값 가져오기 해결
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //목표를 가져왔다면?
                if(p_tittle != null) {
                    // Header 추가 -> 반영
                    View header = getLayoutInflater().inflate(R.layout.header, null, false);
                    header_goal = (TextView) header.findViewById(R.id.header_goal);
                    gridView.addHeaderView(header);
                    header_goal.setText(p_tittle);
                    gridView.setAdapter(adapter);

                    try {


                        //다이얼로그 p_count 만큼 for문 돌려 도장판 배열칸 생성 성공
                        for (int i = 0; i < p_count; i++) {
                            items.add(new GridItem(i, R.drawable.heart));
                        }

                        adapter.items = items;

                        databaseReference.child("dialog_personal").child("2").child(p_tittle).child("goal").setValue(items).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(custom_p_goal_click.this, "생성", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(custom_p_goal_click.this, "실패", Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    adapter.notifyDataSetChanged();


                    custom_dialog.dismiss();
                }
            }
        },400);


//        gridView.setAdapter(adapter);
    }


    //다이얼로그 저장된 함수 가져오기
    private void readPersonalDialog(){
        databaseReference.child("dialog_personal").child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //다이얼로그 저장값 가져오기
                personalDialog read_p_dialog = snapshot.getValue(personalDialog.class);

                //저장된 클래스 get소환해서 값넣기
                p_tittle = read_p_dialog.getpTittle();
                p_count = read_p_dialog.getpCount();
                //목표값 가져왔는 확인하게끔 toast 메세지
                //Toast.makeText(MainActivity.this, p_tittle, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(custom_p_goal_click.this, "저장실패", Toast.LENGTH_LONG).show();
            }
        });
    }
}