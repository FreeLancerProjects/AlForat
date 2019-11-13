package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.List;

public class ClientsDataModel implements Serializable {

    private List<ClientModel> clients;
    private Meta meta;


    public List<ClientModel> getClientModelList() {
        return clients;
    }

    public Meta getMeta() {
        return meta;
    }

    public class ClientModel implements Serializable
    {
        private String id_client;
        private String client_acc_code;
        private String client_name;
        private String client_phone;
        private String cleint_tax_card;
        private String cleint_address;
        private String client_debt_limit;
        private String client_commercial_register;
        private String client_past_amount;
        private String area_id_fk;
        private String area_name;
        private int client_madeen;
        private int client_dayeen;
        private int client_balance;
        private String delegate_id_fk;
        private String date;




        public String getId_client() {
            return id_client;
        }

        public String getClient_acc_code() {
            return client_acc_code;
        }

        public String getClient_name() {
            return client_name;
        }

        public String getClient_phone() {
            return client_phone;
        }

        public String getCleint_tax_card() {
            return cleint_tax_card;
        }

        public String getCleint_address() {
            return cleint_address;
        }

        public String getClient_debt_limit() {
            return client_debt_limit;
        }

        public String getClient_commercial_register() {
            return client_commercial_register;
        }

        public String getClient_past_amount() {
            return client_past_amount;
        }

        public String getArea_id_fk() {
            return area_id_fk;
        }

        public String getArea_name() {
            return area_name;
        }

        public int getClient_madeen() {
            return client_madeen;
        }

        public int getClient_dayeen() {
            return client_dayeen;
        }

        public int getClient_balance() {
            return client_balance;
        }

        public String getDelegate_id_fk() {
            return delegate_id_fk;
        }

        public String getDate() {
            return date;
        }
    }

    public class Meta implements Serializable
    {
        private int current_page;

        public int getCurrent_page() {
            return current_page;
        }
    }
}
