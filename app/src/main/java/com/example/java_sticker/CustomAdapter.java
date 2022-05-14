package com.example.java_sticker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    ArrayList<GridItem> items = new ArrayList<>();


    @Override
    public int getCount(){
        return items.size();
    }

    @Override
    public Object getItem(int position){
        return items.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        final Context context = viewGroup.getContext();
        final GridItem gridItem = items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_grid, viewGroup, false);

            ImageView sticker_img = (ImageView) convertView.findViewById(R.id.sticker_img);

            sticker_img.setImageResource(gridItem.getGoal_img_Id());
        }else{
            View view = new View(context);
            view = (View) convertView;
        }

        return convertView;
    }


}
