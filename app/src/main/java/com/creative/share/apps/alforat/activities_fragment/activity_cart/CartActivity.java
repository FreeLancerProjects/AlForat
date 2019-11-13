package com.creative.share.apps.alforat.activities_fragment.activity_cart;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.adapters.CartAdapter;
import com.creative.share.apps.alforat.databinding.ActivityCartBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ItemCartModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.share.Common;
import com.creative.share.apps.alforat.singleton.CartSingleTon;
import com.creative.share.apps.alforat.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;
    private String lang;
    private CartSingleTon cartSingleTon;
    private ItemCartModel itemCartModel;
    private CartAdapter adapter;
    private UserModel userModel;
    private Preferences preferences;

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
        preferences  = Preferences.newInstance();
        userModel = preferences.getUserData(this);
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
            binding.tvBillCost.setText(cartSingleTon.getTotalBillCost(itemCartModel.isGeneralOffer(),itemCartModel.getGeneral_offer_value())+" "+getString(R.string.sar));
            binding.recView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new CartAdapter(cartSingleTon.getItemCartModelList(),this,itemCartModel.isGeneralOffer());
            binding.recView.setAdapter(adapter);

        }else
            {
                binding.llEmptyCart.setVisibility(View.VISIBLE);
                binding.btnUpload.setVisibility(View.GONE);
                binding.constClientData.setVisibility(View.GONE);

            }


        binding.btnUpload.setOnClickListener((v)->addBill());



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

    public void increase_decrease(ItemCartModel.Items item, int adapterPosition) {

        Log.e("bonus",item.getItem_bouns()+"__");
        cartSingleTon.updateItem(item,adapterPosition);
        binding.tvBillCost.setText(cartSingleTon.getTotalBillCost(itemCartModel.isGeneralOffer(),itemCartModel.getGeneral_offer_value())+" "+getString(R.string.sar));

    }




    private void addBill()
    {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(Tags.base_url)
                    .addBill(userModel.getToken(),itemCartModel)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() ) {

                                cartSingleTon.clear();
                                adapter.notifyDataSetChanged();
                                binding.llEmptyCart.setVisibility(View.VISIBLE);
                                binding.btnUpload.setVisibility(View.GONE);
                                binding.constClientData.setVisibility(View.GONE);

                                finish();

                                Toast.makeText(CartActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(CartActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(CartActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(CartActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {

                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();
        }
    }

}
