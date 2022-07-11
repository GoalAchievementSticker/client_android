package com.example.java_sticker.Fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java_sticker.R;
import com.example.java_sticker.group.CardviewFactor;
import com.example.java_sticker.personal.Custom_p_item_adapter;
import com.example.java_sticker.personal.custom_p_goal_click;
import com.example.java_sticker.personal.personalDialog;

import java.util.ArrayList;


public class Notifications_adapter extends RecyclerView.Adapter<Notifications_adapter.ViewHolder> {
    ArrayList<CardviewFactor> items;

    public Notifications_adapter() {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;
        TextView body;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.noti_cardview);
            title = view.findViewById(R.id.noti_title);
            body = view.findViewById(R.id.noti_body);
        }
    }


    @NonNull
    @Override
    public Notifications_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notis_cardview_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Notifications_adapter.ViewHolder holder, int position) {
        final CardviewFactor item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.body.setText(item.getBody());

        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), custom_p_goal_click.class);
            view.getContext().startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
