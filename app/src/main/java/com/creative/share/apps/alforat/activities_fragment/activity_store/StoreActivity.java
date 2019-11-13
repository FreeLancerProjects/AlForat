package com.creative.share.apps.alforat.activities_fragment.activity_store;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.adapters.ProductsAdapter;
import com.creative.share.apps.alforat.databinding.ActivityStoreBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ProductDataModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends AppCompatActivity implements Listeners.BackListener {

    private ActivityStoreBinding binding;
    private String lang;
    private GridLayoutManager manager;
    private Preferences preferences;
    private UserModel userModel;
    private List<ProductDataModel.ProductModel> productModelList;
    private ProductsAdapter adapter;



    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_store);
        initView();
    }

    private void initView() {
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
        adapter = new ProductsAdapter(this,productModelList);
        binding.recView.setAdapter(adapter);
        binding.swipeRefresh.setOnRefreshListener(this::getProducts);
        binding.swipeRefresh.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorPrimary),ContextCompat.getColor(this,R.color.red),ContextCompat.getColor(this,R.color.blue),ContextCompat.getColor(this,R.color.yellow),ContextCompat.getColor(this,R.color.black));
        getProducts();
    }

    private void getProducts() {

        try {
            Api.getService(Tags.base_url)
                    .getStoreProducts(userModel.getToken())
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
                                    Toast.makeText(StoreActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(StoreActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(StoreActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(StoreActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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


}
