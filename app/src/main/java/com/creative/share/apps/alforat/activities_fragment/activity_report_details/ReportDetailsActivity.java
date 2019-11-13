package com.creative.share.apps.alforat.activities_fragment.activity_report_details;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_client.ClientActivity;
import com.creative.share.apps.alforat.adapters.ReportAdapter;
import com.creative.share.apps.alforat.databinding.ActivityReportDetailsBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.ClientsDataModel;
import com.creative.share.apps.alforat.models.ReportDataModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.share.Common;
import com.creative.share.apps.alforat.tags.Tags;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailsActivity extends AppCompatActivity implements Listeners.BackListener,DatePickerDialog.OnDateSetListener {
    private ActivityReportDetailsBinding binding;
    private String lang;
    private ReportAdapter adapter;
    private List<ReportDataModel.ReportModel> reportModelList;
    private ClientsDataModel.ClientModel clientModel = null;
    private DatePickerDialog datePickerDialog;
    private int selected_date = 0;
    private String from_date ="",to_date="";
    private Preferences preferences;
    private UserModel userModel;
    private int type=0;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_details);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            type = intent.getIntExtra("type",0);
            if (intent.hasExtra("client"))
            {
                clientModel = (ClientsDataModel.ClientModel) intent.getSerializableExtra("client");
            }

        }
    }


    private void initView() {

        reportModelList = new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        adapter = new ReportAdapter(this,reportModelList);

        binding.recView.setLayoutManager(new LinearLayoutManager(this));

        binding.recView.setAdapter(adapter);

        binding.tvClientName.setOnClickListener(view -> {
            Intent intent = new Intent(this, ClientActivity.class);
            intent.putExtra("client",true);
            startActivityForResult(intent,100);
        });

        binding.tvFromDate.setOnClickListener(view -> {
            selected_date = 1;
            datePickerDialog.show(getFragmentManager(),"");
        });

        binding.tvToDate.setOnClickListener(view -> {
            selected_date = 2;
            datePickerDialog.show(getFragmentManager(),"");
        });

        binding.llSearch.setOnClickListener(view -> {

            if (userModel!=null)
            {

                if (type ==1)
                {
                    if (clientModel!=null&&!from_date.isEmpty()&&!to_date.isEmpty())
                    {
                        binding.tvClientName.setError(null);
                        binding.tvFromDate.setError(null);
                        binding.tvToDate.setError(null);
                        searchClientAccount();
                    }else
                    {
                        if (clientModel== null)
                        {
                            binding.tvClientName.setError(getString(R.string.field_req));

                        }else
                        {
                            binding.tvClientName.setError(null);

                        }

                        if (from_date.isEmpty())
                        {
                            binding.tvFromDate.setError(getString(R.string.field_req));

                        }else
                        {
                            binding.tvFromDate.setError(null);

                        }

                        if (to_date.isEmpty())
                        {
                            binding.tvToDate.setError(getString(R.string.field_req));

                        }else
                        {
                            binding.tvToDate.setError(null);

                        }
                    }
                }else if (type==2)
                {
                    if (!from_date.isEmpty()&&!to_date.isEmpty())
                    {
                        binding.tvFromDate.setError(null);
                        binding.tvToDate.setError(null);
                        searchMyAccount();
                    }else
                    {

                        if (from_date.isEmpty())
                        {
                            binding.tvFromDate.setError(getString(R.string.field_req));

                        }else
                        {
                            binding.tvFromDate.setError(null);

                        }

                        if (to_date.isEmpty())
                        {
                            binding.tvToDate.setError(getString(R.string.field_req));

                        }else
                        {
                            binding.tvToDate.setError(null);

                        }
                    }
                }

            }else
                {
                    Common.CreateNoSignAlertDialog(this);
                }

        });

        if (type==1)
        {
            binding.tvClientName.setText(clientModel.getClient_name());
        }
        else if (type==2)
        {
            binding.tvClientName.setText(userModel.getUser_name());
        }

        createDatePickerDialog();




    }

    private void searchClientAccount()
    {

        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);

        Api.getService(Tags.base_url)
                .getClientReport(userModel.getToken(),clientModel.getClient_acc_code(),from_date,to_date)
                .enqueue(new Callback<ReportDataModel>() {
                    @Override
                    public void onResponse(Call<ReportDataModel> call, Response<ReportDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {


                            if (response.body().getData().size() > 0) {
                                binding.tvNoData.setVisibility(View.GONE);
                                binding.ll.setVisibility(View.VISIBLE);
                                calculateBalance(response.body().getData());

                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);

                                binding.ll.setVisibility(View.VISIBLE);

                            }


                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(ReportDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ReportDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ReportDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ReportDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void searchMyAccount()
    {
        binding.progBar.setVisibility(View.VISIBLE);
        binding.tvNoData.setVisibility(View.GONE);
        Api.getService(Tags.base_url)
                .getMyReport(userModel.getToken(),from_date,to_date)
                .enqueue(new Callback<ReportDataModel>() {
                    @Override
                    public void onResponse(Call<ReportDataModel> call, Response<ReportDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {


                            if (response.body().getData().size() > 0) {
                                binding.tvNoData.setVisibility(View.GONE);

                                binding.ll.setVisibility(View.VISIBLE);
                                calculateBalance(response.body().getData());
                            } else {
                                binding.tvNoData.setVisibility(View.VISIBLE);

                                binding.ll.setVisibility(View.VISIBLE);

                            }


                        } else {

                            try {

                                Log.e("error", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (response.code() == 500) {
                                Toast.makeText(ReportDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(ReportDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportDataModel> call, Throwable t) {
                        try {
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage());
                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                    Toast.makeText(ReportDetailsActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ReportDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

    }


    private void calculateBalance(List<ReportDataModel.ReportModel> data) {
        reportModelList.clear();
        double balance = 0;

        for (ReportDataModel.ReportModel reportModel:data)
        {
            balance = balance+(reportModel.getOp_madeen()-Double.parseDouble(reportModel.getOp_dayeen()));
            reportModel.setBalance(balance);
            reportModelList.add(reportModel);
        }

        adapter.notifyDataSetChanged();

    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.MONTH,0);
        calendar.set(Calendar.YEAR,2018);

        datePickerDialog = DatePickerDialog.newInstance(this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setAccentColor(ActivityCompat.getColor(this,R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(this,R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(this,R.color.colorPrimary));
        datePickerDialog.setOkText(getString(R.string.select));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(lang));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==100&&resultCode==RESULT_OK&&data!=null)
        {
            if (data.hasExtra("client"))
            {
                clientModel = (ClientsDataModel.ClientModel) data.getSerializableExtra("client");
                binding.tvClientName.setText(clientModel.getClient_name());
            }
        }
    }

    @Override
    public void back() {
        finish();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if (selected_date==1)
        {
            from_date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
            binding.tvFromDate.setText(from_date);
        }else
            {
                to_date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                binding.tvToDate.setText(to_date);

            }
    }
}
