package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.databinding.OfferRowBinding;
import com.creative.share.apps.alforat.models.OfferDataModel;
import com.creative.share.apps.alforat.tags.Tags;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyHolder> {

    private List<OfferDataModel.Offer> list;
    private Context context;
    public OfferAdapter(List<OfferDataModel.Offer> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.offer_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        OfferDataModel.Offer offer = list.get(position);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+offer.getOffer_image())).fit().into(holder.binding.image, new Callback() {
            @Override
            public void onSuccess() {
                holder.binding.progBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private OfferRowBinding binding;
        public MyHolder(@NonNull OfferRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
