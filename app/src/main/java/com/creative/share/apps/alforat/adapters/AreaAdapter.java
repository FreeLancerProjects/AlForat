package com.creative.share.apps.alforat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.databinding.SpinnerAreaRowBinding;
import com.creative.share.apps.alforat.models.AreaDataModel;

import java.util.List;

public class AreaAdapter extends BaseAdapter {
    private Context context;
    private List<AreaDataModel.AreaModel> areaModelList;

    public AreaAdapter(Context context, List<AreaDataModel.AreaModel> areaModelList) {
        this.context = context;
        this.areaModelList = areaModelList;
    }

    @Override
    public int getCount() {
        return areaModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return areaModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        @SuppressLint("ViewHolder") SpinnerAreaRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_area_row,viewGroup,false);
        binding.setAreaModel(areaModelList.get(i));
        return binding.getRoot();
    }
}
