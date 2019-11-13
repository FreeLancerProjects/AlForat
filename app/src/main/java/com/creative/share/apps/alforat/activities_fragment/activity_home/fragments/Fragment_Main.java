package com.creative.share.apps.alforat.activities_fragment.activity_home.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_client.ClientActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_home.activity.HomeActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_offers.OffersActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_reports.ReportsActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_sales.SalesActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_store.StoreActivity;
import com.creative.share.apps.alforat.databinding.FragmentMainBinding;
import com.creative.share.apps.alforat.models.ClientsDataModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.share.Common;

import io.paperdb.Paper;

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private Preferences preferences;
    private UserModel userModel;




    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);

        binding.llReports.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ReportsActivity.class);
            startActivity(intent);
        });

        binding.llOffer.setOnClickListener(view -> {
            Intent intent = new Intent(activity, OffersActivity.class);
            startActivity(intent);
        });

        binding.llSales.setOnClickListener(view -> {

            Intent intent = new Intent(activity, ClientActivity.class);
            intent.putExtra("client",true);
            startActivityForResult(intent,100);


        });
        binding.llClients.setOnClickListener(view -> {
            if (userModel!=null)
            {
                Intent intent = new Intent(activity, ClientActivity.class);
                startActivity(intent);
            }else
                {
                    Common.CreateNoSignAlertDialog(activity);
                }

        });

/*
        binding.llExpenses.setOnClickListener(view -> {

            if (userModel!=null)
            {
                Intent intent = new Intent(activity, ExpensesActivity.class);
                startActivity(intent);
            }else
            {
                Common.CreateNoSignAlertDialog(activity);
            }


        });
*/

        binding.llStore.setOnClickListener(view -> {
            if (userModel!=null)
            {
                Intent intent = new Intent(activity, StoreActivity.class);
                startActivity(intent);
            }else
            {
                Common.CreateNoSignAlertDialog(activity);
            }

        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==100&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            if (data.hasExtra("client"))
            {
                ClientsDataModel.ClientModel clientModel = (ClientsDataModel.ClientModel) data.getSerializableExtra("client");
                Intent intent = new Intent(activity, SalesActivity.class);
                intent.putExtra("client",clientModel);
                startActivity(intent);

            }
        }
    }
}
