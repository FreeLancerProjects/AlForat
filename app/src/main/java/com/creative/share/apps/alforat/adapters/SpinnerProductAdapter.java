package com.creative.share.apps.alforat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.databinding.SpinnerProductRowBinding;
import com.creative.share.apps.alforat.models.ProductDataModel;

import java.util.List;

public class SpinnerProductAdapter extends BaseAdapter {
    private Context context;
    private List<ProductDataModel.ProductModel> productModelList;

    public SpinnerProductAdapter(Context context, List<ProductDataModel.ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }

    @Override
    public int getCount() {
        return productModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return productModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        @SuppressLint("ViewHolder") SpinnerProductRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_product_row,viewGroup,false);
        binding.setProductModel(productModelList.get(i));
        return binding.getRoot();
    }
}
