package com.example.taras.reminerapp.db.model;

/**
 * Created by Taras Koloshmatin on 15.08.2018
 */
public class Login {

    private String username;

    private String password;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
