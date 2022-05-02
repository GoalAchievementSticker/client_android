package com.example.java_sticker;

import static java.lang.Integer.parseInt;

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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class MainActivity extends AppCompatActivity {

    private TextView header_goal;
    private CustomAdapter adapter = null;
    ArrayList<GridItem> items;
    private GridViewWithHeaderAndFooter gridView = null;
    Dialog custom_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridView = (GridViewWithHeaderAndFooter) findViewById(R.id.gridView);
        adapter = new CustomAdapter();
        items = new ArrayList<GridItem>();
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

            String vi = sticker_count.getText().toString();
            String goal = sticker_goal.getText().toString();
            int it = 0;
            try {
                it = Objects.requireNonNull(NumberFormat.getInstance().parse(vi)).intValue();

                for (int i = 0; i < it; i++) {
                    items.add(new GridItem(R.drawable.heart));
                }

                adapter.items = items;
            } catch (Exception e) {
                e.printStackTrace();
            }


            adapter.notifyDataSetChanged();


            custom_dialog.dismiss();
        });


    }
}