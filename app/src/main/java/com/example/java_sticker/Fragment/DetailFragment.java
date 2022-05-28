package com.example.java_sticker.Fragment;


import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.java_sticker.R;


public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private View view;
    TextView goal;
    TextView count;
    TextView auth;
    TextView limit;
    TextView cate;

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);

        goal = view.findViewById(R.id.goal);
        count = view.findViewById(R.id.count);
        auth = view.findViewById(R.id.auth);
        limit = view.findViewById(R.id.limit);
        cate = view.findViewById(R.id.cate);

        try {
            GetBundle();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }



        // Inflate the layout for this fragment
        return view;
    }

    private void GetBundle() {
        Bundle bundle = this.getArguments();

        String _count = bundle.getString("count");
        int _limit = bundle.getInt("limit");
        String _goal = bundle.getString("goal");
        String _auth = bundle.getString("auth");
        String _cate = bundle.getString("cate");

        Log.d("getBundle",_count+"/ "+_limit+"/ "+_goal+"/ "+_auth+"/ "+_cate);
        goal.setText(_goal);
        count.setText(_count);
        auth.setText(_auth);
        limit.setText(String.valueOf(_limit));
        cate.setText(_cate);
    }
}