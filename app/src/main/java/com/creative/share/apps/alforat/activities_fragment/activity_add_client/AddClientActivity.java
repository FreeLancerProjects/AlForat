package com.creative.share.apps.alforat.activities_fragment.activity_add_client;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.adapters.AreaAdapter;
import com.creative.share.apps.alforat.adapters.ImageAdapter;
import com.creative.share.apps.alforat.databinding.ActivityAddClientBinding;
import com.creative.share.apps.alforat.databinding.DialogSelectImageBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.AddClientModel;
import com.creative.share.apps.alforat.models.AreaDataModel;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.share.Common;
import com.creative.share.apps.alforat.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddClientActivity extends AppCompatActivity implements Listeners.BackListener ,Listeners.ShowCountryDialogListener, OnCountryPickerListener,Listeners.AddClientListener {
    private ActivityAddClientBinding binding;
    private String lang;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri url = null;
    private List<String> urlList;
    private LinearLayoutManager manager;
    private ImageAdapter imageAdapter;
    private CountryPicker countryPicker;
    private AddClientModel addClientModel;
    private List<AreaDataModel.AreaModel> areaModelList;
    private Preferences preferences;
    private UserModel userModel;
    private AreaAdapter areaAdapter;
    private String selected_area_id = "0";


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_client);
        initView();
    }

    private void initView() {
        areaModelList = new ArrayList<>();
        areaModelList.add(new AreaDataModel.AreaModel(getString(R.string.ch_area)));
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        addClientModel = new AddClientModel();

        urlList = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setShowCountryListener(this);
        binding.setAddClientListener(this);
        binding.setAddClientModel(addClientModel);

        createCountryDialog();

        //manager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
       // binding.recView.setLayoutManager(manager);
        imageAdapter = new ImageAdapter(urlList,this);
        //binding.recView.setAdapter(imageAdapter);

       // binding.imageSelect.setOnClickListener(view -> CreateImageAlertDialog());

        areaAdapter = new AreaAdapter(this,areaModelList);
        binding.spinner.setAdapter(areaAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0)
                {
                    selected_area_id = "0";
                    addClientModel.setArea_id(selected_area_id);

                }
                else
                    {
                        selected_area_id = areaModelList.get(i).getId_area();
                        addClientModel.setArea_id(selected_area_id);


                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        getAreas();


    }

   /* private void addClientWithImage()
    {


        final ProgressDialog dialog = Common.createProgressDialog(AddClientActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            RequestBody name_part = Common.getRequestBodyText(addClientModel.getName());
            RequestBody balance_part = Common.getRequestBodyText(addClientModel.getBalance());
            RequestBody address_part = Common.getRequestBodyText(addClientModel.getAddress());
            RequestBody phone_part = Common.getRequestBodyText(addClientModel.getPhone_number());
            RequestBody tax_part = Common.getRequestBodyText(addClientModel.getTax_number());
            RequestBody commercial_part = Common.getRequestBodyText(addClientModel.getCommercial());
            RequestBody max_money_part = Common.getRequestBodyText(addClientModel.getMax_money());
            RequestBody area_part = Common.getRequestBodyText(selected_area_id);


            Api.getService(Tags.base_url)
                    .addWithImageClient(userModel.getToken(),name_part,balance_part,area_part,address_part,phone_part,tax_part,commercial_part,max_money_part,getImages())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(AddClientActivity.this,getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                Intent intent = getIntent();
                                if (intent!=null)
                                {
                                    setResult(RESULT_OK,intent);
                                }
                                finish();
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(AddClientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 401) {
                                    Toast.makeText(AddClientActivity.this, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(AddClientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(AddClientActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddClientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            dialog.dismiss();

        }

    }*/

    private void addClientWithoutImage()
    {


        final ProgressDialog dialog = Common.createProgressDialog(AddClientActivity.this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            RequestBody name_part = Common.getRequestBodyText(addClientModel.getName());
            //RequestBody balance_part = Common.getRequestBodyText(addClientModel.getBalance());
            RequestBody address_part = Common.getRequestBodyText(addClientModel.getAddress());
            RequestBody phone_part = Common.getRequestBodyText(addClientModel.getPhone_number());
           /* RequestBody tax_part = Common.getRequestBodyText(addClientModel.getTax_number());
            RequestBody commercial_part = Common.getRequestBodyText(addClientModel.getCommercial());
            RequestBody max_money_part = Common.getRequestBodyText(addClientModel.getMax_money());
          */  RequestBody area_part = Common.getRequestBodyText(selected_area_id);


            Api.getService(Tags.base_url)
                    .addWithoutImageClient(userModel.getToken(),name_part,area_part,address_part,phone_part)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                Intent intent = getIntent();
                                if (intent!=null)
                                {
                                    setResult(RESULT_OK,intent);
                                }
                                finish();
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(AddClientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                } else if (response.code() == 401) {
                                    Toast.makeText(AddClientActivity.this, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                }else {
                                    Toast.makeText(AddClientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(AddClientActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddClientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private List<MultipartBody.Part> getImages()
    {
        List<MultipartBody.Part> images = new ArrayList<>();
        for (String uri :urlList)
        {
            MultipartBody.Part image = Common.getMultiPart(this,Uri.parse(uri),"attachments[]");

            images.add(image);
        }
        return images;
    }

    private void getAreas() {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(Tags.base_url)
                    .getAreas(userModel.getToken())
                    .enqueue(new Callback<AreaDataModel>() {
                        @Override
                        public void onResponse(Call<AreaDataModel> call, Response<AreaDataModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null&& response.body().getAreas() != null) {

                                areaModelList.addAll(response.body().getAreas());
                                areaAdapter.notifyDataSetChanged();
                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(AddClientActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else {
                                    Toast.makeText(AddClientActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AreaDataModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(AddClientActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(AddClientActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void createCountryDialog() {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(this)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM()!=null)
        {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        }else if (telephonyManager!=null&&countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        }else if (countryPicker.getCountryByLocale(Locale.getDefault())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        }else
        {
            String code = "+966";
            binding.tvCode.setText(code);
            addClientModel.setPhone_code("00966");
            binding.setAddClientModel(addClientModel);

        }

    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(this);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);

    }

    private void updatePhoneCode(Country country)
    {
        binding.tvCode.setText(country.getDialCode());
        addClientModel.setPhone_code(country.getDialCode());
        binding.setAddClientModel(addClientModel);

    }

    private void CreateImageAlertDialog()
    {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogSelectImageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.dialog_select_image,null,false);



        binding.btnCamera.setOnClickListener(v -> {
            dialog.dismiss();
            Check_CameraPermission();

        });

        binding.btnGallery.setOnClickListener(v -> {
            dialog.dismiss();
            CheckReadPermission();



        });

        binding.btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    private void CheckReadPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission()
    {
        if (ContextCompat.checkSelfPermission(this,camera_permission)!= PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this,write_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{camera_permission,write_permission},IMG_REQ2);
        }else
        {
            SelectImage(IMG_REQ2);

        }

    }
    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }else
            {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent,img_req);

        }else if (img_req ==IMG_REQ2)
        {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,img_req);
            }catch (SecurityException e)
            {
                Toast.makeText(this,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e)
            {
                Toast.makeText(this,R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            url = getUriFromBitmap(bitmap);
            urlList.add(url.toString());
            imageAdapter.notifyDataSetChanged();
            binding.setAddClientModel(addClientModel);



        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            url = data.getData();
            urlList.add(url.toString());
            imageAdapter.notifyDataSetChanged();
            binding.setAddClientModel(addClientModel);




        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == IMG_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ1);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }

    public void deleteImage(int adapterPosition) {
        urlList.remove(adapterPosition);
        imageAdapter.notifyItemRemoved(adapterPosition);
        binding.setAddClientModel(addClientModel);
    }

    @Override
    public void back() {
        finish();
    }


    @Override
    public void addClient(AddClientModel addClientModel) {
        this.addClientModel = addClientModel;

        if (addClientModel.isDataValid(this))
        {
                if (urlList.size()>0)
                {
                    //addClientWithImage();
                }else
                    {
                        addClientWithoutImage();
                    }
        }
    }
}
