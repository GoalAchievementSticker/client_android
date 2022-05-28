package com.example.java_sticker.Fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java_sticker.R;
import com.example.java_sticker.group.GroupDialog;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {


    private ArrayList<GroupDialog> mDataset;
    Intent studydata;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tittle, person_count, goal_count;
        CardView cardView;

        public ViewHolder(View view){
            super(view);
            tittle = (TextView) view.findViewById(R.id.fh_tittle);
            person_count = (TextView) view.findViewById(R.id.personal_count_view);
            goal_count = (TextView) view.findViewById(R.id.goal_count_view);
            cardView = (CardView) view.findViewById(R.id.custom_fraghome_cardView);
        }
    }

    public CategoryAdapter(ArrayList<GroupDialog> mDataset){
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_fraghome, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.tittle.setText(mDataset.get(position).getgTittle());
        holder.person_count.setText(String.valueOf(mDataset.get(position).getLimit()));
        holder.goal_count.setText(String.valueOf(mDataset.get(position).getgCount())+"개");
        holder.cardView.setOnClickListener(view -> {
            //클릭시 프래그먼트로 데이터 보내기
            //AppCompatActivity activity = (AppCompatActivity) view.getContext();
           // activity.getFragmentManager().beginTransaction().replace(R.id.fragment_place, new Fragment1()).addToBackStack(null).commit();

        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }




}
