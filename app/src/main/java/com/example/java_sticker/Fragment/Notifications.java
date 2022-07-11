package com.example.java_sticker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.java_sticker.R;
import com.example.java_sticker.group.CardviewFactor;

import java.util.ArrayList;

public class Notifications extends Fragment {
    private RecyclerView recyclerView;
    private Notifications_adapter noti_ada;
    private ArrayList<CardviewFactor> CardViewFactor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nofications, container, false);

        CardViewFactor=new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.notifications);
        recyclerView.setHasFixedSize(true);
        noti_ada=new Notifications_adapter(CardViewFactor);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(noti_ada);

        return view;
    }
}
