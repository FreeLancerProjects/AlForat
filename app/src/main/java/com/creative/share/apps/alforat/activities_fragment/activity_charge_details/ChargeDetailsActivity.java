package com.creative.share.apps.alforat.activities_fragment.activity_charge_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.adapters.ItemsChargeAdapter;
import com.creative.share.apps.alforat.databinding.ActivityChargeDetailsBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ChargeReportDataModel;

import java.util.Locale;

import io.paperdb.Paper;

public class ChargeDetailsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityChargeDetailsBinding binding;
    private String lang;
    private ChargeReportDataModel.ChargeModel chargeModel;
    private ItemsChargeAdapter adapter;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_charge_details);
        getDataFromIntent();

        initView();

    }




    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setChargeModel(chargeModel);
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemsChargeAdapter(chargeModel.getDetails(),this);
        binding.recView.setAdapter(adapter);



    }

    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("data"))
        {
            chargeModel = (ChargeReportDataModel.ChargeModel) intent.getSerializableExtra("data");

        }

    }


    @Override
    public void back() {
        finish();
    }

}
