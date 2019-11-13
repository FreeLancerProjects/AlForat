package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.List;

public class ProductDataModel implements Serializable {

    private List<ProductModel> data;

    public List<ProductModel> getData() {
        return data;
    }

    public static class ProductModel implements Serializable
    {
        private String id_item;
        private String item_name;
        private String item_image;
        private int current;
        private String unit_title;
        private String storage_title;
        private List<Price> prices;
        private Offer product_offer;
        private GeneralaOffer generalaOffer;


        public ProductModel(String item_name, String unit_title) {
            this.item_name = item_name;
            this.unit_title = unit_title;
        }

        public String getId_item() {
            return id_item;
        }

        public String getItem_name() {
            return item_name;
        }

        public String getItem_image() {
            return item_image;
        }

        public int getCurrent() {
            return current;
        }

        public List<Price> getPrices() {
            return prices;
        }


        public String getUnit_title() {
            return unit_title;
        }

        public String getStorage_title() {
            return storage_title;
        }

        public Offer getProduct_offer() {
            return product_offer;
        }

        public GeneralaOffer getGeneralaOffer() {
            return generalaOffer;
        }
    }

    public static class Price implements Serializable
    {
        private String item_name;
        private String price_value;
        private String tax_value;
        private String price_title;
        private String price_id;


        public String getItem_name() {
            return item_name;
        }

        public String getPrice_value() {
            return price_value;
        }

        public String getTax_value() {
            return tax_value;
        }

        public String getPrice_title() {
            return price_title;
        }

        public String getPrice_id() {
            return price_id;
        }
    }

    public class Offer implements Serializable
    {
        private String offer_copoun;
        private String offer_id;
        private String offer_value;
        private String offer_limit;
        private String offer_type;


        public String getOffer_copoun() {
            return offer_copoun;
        }

        public String getOffer_id() {
            return offer_id;
        }

        public String getOffer_value() {
            return offer_value;
        }

        public String getOffer_limit() {
            return offer_limit;
        }

        public String getOffer_type() {
            return offer_type;
        }
    }


    public class GeneralaOffer implements Serializable
    {
        private String offer_copoun;
        private String offer_id;
        private String offer_value;
        private String offer_limit;
        private String offer_type;


        public String getOffer_copoun() {
            return offer_copoun;
        }

        public String getOffer_id() {
            return offer_id;
        }

        public String getOffer_value() {
            return offer_value;
        }

        public String getOffer_limit() {
            return offer_limit;
        }

        public String getOffer_type() {
            return offer_type;
        }
    }



}
