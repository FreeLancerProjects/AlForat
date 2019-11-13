package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.List;

public class BillDataModel implements Serializable {

    private Meta meta;
    private List<BillModel> data;

    public Meta getMeta() {
        return meta;
    }

    public List<BillModel> getData() {
        return data;
    }

    public class BillModel implements Serializable
    {
        private String id_bill;
        private String bill_date;
        private String bill_total_cost;
        private String client_name;
        private List<Details> details;

        public String getId_bill() {
            return id_bill;
        }

        public String getBill_date() {
            return bill_date;
        }

        public String getBill_total_cost() {
            return bill_total_cost;
        }

        public String getClient_name() {
            return client_name;
        }

        public List<Details> getDetails() {
            return details;
        }
    }

    public class Details implements Serializable
    {
        private String item_amount;
        private String total_price;
        private String item_name;
        private String total_after_discount;
        private String item_image;

        public String getItem_amount() {
            return item_amount;
        }

        public String getTotal_price() {
            return total_price;
        }

        public String getItem_name() {
            return item_name;
        }

        public String getTotal_after_discount() {
            return total_after_discount;
        }

        public String getItem_image() {
            return item_image;
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
