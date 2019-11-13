package com.creative.share.apps.alforat.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String user_id;
    private String user_name;
    private String user_username;
    private String user_phone;
    private String user_email;
    private String user_photo;
    private String last_login;
    private String token;
    private int madeen;
    private int dayeen;
    private int balance;




    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_username() {
        return user_username;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public String getLast_login() {
        return last_login;
    }

    public String getToken() {
        return token;
    }

    public int getMadeen() {
        return madeen;
    }

    public int getDayeen() {
        return dayeen;
    }

    public int getBalance() {
        return balance;
    }
}
