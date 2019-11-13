package com.creative.share.apps.alforat.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.alforat.BR;
import com.creative.share.apps.alforat.R;

public class AddClientModel extends BaseObservable {

    private String name;
    private String balance;
    private String area_id;
    private String address;
    private String phone_code;
    private String phone_number;
    private String tax_number;
    private String commercial;
    private String max_money;

    public ObservableField<String> error_name = new ObservableField<>();
    //public ObservableField<String> error_balance = new ObservableField<>();
    public ObservableField<String> error_address = new ObservableField<>();
    public ObservableField<String> error_phone_code = new ObservableField<>();
    public ObservableField<String> error_phone = new ObservableField<>();
    //public ObservableField<String> error_tax = new ObservableField<>();
   // public ObservableField<String> error_commercial = new ObservableField<>();
    //public ObservableField<String> error_max_money = new ObservableField<>();


    public boolean isDataValid(Context context)
    {


        if (!TextUtils.isEmpty(name)&&
                !TextUtils.isEmpty(address)&&
                !TextUtils.isEmpty(phone_code)&&
                !TextUtils.isEmpty(phone_number)


        )
        {



            error_name.set(null);
            //error_balance.set(null);
            error_address.set(null);
            error_phone_code.set(null);
            error_phone.set(null);
           /* error_tax.set(null);
            error_commercial.set(null);
            error_max_money.set(null);*/

            return true;
        }else
        {
            if (name.isEmpty())
            {
                error_name.set(context.getString(R.string.field_req));
            }else
            {
                error_name.set(null);
            }

           /* if (balance.isEmpty())
            {
                error_balance.set(context.getString(R.string.field_req));
            }else
            {
                error_balance.set(null);
            }*/

            if (area_id.isEmpty())
            {
                Toast.makeText(context, R.string.ch_area, Toast.LENGTH_SHORT).show();
            }

            if (address.isEmpty())
            {
                error_address.set(context.getString(R.string.field_req));
            }else
            {
                error_address.set(null);
            }

            if (phone_code.isEmpty())
            {
                error_phone_code.set(context.getString(R.string.field_req));
            }else
            {
                error_phone_code.set(null);
            }

            if (phone_number.isEmpty())
            {
                error_phone.set(context.getString(R.string.field_req));
            }else
            {
                error_phone.set(null);
            }

            /*if (tax_number.isEmpty())
            {
                error_tax.set(context.getString(R.string.field_req));
            }else
            {
                error_tax.set(null);
            }

            if (commercial.isEmpty())
            {
                error_commercial.set(context.getString(R.string.field_req));
            }else
            {
                error_commercial.set(null);
            }

            if (max_money.isEmpty())
            {
                error_max_money.set(context.getString(R.string.field_req));
            }else
            {
                error_max_money.set(null);
            }
*/




            return false;
        }
    }

    public AddClientModel() {
        this.name="";
        notifyPropertyChanged(BR.name);
        /*this.balance="";
        notifyPropertyChanged(BR.balance);*/
        this.area_id = "0";
        notifyPropertyChanged(BR.area_id);
        this.address = "";
        notifyPropertyChanged(BR.address);
        this.phone_code="";
        notifyPropertyChanged(BR.phone_code);
        this.phone_number="";
        /*notifyPropertyChanged(BR.phone_number);
        this.tax_number = "";
        notifyPropertyChanged(BR.tax_number);
        this.commercial = "";
        notifyPropertyChanged(BR.commercial);
        this.max_money ="";
        notifyPropertyChanged(BR.max_money);*/

    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    /*@Bindable
    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
        notifyPropertyChanged(BR.balance);

    }*/

    @Bindable
    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
        notifyPropertyChanged(BR.area_id);

    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);

    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);

    }

    @Bindable
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
        notifyPropertyChanged(BR.phone_number);

    }

   /* @Bindable
    public String getTax_number() {
        return tax_number;
    }

    public void setTax_number(String tax_number) {
        this.tax_number = tax_number;
        notifyPropertyChanged(BR.tax_number);

    }*/

   /* @Bindable
    public String getCommercial() {
        return commercial;
    }

    public void setCommercial(String commercial) {
        this.commercial = commercial;
        notifyPropertyChanged(BR.commercial);

    }*/

   /* @Bindable
    public String getMax_money() {
        return max_money;
    }

    public void setMax_money(String max_money) {
        this.max_money = max_money;
        notifyPropertyChanged(BR.max_money);

    }*/




}
