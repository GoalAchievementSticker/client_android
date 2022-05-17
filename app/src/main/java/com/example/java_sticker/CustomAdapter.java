package com.example.java_sticker;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    Context context;
    ArrayList<GridItem> items;

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

        TextView sticker_img;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_grid, viewGroup, false);
            //ImageView sticker_img = (ImageView) convertView.findViewById(R.id.sticker_img);
            //sticker_img.setImageResource(gridItem.getGoal_img_Id());
            sticker_img = (TextView) convertView.findViewById(R.id.sticker_img);
            sticker_img.setText(gridItem.getTest());


            int pos = position;
            sticker_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent3 = new Intent(view.getContext(),custom_p_goal_click.class);
                    intent3.putExtra("pos",pos);
                    //gridItem.setTest("무지개");
                    //sticker_img.setText("무지개");
                }
            });
        }else{
            View view = new View(context);
            view = (View) convertView;


        }




        return convertView;
    }


}
