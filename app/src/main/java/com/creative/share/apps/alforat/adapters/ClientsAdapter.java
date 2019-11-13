package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_client.ClientActivity;
import com.creative.share.apps.alforat.databinding.ClientRowBinding;
import com.creative.share.apps.alforat.databinding.ProgressLoadRowBinding;
import com.creative.share.apps.alforat.models.ClientsDataModel;

import java.util.List;

public class ClientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<ClientsDataModel.ClientModel> clientModelList;
    private Context context;
    private ClientActivity activity;

    public ClientsAdapter(List<ClientsDataModel.ClientModel> clientModelList, Context context) {

        this.clientModelList = clientModelList;
        this.context = context;
        this.activity = (ClientActivity) context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            ClientRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.client_row,parent,false);
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
            ClientsDataModel.ClientModel clientModel = clientModelList.get(position);
            myHolder.binding.setClient(clientModel);
            myHolder.itemView.setOnClickListener(view -> {
                ClientsDataModel.ClientModel clientModel1 = clientModelList.get(myHolder.getAdapterPosition());
                activity.setItemData(clientModel1);

            });

        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.binding.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return clientModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       private ClientRowBinding binding;
        public MyHolder(ClientRowBinding binding) {
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
        ClientsDataModel.ClientModel clientModel = clientModelList.get(position);
        if (clientModel == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }



    }
}
