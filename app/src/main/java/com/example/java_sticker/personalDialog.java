package com.example.java_sticker;

import androidx.annotation.NonNull;

public class personalDialog {

        int pCount;
        String pTittle;

        public personalDialog(){

        }

        public personalDialog(int pCount, String pTittle){
            this.pCount = pCount;
            this.pTittle = pTittle;
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

    @NonNull
    @Override
    public String toString() {
        return "personalDialog{" +
                "pCount='" + pCount + '\'' +
                ", pTittle='" + pTittle + '\'' +
                '}';
    }
}
