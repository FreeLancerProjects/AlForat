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
import com.creative.share.apps.alforat.activities_fragment.activity_permissions.PermissionsActivity;
import com.creative.share.apps.alforat.databinding.ChargeRowBinding;
import com.creative.share.apps.alforat.databinding.ProgressLoadRowBinding;
import com.creative.share.apps.alforat.models.ChargeReportDataModel;

import java.util.List;

public class ChargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<ChargeReportDataModel.ChargeModel> list;
    private Context context;
    private PermissionsActivity activity;

    public ChargeAdapter(List<ChargeReportDataModel.ChargeModel> list, Context context) {

        this.list = list;
        this.context = context;
        this.activity = (PermissionsActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            ChargeRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.charge_row,parent,false);
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
            ChargeReportDataModel.ChargeModel chargeModel = list.get(position);
            myHolder.binding.setChargeModel(chargeModel);
            myHolder.itemView.setOnClickListener(view -> {
                ChargeReportDataModel.ChargeModel chargeModel2 = list.get(myHolder.getAdapterPosition());
                activity.setItemData(chargeModel2);
            });

        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       private ChargeRowBinding binding;
        public MyHolder(ChargeRowBinding binding) {
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
        ChargeReportDataModel.ChargeModel chargeModel = list.get(position);
        if (chargeModel == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }



    }
}
