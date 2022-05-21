package com.example.java_sticker;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    Context context;
    ArrayList<GridItem> items = null;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    GridItem gi;
    ImageView sticker_img;

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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Context context = viewGroup.getContext();
        GridItem gridItem = items.get(position);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_grid, viewGroup, false);

        sticker_img = convertView.findViewById(R.id.sticker_img);
        sticker_img.setImageResource(R.drawable.plus);

        // Got the download URL for 'plus.png'

        if(gridItem!=null) {
            // Got the download URL for 'plus.png'
            Glide.with(context)
                    .load(gridItem.getTest())
                    .into(sticker_img);

        }else{
            Toast.makeText(context,"해당 셀이 null 입니다",Toast.LENGTH_LONG).show();
        }


        int pos = position;
        sticker_img.setOnClickListener(view -> {
//
//                Intent intent3 = new Intent(view.getContext(),custom_p_goal_click.class);
//                intent3.putExtra("pos",pos);
//                Log.d("TAG", String.valueOf(pos));
//                Log.d("TAG", String.valueOf(gridItem));


//                SharedPreferences prefs = context.getSharedPreferences("Item Index",Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putString("pos", String.valueOf(pos)); //InputString: from the EditText
//                editor.commit(); // data 저장!
            //gridItem.setTest("무지개");
            //sticker_img.setText("무지개");
        });

        View view = new View(context);
        view = (View) convertView;


        return convertView;
    }


}