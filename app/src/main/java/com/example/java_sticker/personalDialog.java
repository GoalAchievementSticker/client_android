package com.example.java_sticker;

import androidx.annotation.NonNull;

public class personalDialog {

        int resCount;
        String resTittle;

        public personalDialog(){

        }

        public personalDialog(int resCount, String resTittle){
            this.resCount = resCount;
            this.resTittle = resTittle;
        }

    public int getResCount() {
        return resCount;
    }

    public void setResCount(int resCount) {
        this.resCount = resCount;
    }

    public String getResTittle() {
        return resTittle;
    }

    public void setResTittle(String resTittle) {
        this.resTittle = resTittle;
    }

    @NonNull
    @Override
    public String toString() {
        return "personalDialog{" +
                "resCount='" + resCount + '\'' +
                ", resTittle='" + resTittle + '\'' +
                '}';
    }
}
