package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.databinding.ReportRowBinding;
import com.creative.share.apps.alforat.models.ReportDataModel;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyHolder> {
    private Context context;
    private List<ReportDataModel.ReportModel> list;

    public ReportAdapter(Context context, List<ReportDataModel.ReportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReportRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.report_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ReportDataModel.ReportModel reportModel = list.get(position);
        holder.binding.setReportModel(reportModel);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ReportRowBinding binding;
        public MyHolder(@NonNull ReportRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
