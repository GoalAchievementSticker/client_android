package com.example.java_sticker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    Context context;
    ArrayList<GridItem> items=null;

    public CustomAdapter(Context context,ArrayList<GridItem> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        Context context = viewGroup.getContext();
        GridItem gridItem = items.get(position);

        ImageView sticker_img;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_grid, viewGroup, false);
            sticker_img =  convertView.findViewById(R.id.sticker_img);



            Glide.with(context)
                    .load(gridItem.getTest())
                    .into(sticker_img);

            int pos = position;
            sticker_img.setOnClickListener(view -> {

                SharedPreferences prefs = context.getSharedPreferences("Item Index",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("pos", String.valueOf(pos)); //InputString: from the EditText
                editor.commit(); // data 저장!
            });
        }else{
            View view = new View(context);
            view = (View) convertView;


        }




        return convertView;
    }


}