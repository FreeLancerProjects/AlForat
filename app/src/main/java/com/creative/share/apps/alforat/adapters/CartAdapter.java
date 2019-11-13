package com.creative.share.apps.alforat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private boolean isGeneral_offer;
    public CartAdapter(List<ItemCartModel.Items> itemList, Context context,boolean isGeneral_offer) {
        this.itemList = itemList;
        this.context = context;
        this.activity = (CartActivity) context;
        this.isGeneral_offer = isGeneral_offer;
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

        holder.binding.imageIncrease.setOnClickListener(view -> {
            ItemCartModel.Items item2 = itemList.get(holder.getAdapterPosition());
            int amount = item2.getItem_amount()+1;

            if (amount>item.getCurrent_amount())
            {
                Toast.makeText(context, context.getString(R.string.inv_amount), Toast.LENGTH_SHORT).show();
            }else
            {
                item2.setItem_amount(amount);

                if (isGeneral_offer)
                {
                    item2.setItem_bouns(0);
                }else
                    {

                        if (item.getLimit()!=0)
                        {
                            if (amount%item2.getLimit()==0)
                            {
                                int bonus_value = (amount/item2.getLimit())*item2.getOffer_value();

                                item2.setItem_bouns(bonus_value);

                            }else
                            {
                                int bonus_value = (amount/item2.getLimit())*item2.getOffer_value();
                                item2.setItem_bouns(bonus_value);

                            }
                        }else
                            {
                                item2.setItem_bouns(0);
                            }






            }

                holder.binding.tvAmount.setText(String.valueOf(amount));

                activity.increase_decrease(item2,holder.getAdapterPosition());

            }

            holder.binding.setItem(item2);



        });

        holder.binding.imageDecrease.setOnClickListener(view -> {
            ItemCartModel.Items item2 = itemList.get(holder.getAdapterPosition());
            int amount = item2.getItem_amount()-1;

            if (amount>item.getCurrent_amount())
            {
                Toast.makeText(context, context.getString(R.string.inv_amount), Toast.LENGTH_SHORT).show();
            }else
            {
                item2.setItem_amount(amount);

                if (isGeneral_offer)
                {
                    item2.setItem_bouns(0);
                }else
                {


                    if (item2.getLimit()!=0)
                    {
                        if (amount%item2.getLimit()==0)
                        {
                            int bonus_value = (amount/item2.getLimit())*item2.getOffer_value();

                            item2.setItem_bouns(bonus_value);

                        }else
                        {
                            int bonus_value = (amount/item2.getLimit())*item2.getOffer_value();
                            item2.setItem_bouns(bonus_value);

                        }
                    }else
                        {
                            item2.setItem_bouns(0);
                        }





                }

                holder.binding.tvAmount.setText(String.valueOf(amount));

                activity.increase_decrease(item2,holder.getAdapterPosition());

            }

            holder.binding.setItem(item2);



        });

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
