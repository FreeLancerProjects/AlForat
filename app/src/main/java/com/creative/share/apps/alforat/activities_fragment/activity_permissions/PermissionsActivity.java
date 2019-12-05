package com.creative.share.apps.alforat.activities_fragment.activity_permissions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_charge_details.ChargeDetailsActivity;
import com.creative.share.apps.alforat.adapters.ChargeAdapter;
import com.creative.share.apps.alforat.databinding.ActivityPermissionsBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ChargeReportDataModel;
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

public class PermissionsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityPermissionsBinding binding;
    private String lang;
    private List<ChargeReportDataModel.ChargeModel> chargeModelList;
    private UserModel userModel;
    private Preferences preferences;
    private int current_page = 1;
    private boolean isLoading=false;
    private ChargeAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_permissions);

        initView();

    }




    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        chargeModelList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        adapter = new ChargeAdapter(chargeModelList,this);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int total_items = adapter.getItemCount();
                    int lastVisibleItem = ((LinearLayoutManager)(binding.recView.getLayoutManager())).findLastCompletelyVisibleItemPosition();

                    if (lastVisibleItem > 5 && lastVisibleItem == (total_items - 2) && !isLoading) {
                        isLoading = true;
                        int page = current_page + 1;
                        chargeModelList.add(null);
                        adapter.notifyDataSetChanged();
                        loadMore(page);

                    }
                }
            }
        });
        getCharge();

    }


    private void getCharge()
    {

        Api.getService(Tags.base_url)
                .getChargeData(userModel.getToken(), 1)
                .enqueue(new Callback<ChargeReportDataModel>() {
                    @Override
                    public void onResponse(Call<ChargeReportDataModel> call, Response<ChargeReportDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            chargeModelList.clear();
                            chargeModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();

                            if (chargeModelList.size() > 0) {
                                binding.tvNoData.setVisibility(View.GONE);
                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);

                            }
                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(PermissionsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(PermissionsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChargeReportDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(PermissionsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PermissionsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void loadMore(int page)
    {
        Api.getService(Tags.base_url)
                .getChargeData(userModel.getToken(), page)
                .enqueue(new Callback<ChargeReportDataModel>() {
                    @Override
                    public void onResponse(Call<ChargeReportDataModel> call, Response<ChargeReportDataModel> response) {
                        isLoading = false;
                        chargeModelList.remove(chargeModelList.size() - 1);
                        adapter.notifyDataSetChanged();
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            current_page = response.body().getMeta().getCurrent_page();
                            chargeModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(PermissionsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(PermissionsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChargeReportDataModel> call, Throwable t) {
                        try {
                            chargeModelList.remove(chargeModelList.size() - 1);
                            isLoading = false;
                            adapter.notifyDataSetChanged();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(PermissionsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PermissionsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }

    @Override
    public void back() {
        finish();
    }

    public void setItemData(ChargeReportDataModel.ChargeModel chargeModel) {
        Intent intent = new Intent(this, ChargeDetailsActivity.class);
        intent.putExtra("data",chargeModel);
        startActivity(intent);
    }
}
