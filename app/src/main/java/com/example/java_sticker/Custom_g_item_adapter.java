package com.example.java_sticker;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class Custom_g_item_adapter extends RecyclerView.Adapter<Custom_g_item_adapter.ViewHolder> {
    Context context;
    ArrayList<GroupDialog> items;
    Intent intent;
    //int item_layout;

    //그룹방 이미지 불러오기 위한 장치
    String uid;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference profile_databaseReference = firebaseDatabase.getReference();


    public Custom_g_item_adapter(Context context,ArrayList<GroupDialog> items){
        this.context = context;
        this.items = items;

    }




    @NonNull
    @Override
    public Custom_g_item_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_g_goal,parent,false);
        return new Custom_g_item_adapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull Custom_g_item_adapter.ViewHolder holder, int position) {

        //파이어베이스
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        uid = user.getUid();

        final GroupDialog item = items.get(position);

        //그룹방 이미지(일단 프사로 설정)
        profile_databaseReference.child("user").child(uid).child("profileImageUrl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uri = snapshot.getValue(String.class);
                Glide.with(context)
                        .load(uri)
                        .into(holder.g_img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //holder.g_img.setImageResource(R.drawable.ic_baseline_supervised_user_circle_24);
        holder.g_goal_tittle.setText(item.getgTittle());
        holder.g_goal_progressBar.setMaxValue(item.gCount);
       // Log.d("TAG", String.valueOf(item.gCount));
        holder.g_goal_progressBar.setCurValue(item.gGoal);
        holder.cardView.setOnClickListener(view -> {
            intent = new Intent(view.getContext(), custom_g_goal_click.class);
            intent.putExtra("tittle",item.getgTittle());
            intent.putExtra("key", item.getKey());
            intent.putExtra("count", item.getgCount());
            view.getContext().startActivity(intent);


        });

    }

    @Override
    public int getItemCount() {
        return (items != null ? items.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //ProgressBar p_goal_progressBar;
        ImageView g_img;
        CustomProgress g_goal_progressBar;
        TextView g_goal_tittle;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            g_img=itemView.findViewById(R.id.g_pic);
            g_goal_progressBar = (CustomProgress) itemView.findViewById(R.id.customProgress);
            g_goal_tittle = (TextView) itemView.findViewById(R.id.custom_g_goal_tittle);
            cardView = (CardView) itemView.findViewById(R.id.custom_g_goal_cardView);


        }

    }
    }

