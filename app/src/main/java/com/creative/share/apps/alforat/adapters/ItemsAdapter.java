package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.databinding.ItemRowBinding;
import com.creative.share.apps.alforat.models.BillDataModel;
import com.creative.share.apps.alforat.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyHolder> {

    private List<BillDataModel.Details> list;
    private Context context;
    public ItemsAdapter(List<BillDataModel.Details> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        BillDataModel.Details details = list.get(position);
        holder.binding.setDetailsModel(details);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+details.getItem_image())).fit().into(holder.binding.image);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ItemRowBinding binding;
        public MyHolder(@NonNull ItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
