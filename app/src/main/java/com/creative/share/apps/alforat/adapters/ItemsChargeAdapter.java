package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.databinding.ItemChargeRowBinding;
import com.creative.share.apps.alforat.models.ChargeReportDataModel;
import com.creative.share.apps.alforat.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemsChargeAdapter extends RecyclerView.Adapter<ItemsChargeAdapter.MyHolder> {

    private List<ChargeReportDataModel.Details> list;
    private Context context;
    public ItemsChargeAdapter(List<ChargeReportDataModel.Details> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChargeRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_charge_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        ChargeReportDataModel.Details details = list.get(position);
        holder.binding.setDetailsModel(details);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+details.getItem_image())).fit().into(holder.binding.image);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ItemChargeRowBinding binding;
        public MyHolder(@NonNull ItemChargeRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
