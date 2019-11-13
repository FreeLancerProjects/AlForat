package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_add_client.AddClientActivity;
import com.creative.share.apps.alforat.databinding.ImageRowBinding;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyHolder> {

    private List<String> urlList;
    private Context context;
    private AddClientActivity activity;
    public ImageAdapter(List<String> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;
        activity = (AddClientActivity) context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageRowBinding imageRowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.image_row,parent,false);
        return new MyHolder(imageRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String url = urlList.get(position);
        holder.imageRowBinding.setUrl(url);
        holder.imageRowBinding.imageDelete.setOnClickListener(view -> activity.deleteImage(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return urlList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageRowBinding imageRowBinding;
        public MyHolder(@NonNull ImageRowBinding imageRowBinding) {
            super(imageRowBinding.getRoot());
            this.imageRowBinding = imageRowBinding;
        }
    }

}
