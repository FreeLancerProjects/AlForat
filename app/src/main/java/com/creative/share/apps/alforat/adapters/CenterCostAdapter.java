package com.creative.share.apps.alforat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.databinding.SpinnerCenterCostRowBinding;
import com.creative.share.apps.alforat.models.AreaDataModel;

import java.util.List;

public class CenterCostAdapter extends BaseAdapter {
    private Context context;
    private List<AreaDataModel.CenterCostModel> centerCostModelList;

    public CenterCostAdapter(Context context, List<AreaDataModel.CenterCostModel> centerCostModelList) {
        this.context = context;
        this.centerCostModelList = centerCostModelList;
    }

    @Override
    public int getCount() {
        return centerCostModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return centerCostModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        @SuppressLint("ViewHolder") SpinnerCenterCostRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.spinner_center_cost_row,viewGroup,false);
        binding.setCenterCostModel(centerCostModelList.get(i));
        return binding.getRoot();
    }
}
