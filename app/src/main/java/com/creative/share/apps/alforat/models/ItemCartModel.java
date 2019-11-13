package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.List;

public class ItemCartModel implements Serializable {

    private String client_acc_code;
    private String id_client;
    private String area_id_fk;

    private double bill_total_cost;
    private int total_discount;
    private double total_tax;
    private boolean isGeneralOffer;
    private double general_offer_value;
    private String general_offer_type;
    private String client_name;
    private String client_phone;
    private String client_address;




    private List<Items> cart;

    public String getClient_acc_code() {
        return client_acc_code;
    }

    public void setClient_acc_code(String client_acc_code) {
        this.client_acc_code = client_acc_code;
    }

    public String getId_client() {
        return id_client;
    }

    public String getArea_id_fk() {
        return area_id_fk;
    }

    public void setArea_id_fk(String area_id_fk) {
        this.area_id_fk = area_id_fk;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public double getBill_total_cost() {
        return bill_total_cost;
    }

    public void setBill_total_cost(double bill_total_cost) {
        this.bill_total_cost = bill_total_cost;
    }

    public int getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(int total_discount) {
        this.total_discount = total_discount;
    }

    public double getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(double total_tax) {
        this.total_tax = total_tax;
    }

    public List<Items> getCart() {
        return cart;
    }

    public void setCart(List<Items> cart) {
        this.cart = cart;
    }

    public boolean isGeneralOffer() {
        return isGeneralOffer;
    }

    public void setGeneralOffer(boolean generalOffer) {
        isGeneralOffer = generalOffer;
    }

    public double getGeneral_offer_value() {
        return general_offer_value;
    }

    public void setGeneral_offer_value(double general_offer_value) {
        this.general_offer_value = general_offer_value;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_name() {
        return client_name;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public String getClient_address() {
        return client_address;
    }

    public void setClient_address(String client_address) {
        this.client_address = client_address;
    }

    public String getGeneral_offer_type() {
        return general_offer_type;
    }

    public void setGeneral_offer_type(String general_offer_type) {
        this.general_offer_type = general_offer_type;
    }

    public static class Items implements Serializable
    {
        private String item_id_fk;
        private String name;
        private String image;
        private int item_amount;
        private int item_bouns;
        private String price_value;
        private String tax_value;
        private int current_amount;
        private int limit;
        private int offer_value;
        private String price_id;


        public Items() {
        }

        public Items(String item_id_fk, String name, String image, int item_amount, int item_bouns, String price_value, String tax_value) {
            this.item_id_fk = item_id_fk;
            this.name = name;
            this.image = image;
            this.item_amount = item_amount;
            this.item_bouns = item_bouns;
            this.price_value = price_value;
            this.tax_value = tax_value;
        }

        public String getItem_id_fk() {
            return item_id_fk;
        }

        public void setItem_id_fk(String item_id_fk) {
            this.item_id_fk = item_id_fk;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getItem_amount() {
            return item_amount;
        }

        public void setItem_amount(int item_amount) {
            this.item_amount = item_amount;
        }

        public int getItem_bouns() {
            return item_bouns;
        }

        public void setItem_bouns(int item_bouns) {
            this.item_bouns = item_bouns;
        }

        public String getPrice_value() {
            return price_value;
        }

        public void setPrice_value(String price_value) {
            this.price_value = price_value;
        }

        public String getTax_value() {
            return tax_value;
        }

        public void setTax_value(String tax_value) {
            this.tax_value = tax_value;
        }

        public int getCurrent_amount() {
            return current_amount;
        }

        public void setCurrent_amount(int current_amount) {
            this.current_amount = current_amount;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getOffer_value() {
            return offer_value;
        }

        public void setOffer_value(int offer_value) {
            this.offer_value = offer_value;
        }

        public String getPrice_id() {
            return price_id;
        }

        public void setPrice_id(String price_id) {
            this.price_id = price_id;
        }
    }
}
