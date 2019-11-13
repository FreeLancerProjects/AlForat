package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateBillModel implements Serializable {

    private String client_name;
    private String client_id;
    private long bill_date;
    private String center_cost_id;
    private long due_date;
    private String bill;
    private List<Item> itemList=new ArrayList<>();
    private double net_total;
    private double total_discount_value;
    private double total_tax_value;
    private double total;





    public void addItem(Item item)
    {
        this.itemList.add(item);
        calculate_all_total();

    }
    private void calculate_all_total()
    {
        net_total = 0;
        total_tax_value = 0;
        total_discount_value = 0;
        total =0;

        if (itemList!=null)
        {
            for (Item item:itemList)
            {
                total += item.getNet_total();
                total_discount_value += item.getTotal_discount_value();
                total_tax_value = item.getTotal_tax_value();

            }

            net_total = (total-total_discount_value)+total_tax_value;

        }
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public long getBill_date() {
        return bill_date;
    }

    public void setBill_date(long bill_date) {
        this.bill_date = bill_date;
    }

    public String getCenter_cost_id() {
        return center_cost_id;
    }

    public void setCenter_cost_id(String center_cost_id) {
        this.center_cost_id = center_cost_id;
    }

    public long getDue_date() {
        return due_date;
    }

    public void setDue_date(long due_date) {
        this.due_date = due_date;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public List<Item> getItemList() {
        return itemList;
    }


    public double getNet_total() {
        return net_total;
    }

    public void setNet_total(double net_total) {
        this.net_total = net_total;
    }

    public double getTotal_discount_value() {
        return total_discount_value;
    }

    public void setTotal_discount_value(double total_discount_value) {
        this.total_discount_value = total_discount_value;
    }

    public double getTotal_tax_value() {
        return total_tax_value;
    }

    public void setTotal_tax_value(double total_tax_value) {
        this.total_tax_value = total_tax_value;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public static class Item implements Serializable {
        private String item_id;
        private String item_name;
        private String unit;
        private int bonus;
        private double tax;
        private double total_tax_value;
        private String discount_id;
        private double discount;
        private String discount_title;

        private double total_discount_value;
        private double total_after_calc;
        private int amount;
        private double price;
        private double net_total;

        public Item() {
        }

        public Item(String item_id, String item_name, String unit, int bonus, double tax, double total_tax_value, String discount_id, double discount, String discount_title, double total_discount_value, double total_after_calc, int amount, double price, double net_total) {
            this.item_id = item_id;
            this.item_name = item_name;
            this.unit = unit;
            this.bonus = bonus;
            this.tax = tax;
            this.total_tax_value = total_tax_value;
            this.discount_id = discount_id;
            this.discount = discount;
            this.discount_title = discount_title;
            this.total_discount_value = total_discount_value;
            this.total_after_calc = total_after_calc;
            this.amount = amount;
            this.price = price;
            this.net_total = net_total;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getBonus() {
            return bonus;
        }

        public void setBonus(int bonus) {
            this.bonus = bonus;
        }

        public double getTax() {
            return tax;
        }

        public String getDiscount_id() {
            return discount_id;
        }

        public void setDiscount_id(String discount_id) {
            this.discount_id = discount_id;
        }

        public void setTax(double tax) {
            this.tax = tax;
        }

        public double getTotal_tax_value() {
            return total_tax_value;
        }

        public void setTotal_tax_value(double total_tax_value) {
            this.total_tax_value = total_tax_value;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public String getDiscount_title() {
            return discount_title;
        }

        public void setDiscount_title(String discount_title) {
            this.discount_title = discount_title;
        }

        public double getTotal_discount_value() {
            return total_discount_value;
        }

        public void setTotal_discount_value(double total_discount_value) {
            this.total_discount_value = total_discount_value;
        }

        public double getTotal_after_calc() {
            return total_after_calc;
        }

        public void setTotal_after_calc(double total_after_calc) {
            this.total_after_calc = total_after_calc;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getNet_total() {
            return net_total;
        }

        public void setNet_total(double net_total) {
            this.net_total = net_total;
        }
    }
}
