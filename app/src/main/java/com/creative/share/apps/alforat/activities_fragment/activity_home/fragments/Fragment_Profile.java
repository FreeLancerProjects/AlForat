package com.creative.share.apps.alforat.activities_fragment.activity_home.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_home.activity.HomeActivity;
import com.creative.share.apps.alforat.databinding.DialogLanguageBinding;
import com.creative.share.apps.alforat.databinding.FragmentProfileBinding;
import com.creative.share.apps.alforat.interfaces.Listeners;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.share.Common;
import com.creative.share.apps.alforat.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment implements Listeners.LogoutListener {
    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;


    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        binding.setUserModel(userModel);
        binding.setLogoutListener(this);
        binding.consLanguage.setOnClickListener(view -> CreateLangDialog());




    }


    private void CreateLangDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_language, null, false);
        String lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        if (lang.equals("ar")) {
            binding.rbAr.setChecked(true);
        } else {
            binding.rbEn.setChecked(true);

        }
        binding.btnCancel.setOnClickListener((v) ->
                dialog.dismiss()

        );
        binding.rbAr.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> activity.refreshActivity("ar"), 1000);
        });
        binding.rbEn.setOnClickListener(view -> {
            dialog.dismiss();
            new Handler()
                    .postDelayed(() -> activity.refreshActivity("en"), 1000);
        });
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }




    @Override
    public void logout() {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {
            Api.getService(Tags.base_url)
                    .logout(userModel.getToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                preferences.clear(activity);
                                activity.navigateToSinInActivity();

                            } else {

                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
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
