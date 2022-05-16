package com.example.java_sticker;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    Context context;
    ArrayList<GridItem> items;

    public CustomAdapter(ArrayList<GridItem> items) {
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

    //    public CustomAdapter(Context context, ArrayList<GridItem> items){
//        this.context = context;
//        this.items = items;
//    }
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        RecyclerView.ViewHolder holder;
//        View view;
////        if(viewType == TYPE_HEADER){
////            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent, false);
////            holder = new HeaderViewHolder(view);
////        }else{
////            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid, parent,false);
////            holder = new ItemViewHoler(view);
////        }
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_grid, parent,false);
//        holder = new ItemViewHoler(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        GridItem gridItem = items.get(position);
//        //        if (holder instanceof HeaderViewHolder) {
////            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
////        }else{
////            // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
////            ItemViewHoler itemViewHolder = (ItemViewHoler) holder;
////            itemViewHolder.sticker_img.setImageResource(items.get(position).getGoal_img_Id());
////        }
//        ItemViewHoler itemViewHolder = (ItemViewHoler) holder;
//        //itemViewHolder.sticker_img.setImageResource(gridItem.getGoal_img_Id());
//        itemViewHolder.sticker_img.setText(gridItem.getTest());
//    }
//
//
//    @Override
//    public long getItemId(int position){
//        return position;
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        // 아이템의 처음과 마지막은 각각 헤더와 푸터를 의미함
////        if (position == 0)
////            return TYPE_HEADER;
////        else
//            return position;
//    }
//
////    class HeaderViewHolder extends RecyclerView.ViewHolder {
////        TextView textView;
////        // 헤더 뷰홀더
////        HeaderViewHolder(View headerView) {
////            super(headerView);
////            textView = (TextView) headerView.findViewById(R.id.header_goal);
////        }
////    }
//
//
//    class ItemViewHoler extends RecyclerView.ViewHolder {
//        // 아이템 뷰홀더
//        //ImageView sticker_img;
//        TextView sticker_img;
//        ItemViewHoler(View itemViewHolder) {
//            super(itemViewHolder);
//            sticker_img = (TextView) itemViewHolder.findViewById(R.id.sticker_img);
//        }
//    }

//    public void addItem(ArrayList<GridItem> item){
//        items=item;
//    }
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        Context context = viewGroup.getContext();
        GridItem gridItem = items.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_grid, viewGroup, false);
            //ImageView sticker_img = (ImageView) convertView.findViewById(R.id.sticker_img);
            TextView sticker_img = (TextView) convertView.findViewById(R.id.sticker_img);

            //sticker_img.setImageResource(gridItem.getGoal_img_I
            // d());
            sticker_img.setText((CharSequence) gridItem.getUri());
        }else{
            View view = new View(context);
            view = (View) convertView;
        }




        return convertView;
    }


}
