package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_item_details.ItemDetailsActivity;
import com.creative.share.apps.alforat.databinding.PriceRowBinding;
import com.creative.share.apps.alforat.models.ProductDataModel;

import java.util.List;

public class PricesAdapter extends RecyclerView.Adapter<PricesAdapter.MyHolder> {

    private List<ProductDataModel.Price> list;
    private Context context;
    private int selected_pos = -1;
    private ItemDetailsActivity activity;
    public PricesAdapter(List<ProductDataModel.Price> list, Context context) {
        this.list = list;
        this.context = context;
        this.activity = (ItemDetailsActivity) context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PriceRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.price_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        ProductDataModel.Price price = list.get(position);
        holder.binding.setPriceModel(price);

        if (selected_pos == position)
        {
            holder.binding.tvPrice.setBackgroundResource(R.drawable.price_selected);
        }else
            {
                holder.binding.tvPrice.setBackgroundResource(R.drawable.price_unselected);

            }

        holder.itemView.setOnClickListener(view -> {
            ProductDataModel.Price price2 = list.get(holder.getAdapterPosition());
            activity.setItemData(price2);
            selected_pos = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private PriceRowBinding binding;
        public MyHolder(@NonNull PriceRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
