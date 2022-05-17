package com.example.java_sticker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    Context context;
    ArrayList<GridItem> items;

    public CustomAdapter(Context context, ArrayList<GridItem> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        GridItem gridItem = items.get(position);

        TextView sticker_img = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_grid, viewGroup, false);
            //ImageView sticker_img = (ImageView) convertView.findViewById(R.id.sticker_img);
            //sticker_img.setImageResource(gridItem.getGoal_img_Id());
            sticker_img = (TextView) convertView.findViewById(R.id.sticker_img);
            sticker_img.setText(gridItem.getTest());
        } else {
            View view = new View(context);
            view = (View) convertView;
        }

        // Implement On Item click listener
        sticker_img.setOnClickListener((View.OnClickListener) (parent, view, position1, id) -> {

            switch (position1) {
                case 0:
                    break;
                case 1:
                    break;

            }
        });

        return convertView;
    }


}
