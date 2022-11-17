package com.example.java_sticker.model;

/*json 데이터를 받아오는 class 선언*/
public class User {
    private int user_id;
//    @SerializedName("nickname")
    private String nickname;
//    @SerializedName("email")
    private String email;
//    @SerializedName("password")
    private String password;

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
