package com.example.java_sticker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.java_sticker.R;

public class j_FragJoin extends Fragment {
    View view;
    public j_FragJoin() {
        // Required empty public constructor
    }


    public static j_FragJoin newInstance() {
        j_FragJoin j_fragJoin = new j_FragJoin();
        return j_fragJoin;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.j_fragjoin, container, false);
        return view;
    }

}
