package com.example.java_sticker.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.java_sticker.Group_main;
import com.example.java_sticker.R;

public class FragHome extends Fragment {
    private View view;
    ImageButton study_category;
    ImageButton exercise_category;
    ImageButton hobby_category;
    ImageButton routin_category;

    Toolbar searchView_home;

    Group_main group_main;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        group_main = (Group_main) getActivity();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        group_main = null;
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fraghome, container, false);

        study_category = (ImageButton) view.findViewById(R.id.home_study_button);
        exercise_category =(ImageButton) view.findViewById(R.id.home_exercise_button);
        hobby_category = (ImageButton) view.findViewById(R.id.home_hobby_button);
        routin_category = (ImageButton) view.findViewById(R.id.home_routin_button);

        searchView_home = (Toolbar) view.findViewById(R.id.home_search);
        ((AppCompatActivity)getActivity()).setSupportActionBar(searchView_home);

        searchView_home.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.search){
                    group_main.onFragmentChange(4);
                }
                return true;
            }
        });


        setHasOptionsMenu(true);

        //0:공부, 1:운동, 2:취미, 3:루틴
        study_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                group_main.onFragmentChange(0);

            }
        });

        exercise_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                group_main.onFragmentChange(1);
            }
        });

        hobby_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                group_main.onFragmentChange(2);
            }
        });

        routin_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                group_main.onFragmentChange(3);
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
}
