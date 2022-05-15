package com.example.java_sticker;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Custom_p_item_adapter extends RecyclerView.Adapter<Custom_p_item_adapter.ViewHolder> {

    Context context;
    ArrayList<personalDialog> items;
    Intent intent;
    //int item_layout;

    public Custom_p_item_adapter(Context context,ArrayList<personalDialog> items){
        this.context = context;
        this.items = items;

    }




    @NonNull
    @Override
    public Custom_p_item_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_p_goal,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Custom_p_item_adapter.ViewHolder holder, int position) {

        final personalDialog item = items.get(position);
        holder.p_goal_tittle.setText(item.getpTittle());
        holder.p_goal_progressBar.setProgress(item.pCount);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(view.getContext(), custom_p_goal_click.class);
                intent.putExtra("tittle",item.getpTittle());
                intent.putExtra("key", item.getKey());
                intent.putExtra("count", item.getpCount());
                view.getContext().startActivity(intent);


            }
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
        ProgressBar p_goal_progressBar;
        TextView p_goal_tittle;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            p_goal_progressBar = (ProgressBar) itemView.findViewById(R.id.custom_p_goal_progressbar);
            p_goal_tittle = (TextView) itemView.findViewById(R.id.custom_p_goal_tittle);
            cardView = (CardView) itemView.findViewById(R.id.custom_p_goal_cardView);


        }

    }
}
