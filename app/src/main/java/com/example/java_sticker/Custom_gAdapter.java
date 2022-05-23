package com.example.java_sticker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import in.srain.cube.views.GridViewWithHeaderAndFooter;

public class Custom_gAdapter extends BaseAdapter {
    Context context;
    ArrayList<GridItem> items = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    GridItem gi;
    ImageView sticker_img;
    LinearLayout ll;
    View select_v;
    GridViewWithHeaderAndFooter gridView;

    public Custom_gAdapter(Context context, ArrayList<GridItem> items) {
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        GridItem gridItem = items.get(position);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_grid, viewGroup, false);

        sticker_img = convertView.findViewById(R.id.sticker_img);
        sticker_img.setImageResource(R.drawable.plus);

//        select_v=inflater.inflate(R.layout.activity_custom_pgoal_click,viewGroup,false);
//        ll = select_v.findViewById(R.id.select_sticker);
//        gridView = select_v.findViewById(R.id.gridView);

        // Got the download URL for 'plus.png'

        if(gridItem!=null) {
            // Got the download URL for 'plus.png'
            Toast.makeText(context,"해당 셀이 null 이 아닙니다",Toast.LENGTH_LONG).show();
            Glide.with(context)
                    .load(gridItem.getTest())
                    .into(sticker_img);

        }else{
            Toast.makeText(context,"해당 셀이 null 입니다",Toast.LENGTH_LONG).show();
        }

        sticker_img.setOnClickListener(view->{});


        View view = new View(context);
        view = (View) convertView;


        return convertView;
    }


}
