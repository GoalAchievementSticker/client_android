package com.example.java_sticker;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

//        gridView.setAdapter(adapter);
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

            writePersonalDialog("2", vi,goal );

            readPersonalDialog();

            //목표를 가져왔다면?
            if(p_tittle != null){
                // Header 추가 -> 반영
                View header = getLayoutInflater().inflate(R.layout.header, null, false);
                header_goal = (TextView) header.findViewById(R.id.header_goal);
                gridView.addHeaderView(header);
                header_goal.setText(p_tittle);
                gridView.setAdapter(adapter);

                try {


                    //vi는 원래 p_count여야하는데 아직 잘 안가져와서 vi로 해둠
                    for (int i = 0; i <vi ; i++) {
                        items.add(new GridItem(i,R.drawable.heart));
                    }

                    adapter.items = items;

                    databaseReference.child("goal_personal").child("2").setValue(items).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "저장함", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();


                custom_dialog.dismiss();

            }




        });


    }

    //다이얼로그 값 저장하는 함수
    private void writePersonalDialog(String userid, int count, String tittle){
        personalDialog pDialog = new personalDialog(count, tittle);

        databaseReference.child("dialog_personal").child(userid).setValue(pDialog).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(MainActivity.this, "다이얼로그 저장", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(MainActivity.this, "다이얼로그 저장못함", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //다이얼로그 저장된 함수 가져오기
    private void readPersonalDialog(){
        databaseReference.child("dialog_personal").child("2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                p_tittle = (String) snapshot.child("pTittle").getValue();
                //p_count = (int) snapshot.child("pCount").getValue();

                //목표값 가져왔는 확인하게끔 toast 메세지
                Toast.makeText(MainActivity.this, p_tittle, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, "못함", Toast.LENGTH_LONG).show();
            }
        });
    }
}