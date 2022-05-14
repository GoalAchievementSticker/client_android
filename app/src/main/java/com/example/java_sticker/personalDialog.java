package com.example.java_sticker;

import java.util.ArrayList;

public class personalDialog {

        int pCount;
        String pTittle;
        String key;

        public personalDialog(){

        }

        public personalDialog(int pCount, String pTittle, String key){
            this.pCount = pCount;
            this.pTittle = pTittle;
            this.key = key;

        }

    public int getpCount() {
        return pCount;
    }

    public void setpCount(int pCount) {
        this.pCount = pCount;
    }

    public String getpTittle() {
        return pTittle;
    }

    public void setpTittle(String pTittle) {
        this.pTittle = pTittle;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }





}
