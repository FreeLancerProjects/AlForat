package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_sales.SalesActivity;
import com.creative.share.apps.alforat.databinding.ProductSalesRowBinding;
import com.creative.share.apps.alforat.models.ProductDataModel;

import java.util.List;

public class ProductSalesAdapter extends RecyclerView.Adapter<ProductSalesAdapter.MyHolder> {
    private Context context;
    private List<ProductDataModel.ProductModel> productModelList;
    private SalesActivity activity;

    public ProductSalesAdapter(Context context, List<ProductDataModel.ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
        activity = (SalesActivity) context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductSalesRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.product_sales_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ProductDataModel.ProductModel productModel = productModelList.get(position);
        holder.binding.setProduct(productModel);

        holder.binding.flAddItem.setOnClickListener(view -> {

            ProductDataModel.ProductModel productModel1 = productModelList.get(holder.getAdapterPosition());
            activity.setItemData(productModel1);



        });
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ProductSalesRowBinding binding;
        public MyHolder(@NonNull ProductSalesRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
