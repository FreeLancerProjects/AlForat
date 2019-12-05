package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.List;

public class ChargeReportDataModel implements Serializable {
    private Meta meta;
    private List<ChargeModel> data;

    public Meta getMeta() {
        return meta;
    }

    public List<ChargeModel> getData() {
        return data;
    }

    public class ChargeModel implements Serializable
    {
        private String id_order;
        private String order_time;
        private String user_name;
        private String user_photo;
        private String storage_title;
        private List<Details> details;

        public String getId_order() {
            return id_order;
        }

        public String getOrder_time() {
            return order_time;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public String getStorage_title() {
            return storage_title;
        }

        public List<Details> getDetails() {
            return details;
        }
    }

    public class Details implements Serializable
    {
        private String item_cost;
        private String item_amount;
        private String item_name;
        private String item_image;

        public String getItem_cost() {
            return item_cost;
        }

        public String getItem_amount() {
            return item_amount;
        }

        public String getItem_name() {
            return item_name;
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
