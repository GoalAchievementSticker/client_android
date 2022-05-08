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
import java.util.Objects;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class MainActivity extends AppCompatActivity {

    private TextView header_goal;
    private CustomAdapter adapter = null;
    ArrayList<GridItem> items;
    ArrayList<personalDialog> pDialog;
    private GridViewWithHeaderAndFooter gridView = null;
    Dialog custom_dialog;


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
            // Header 추가
            View header = getLayoutInflater().inflate(R.layout.header, null, false);
            header_goal = (TextView) header.findViewById(R.id.header_goal);
            gridView.addHeaderView(header);
            header_goal.setText(sticker_goal.getText().toString().trim());
            gridView.setAdapter(adapter);

            int vi = Integer.parseInt(sticker_count.getText().toString());
            String goal = sticker_goal.getText().toString();

            HashMap result = new HashMap<>();
            result.put("sticker_count",vi);
            result.put("sticker_goal",goal);

            writePersonalDialog("1", vi,goal );



            int it = 0;
            try {
                for (int i = 0; i < vi; i++) {
                    items.add(new GridItem(R.drawable.heart));
                }

                adapter.items = items;

                databaseReference.child("personal_goal").child("1").setValue(items).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "저장함", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "저장못함", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


            adapter.notifyDataSetChanged();


            custom_dialog.dismiss();
        });


    }

    private void writePersonalDialog(String userid, int count, String tittle){
        personalDialog pDialog = new personalDialog(count, tittle);

        databaseReference.child("dialog_personal").child(userid).setValue(pDialog).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "저장함", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "저장못함", Toast.LENGTH_LONG).show();
            }
        });
    }
}