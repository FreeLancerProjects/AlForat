package com.creative.share.apps.alforat.interfaces;


import com.creative.share.apps.alforat.models.AddClientModel;

public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String user_name, String password);
    }

    interface SkipListener
    {
        void skip();
    }
    interface ShowCountryDialogListener
    {
        void showDialog();
    }


    interface BackListener
    {
        void back();
    }

    interface AddClientListener
    {
        void addClient(AddClientModel addClientModel);
    }

    interface LogoutListener
    {
        void logout();
    }





}
