package com.creative.share.apps.alforat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.databinding.SpinnerPriceRowBinding;
import com.creative.share.apps.alforat.models.ProductDataModel;

import java.util.List;

public class PriceAdapter extends BaseAdapter {
    private Context context;
    private List<ProductDataModel.Price> priceList;

    public PriceAdapter(Context context, List<ProductDataModel.Price> priceList) {
        this.context = context;
        this.priceList = priceList;
    }

    @Override
    public int getCount() {
        return priceList.size();
    }

    @Override
    public Object getItem(int i) {
        return priceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        @SuppressLint("ViewHolder") SpinnerPriceRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_price_row,viewGroup,false);
        binding.setPriceModel(priceList.get(i));
        return binding.getRoot();
    }
}
