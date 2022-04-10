package com.example.java_sticker;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView gridView = null;
    private CustomAdapter adapter = null;
    GridItem gridItem;
    ArrayList<GridItem> items;
    Dialog custom_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridView = (GridView) findViewById(R.id.gridView);
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

        gridView.setAdapter(adapter);
    }


    public void showDialog(){
        custom_dialog.show();


        EditText sticker_count = (EditText) custom_dialog.findViewById(R.id.sticker_count);
        Button noBtn = custom_dialog.findViewById(R.id.noBtn);
        Button yesBtn = custom_dialog.findViewById(R.id.yesBtn);


        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                custom_dialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vi = sticker_count.getText().toString();
                int it = 0;
                try {
                    it = NumberFormat.getInstance().parse(vi).intValue();
                    //Toast.makeText(getApplicationContext(), it, Toast.LENGTH_LONG).show();
                    for(int i=0; i<it; i++){
                        items.add(new GridItem(R.drawable.heart));
                    }

                    adapter.items = items;
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                adapter.notifyDataSetChanged();


                custom_dialog.dismiss();
            }
        });


    }
}