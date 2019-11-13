package com.creative.share.apps.alforat.singleton;

import com.creative.share.apps.alforat.models.ItemCartModel;

import java.util.ArrayList;
import java.util.List;

public class CartSingleTon {

    private static CartSingleTon instance = null;
    private static List<ItemCartModel.Items> itemCartModelList = new ArrayList<>();

    private CartSingleTon() {
    }

    public static synchronized CartSingleTon newInstance() {
        if (instance == null) {
            instance = new CartSingleTon();

        }

        return instance;
    }

    public void addItem(ItemCartModel.Items item) {
        int pos = getItemPos(item);
        if (pos == -1) {
            itemCartModelList.add(item);
        } else {
            itemCartModelList.set(pos, item);
        }

    }

    private int getItemPos(ItemCartModel.Items item) {
        int pos = -1;
        for (int i = 0; i < itemCartModelList.size(); i++) {
            if (itemCartModelList.get(i).getItem_id_fk().equals(item.getItem_id_fk())) {
                pos = i;
                return pos;
            }
        }

        return pos;
    }

    public void removeItem(int pos) {
        itemCartModelList.remove(pos);
    }

    public int totalItems() {
        return itemCartModelList.size();
    }

    public void clear() {
        itemCartModelList.clear();
        instance = null;
    }

    public double getTotalItemCost() {
        double cost = 0.0;
        for (ItemCartModel.Items items : itemCartModelList) {
            cost = cost + (items.getItem_amount() * (Double.parseDouble(items.getPrice_value())+Double.parseDouble(items.getTax_value())));
        }
        return cost;
    }

    public double getTotalItemTax() {
        double tax = 0.0;
        for (ItemCartModel.Items items : itemCartModelList) {
            tax = tax + (items.getItem_amount() * Double.parseDouble(items.getTax_value()));
        }
        return tax;
    }

    public double getTotalBillCost(boolean isGeneralOffer, double offer) {
        double total_cost;

        if (isGeneralOffer) {
            total_cost = getTotalItemCost() - (getTotalItemCost() * (offer / 100));

        } else {
            total_cost = getTotalItemCost();
        }
        return total_cost;
    }

    public static List<ItemCartModel.Items> getItemCartModelList() {
        return itemCartModelList;
    }

    public static void setItemCartModelList(List<ItemCartModel.Items> itemCartModelList) {
        CartSingleTon.itemCartModelList = itemCartModelList;
    }

    public void updateItem(ItemCartModel.Items items, int pos)
    {
        itemCartModelList.set(pos,items);
    }
}
