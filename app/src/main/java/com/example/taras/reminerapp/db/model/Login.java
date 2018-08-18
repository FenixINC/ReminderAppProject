package com.example.taras.reminerapp.db.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Taras Koloshmatin on 15.08.2018
 */
public class Login {

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
