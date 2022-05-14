package com.example.java_sticker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);


        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        adapter = new CustomAdapter();
        items = new ArrayList<GridItem>();
        pDialog = new ArrayList<personalDialog>();
        custom_dialog = new Dialog(MainActivity.this);

        custom_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        custom_dialog.setContentView(R.layout.custom_dialog);


        Button btn = (Button) findViewById(R.id.dialogButton);


        btn.setOnClickListener(view -> showDialog());
    }


    public void showDialog() {
        custom_dialog.show();


        EditText sticker_count = (EditText) custom_dialog.findViewById(R.id.sticker_count);
        EditText sticker_goal = (EditText) custom_dialog.findViewById(R.id.sticker_goal);

        Button noBtn = custom_dialog.findViewById(R.id.noBtn);
        Button yesBtn = custom_dialog.findViewById(R.id.yesBtn);

        noBtn.setOnClickListener(view -> custom_dialog.dismiss());

        yesBtn.setOnClickListener(view -> {


            int vi = Integer.parseInt(sticker_count.getText().toString());
            String goal = sticker_goal.getText().toString();

            HashMap result = new HashMap<>();
            result.put("sticker_count",vi);
            result.put("sticker_goal",goal);

            //다이얼로그 값 저장
            writePersonalDialog("2", vi,goal );


            //다이얼로그 값 불러오기
            readPersonalDialog();


            //다이얼로그 값 불러오고 딜레이를 줘서 타이틀값 가져오기 해결
            new Handler().postDelayed(() -> {
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
                            items.add(new GridItem(i, R.drawable.ic_baseline_add_circle_24));
                        }

                        adapter.items = items;

                        databaseReference.child("dialog_personal").child("2").child(p_tittle).child("goal").setValue(items).addOnSuccessListener(unused
                                -> Toast.makeText(MainActivity.this, "생성", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_LONG).show());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    adapter.notifyDataSetChanged();
                    custom_dialog.dismiss();
                }
            },400);

        });

    }

    //다이얼로그 값 저장하는 함수
    private void writePersonalDialog(String userid, int count, String tittle){
        personalDialog pDialog = new personalDialog(count, tittle);

        databaseReference.child("dialog_personal").child(userid).setValue(pDialog).addOnSuccessListener(unused -> {
            //Toast.makeText(MainActivity.this, "다이얼로그 저장", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {
            //Toast.makeText(MainActivity.this, "다이얼로그 저장못함", Toast.LENGTH_SHORT).show();
        });
    }

    //다이얼로그 저장된 함수 가져오기
    private void readPersonalDialog(){
        databaseReference.child("dialog_personal").child("2").addValueEventListener(new ValueEventListener() {

            //경로의 전체 내용을 읽고 변경사항을 수신 대기
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

                Toast.makeText(MainActivity.this, "저장실패", Toast.LENGTH_LONG).show();
            }
        });
    }
}