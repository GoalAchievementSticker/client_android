package com.example.java_sticker.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.java_sticker.R;


public class FragInputCheck extends Fragment {
    private View view;
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert inflater != null;
        view = inflater.inflate(R.layout.fragjoin, container, false);
        return view;
    }
}
