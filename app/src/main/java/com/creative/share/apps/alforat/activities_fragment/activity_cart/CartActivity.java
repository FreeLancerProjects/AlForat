package com.creative.share.apps.alforat.activities_fragment.activity_cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.adapters.CartAdapter;
import com.creative.share.apps.alforat.databinding.ActivityCartBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ItemCartModel;
import com.creative.share.apps.alforat.singleton.CartSingleTon;

import java.util.Locale;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;
    private String lang;
    private CartSingleTon cartSingleTon;
    private ItemCartModel itemCartModel;
    private CartAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent  = getIntent();
        if (intent.hasExtra("data"))
        {
            itemCartModel = (ItemCartModel) intent.getSerializableExtra("data");
        }
    }


    private void initView() {

        cartSingleTon = CartSingleTon.newInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

        if (cartSingleTon.totalItems()>0)
        {
            binding.setCartData(itemCartModel);
            binding.constClientData.setVisibility(View.VISIBLE);
            binding.llEmptyCart.setVisibility(View.GONE);
            binding.btnUpload.setVisibility(View.VISIBLE);
            binding.tvBillCost.setText(String.valueOf(cartSingleTon.getTotalBillCost(itemCartModel.isGeneralOffer(),itemCartModel.getGeneral_offer_value())));
            binding.recView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CartAdapter(cartSingleTon.getItemCartModelList(),this);
            binding.recView.setAdapter(adapter);

        }else
            {
                binding.llEmptyCart.setVisibility(View.VISIBLE);
                binding.btnUpload.setVisibility(View.GONE);
                binding.constClientData.setVisibility(View.GONE);

            }





    }

    @Override
    public void back() {
        finish();
    }

    public void removeItem(int adapterPosition) {
        cartSingleTon.removeItem(adapterPosition);
        adapter.notifyItemRemoved(adapterPosition);
        if (cartSingleTon.totalItems()==0)
        {
            cartSingleTon.clear();
            binding.llEmptyCart.setVisibility(View.VISIBLE);
            binding.btnUpload.setVisibility(View.GONE);
            binding.constClientData.setVisibility(View.GONE);


        }
    }
}
