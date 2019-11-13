package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_cart.CartActivity;
import com.creative.share.apps.alforat.databinding.ItemCartRowBinding;
import com.creative.share.apps.alforat.models.ItemCartModel;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    private List<ItemCartModel.Items> itemList;
    private Context context;
    private CartActivity activity;
    public CartAdapter(List<ItemCartModel.Items> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        activity = (CartActivity) context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_cart_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ItemCartModel.Items item = itemList.get(position);
        holder.binding.setItem(item);
        holder.binding.imageDelete.setOnClickListener(view -> activity.removeItem(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ItemCartRowBinding binding;
        public MyHolder(@NonNull ItemCartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
