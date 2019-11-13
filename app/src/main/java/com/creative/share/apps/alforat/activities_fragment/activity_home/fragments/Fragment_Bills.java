package com.creative.share.apps.alforat.activities_fragment.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_bill_details.BillDetailsActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_home.activity.HomeActivity;
import com.creative.share.apps.alforat.adapters.BillAdapter;
import com.creative.share.apps.alforat.databinding.FragmentBillsBinding;
import com.creative.share.apps.alforat.models.BillDataModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Bills extends Fragment {
    private HomeActivity activity;
    private FragmentBillsBinding binding;
    private List<BillDataModel.BillModel> billModelList;
    private BillAdapter adapter;
    private int current_page = 1;
    private boolean isLoading = false;
    private Preferences preferences;
    private UserModel userModel;


    public static Fragment_Bills newInstance() {
        return new Fragment_Bills();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bills, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        billModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));

        adapter = new BillAdapter(billModelList,activity,this);
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
                        billModelList.add(null);
                        adapter.notifyDataSetChanged();
                        loadMore(page);

                    }
                }
            }
        });
        getBills();



    }

    private void getBills()
    {

        Api.getService(Tags.base_url)
                .getBills(userModel.getToken(), 1)
                .enqueue(new Callback<BillDataModel>() {
                    @Override
                    public void onResponse(Call<BillDataModel> call, Response<BillDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                            billModelList.clear();
                            billModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();

                            if (billModelList.size() > 0) {
                                binding.tvNoBill.setVisibility(View.GONE);
                            } else {
                                binding.tvNoBill.setVisibility(View.VISIBLE);

                            }
                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BillDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                .getBills(userModel.getToken(), page)
                .enqueue(new Callback<BillDataModel>() {
                    @Override
                    public void onResponse(Call<BillDataModel> call, Response<BillDataModel> response) {
                        isLoading = false;
                        billModelList.remove(billModelList.size() - 1);
                        adapter.notifyDataSetChanged();
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            current_page = response.body().getMeta().getCurrent_page();
                            billModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                        } else {
                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BillDataModel> call, Throwable t) {
                        try {
                            billModelList.remove(billModelList.size() - 1);
                            isLoading = false;
                            adapter.notifyDataSetChanged();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }


    public void setItemData(BillDataModel.BillModel billModel) {

        Intent intent = new Intent(activity, BillDetailsActivity.class);
        intent.putExtra("data",billModel);
        startActivity(intent);

    }
}
