package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_home.fragments.Fragment_Bills;
import com.creative.share.apps.alforat.databinding.BillRowBinding;
import com.creative.share.apps.alforat.databinding.ProgressLoadRowBinding;
import com.creative.share.apps.alforat.models.BillDataModel;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<BillDataModel.BillModel> billModelList;
    private Context context;
    private Fragment_Bills fragment_bills;

    public BillAdapter(List<BillDataModel.BillModel> billModelList, Context context,Fragment_Bills fragment_bills) {

        this.billModelList = billModelList;
        this.context = context;
        this.fragment_bills = fragment_bills;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            BillRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.bill_row,parent,false);
            return new MyHolder(binding);
        } else {
            ProgressLoadRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.progress_load_row,parent,false);
            return new LoadMoreHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            final MyHolder myHolder = (MyHolder) holder;
            BillDataModel.BillModel billModel = billModelList.get(position);
            myHolder.binding.setBillModel(billModel);
            myHolder.itemView.setOnClickListener(view -> {
                BillDataModel.BillModel billModel1 = billModelList.get(myHolder.getAdapterPosition());
                fragment_bills.setItemData(billModel);
            });

        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return billModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       private BillRowBinding binding;
        public MyHolder(BillRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {

        private ProgressLoadRowBinding binding;
        public LoadMoreHolder(ProgressLoadRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemViewType(int position) {
        BillDataModel.BillModel billModel = billModelList.get(position);
        if (billModel == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }



    }
}
