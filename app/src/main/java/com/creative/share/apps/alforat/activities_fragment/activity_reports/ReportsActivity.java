package com.creative.share.apps.alforat.activities_fragment.activity_reports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_client.ClientActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_permissions.PermissionsActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_report_details.ReportDetailsActivity;
import com.creative.share.apps.alforat.databinding.ActivityReportsBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ClientsDataModel;

import java.util.Locale;

import io.paperdb.Paper;

public class ReportsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityReportsBinding binding;
    private String lang;
    private ClientsDataModel.ClientModel clientModel = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reports);
        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);

        binding.llClientReport.setOnClickListener(view -> {

            Intent intent = new Intent(this, ClientActivity.class);
            intent.putExtra("client",true);
            startActivityForResult(intent,100);


        });

        binding.llMyReport.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReportDetailsActivity.class);
            intent.putExtra("type",2);
            startActivity(intent);
        });

        binding.llAllow.setOnClickListener(view -> {
            Intent intent = new Intent(this, PermissionsActivity.class);
            startActivity(intent);
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==100&&resultCode==RESULT_OK&&data!=null)
        {
            if (data.hasExtra("client"))
            {
                clientModel = (ClientsDataModel.ClientModel) data.getSerializableExtra("client");

                Intent intent = new Intent(this, ReportDetailsActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("client",clientModel);
                startActivity(intent);
            }
        }
    }


    @Override
    public void back() {
        finish();
    }

}
