package com.example.java_sticker;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = null;
        if(pref.getBoolean("isBlackTheme", false)) {
            this.setTheme(R.style.AppTheme_BlackTheme);
        }
        else {
            this.setTheme(R.style.AppTheme);
        }
    }

}