package com.creative.share.apps.alforat.models;

import android.content.Context;
import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.alforat.BR;
import com.creative.share.apps.alforat.R;

public class LoginModel extends BaseObservable {

    private String user_name;
    private String password;
    public ObservableField<String> error_user_name = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public LoginModel() {
        this.user_name="";
        this.password="";
    }

    public LoginModel(String user_name, String password) {
        this.user_name = user_name;
        notifyPropertyChanged(BR.user_name);
        this.password = password;
        notifyPropertyChanged(BR.password);


    }


    @Bindable
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
        notifyPropertyChanged(BR.user_name);

    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(user_name)&&
                password.length()>=6
        )
        {
            error_user_name.set(null);
            error_password.set(null);

            return true;
        }else
            {
                if (user_name.isEmpty())
                {
                    error_user_name.set(context.getString(R.string.field_req));
                }else
                    {
                        error_user_name.set(null);
                    }



                if (password.isEmpty())
                {
                    error_password.set(context.getString(R.string.field_req));
                }else if (password.length()<6)
                {
                    error_password.set(context.getString(R.string.pass_short));
                }else
                    {
                        error_password.set(null);

                    }
                return false;
            }
    }


}
