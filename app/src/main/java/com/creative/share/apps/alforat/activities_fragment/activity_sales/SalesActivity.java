package com.creative.share.apps.alforat.activities_fragment.activity_sales;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_cart.CartActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_item_details.ItemDetailsActivity;
import com.creative.share.apps.alforat.adapters.ProductSalesAdapter;
import com.creative.share.apps.alforat.databinding.ActivitySalesBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ClientsDataModel;
import com.creative.share.apps.alforat.models.ItemCartModel;
import com.creative.share.apps.alforat.models.ProductDataModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.singleton.CartSingleTon;
import com.creative.share.apps.alforat.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivitySalesBinding binding;
    private String lang;
    private GridLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private List<ProductDataModel.ProductModel> productModelList;
    private ProductSalesAdapter adapter;
    private ClientsDataModel.ClientModel clientModel = null;
    private CartSingleTon cartSingleTon;
    private ItemCartModel itemCartModel;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sales);
        getDataFromIntent();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cartSingleTon = CartSingleTon.newInstance();
        if (cartSingleTon.totalItems()>0)
        {
            binding.tvCount.setText(String.valueOf(cartSingleTon.totalItems()));
        }else
            {
                binding.tvCount.setText("0");

            }

    }


    private void initView() {
        itemCartModel = new ItemCartModel();
        itemCartModel.setTotal_discount(0);
        productModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new GridLayoutManager(this,2);
        binding.recView.setLayoutManager(manager);
        adapter = new ProductSalesAdapter(this,productModelList);
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getProducts);
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorPrimary),ContextCompat.getColor(this,R.color.red),ContextCompat.getColor(this,R.color.blue),ContextCompat.getColor(this,R.color.yellow),ContextCompat.getColor(this,R.color.black));

        binding.llCart.setOnClickListener(view -> {
            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra("data",itemCartModel);
            startActivityForResult(intent,200);
        });
        getProducts();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("client"))
        {
            clientModel = (ClientsDataModel.ClientModel) intent.getSerializableExtra("client");
        }
    }

    private void getProducts() {

        try {
            Api.getService(Tags.base_url)
                    .getSalesProducts(userModel.getToken(),clientModel.getId_client(),clientModel.getDelegate_id_fk(),clientModel.getDate())
                    .enqueue(new Callback<ProductDataModel>() {
                        @Override
                        public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                            binding.progBar.setVisibility(View.GONE);
                            binding.swipeRefresh.setRefreshing(false);
                            if (response.isSuccessful() && response.body() != null&&response.body().getData()!=null) {
                                productModelList.clear();
                                productModelList.addAll(response.body().getData());
                                if (productModelList.size()>0)
                                {
                                    binding.setStoreTitle(productModelList.get(0).getStorage_title());
                                    binding.llNoProducts.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }else
                                {
                                    binding.llNoProducts.setVisibility(View.VISIBLE);

                                }

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(SalesActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(SalesActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductDataModel> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);

                                binding.swipeRefresh.setRefreshing(false);

                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(SalesActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SalesActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {

        }
    }

    @Override
    public void back() {
        finish();
    }

    public void setItemData(ProductDataModel.ProductModel productModel) {

        itemCartModel.setClient_acc_code(clientModel.getClient_acc_code());
        itemCartModel.setId_client(clientModel.getId_client());
        itemCartModel.setClient_name(clientModel.getClient_name());
        itemCartModel.setClient_phone(clientModel.getClient_phone());
        itemCartModel.setClient_address(clientModel.getCleint_address());
        itemCartModel.setArea_id_fk(clientModel.getArea_id_fk());
        Intent intent = new Intent(this, ItemDetailsActivity.class);
        intent.putExtra("data",productModel);
        startActivityForResult(intent,100);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK&&data!=null)
        {
            boolean isGeneralOffer = data.getBooleanExtra("isGeneralOffer",false);
            double offer = data.getDoubleExtra("offer",0.0);
            String general_offer_type = data.getStringExtra(" general_offer_type");
            itemCartModel.setGeneralOffer(isGeneralOffer);
            itemCartModel.setGeneral_offer_value(offer);
            itemCartModel.setGeneral_offer_type(general_offer_type);

            itemCartModel.setCart(cartSingleTon.getItemCartModelList());
            itemCartModel.setTotal_tax(cartSingleTon.getTotalItemTax());

            binding.tvCount.setText(String.valueOf(cartSingleTon.totalItems()));

        }else if (requestCode==200&&resultCode==RESULT_OK&&data!=null)
        {
            cartSingleTon.clear();
            binding.tvCount.setText("0");
            getProducts();

        }
    }



}
