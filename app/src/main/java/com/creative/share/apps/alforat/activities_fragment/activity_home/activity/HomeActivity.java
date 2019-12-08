package com.creative.share.apps.alforat.activities_fragment.activity_home.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_home.fragments.Fragment_Bills;
import com.creative.share.apps.alforat.activities_fragment.activity_home.fragments.Fragment_Main;
import com.creative.share.apps.alforat.activities_fragment.activity_home.fragments.Fragment_Profile;
import com.creative.share.apps.alforat.activities_fragment.activity_permissions.PermissionsActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_sign_in.SignInActivity;
import com.creative.share.apps.alforat.databinding.ActivityHomeBinding;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.models.UserModel;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.remote.Api;
import com.creative.share.apps.alforat.share.Common;
import com.creative.share.apps.alforat.tags.Tags;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private FragmentManager fragmentManager;
    private Fragment_Main fragment_main;
    private Fragment_Bills fragment_bills;
    private Fragment_Profile fragment_profile;


    private Preferences preferences;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        initView();
        if (savedInstanceState==null)
        {
            DisplayFragmentMain();
        }

        getDataFromIntent();



    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null&&intent.hasExtra("notification"))
        {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager!=null)
            {
                manager.cancel("forat",0);

            }

            Intent intent1 = new Intent(this, PermissionsActivity.class);
            startActivity(intent1);
        }
    }


    private void initView()
    {
        Paper.init(this);
        fragmentManager = this.getSupportFragmentManager();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);


        setUpBottomNavigation();
        binding.ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:

                    DisplayFragmentMain();
                    break;
                case 1:
                    if (userModel!=null)
                    {
                        DisplayFragmentBills();

                    }else
                    {
                        Common.CreateNoSignAlertDialog(this);
                    }





                    break;
                case 2:
                    if (userModel!=null)
                    {
                        DisplayFragmentProfile();

                    }else
                    {
                        Common.CreateNoSignAlertDialog(this);
                    }

                    break;

            }
            return false;
        });

        if (userModel!=null)
        {
            updateToken();
        }

    }

    private void updateToken() {

        FirebaseInstanceId.getInstance()
                .getInstanceId().addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        if (task.getResult()!=null)
                        {
                            String token = task.getResult().getToken();

                            try {

                                Api.getService(Tags.base_url)
                                        .updateToken(userModel.getToken(),token)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful() && response.body() != null )
                                                {
                                                    Log.e("token","updated successfully");
                                                } else {
                                                    try {

                                                        Log.e("error", response.code() + "_" + response.errorBody().string());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                try {

                                                    if (t.getMessage() != null) {
                                                        Log.e("error", t.getMessage());
                                                        if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                                            Toast.makeText(HomeActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                } catch (Exception e) {
                                                }
                                            }
                                        });
                            } catch (Exception e) {


                            }
                        }


                    }
                });
    }

    private void setUpBottomNavigation()
    {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.ic_bills);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.ic_nav_user);

        binding.ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
        binding.ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.ahBottomNav.setTitleTextSizeInSp(14, 12);
        binding.ahBottomNav.setForceTint(true);
        binding.ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.gray5));

        binding.ahBottomNav.addItem(item1);
        binding.ahBottomNav.addItem(item2);
        binding.ahBottomNav.addItem(item3);

        binding.ahBottomNav.setCurrentItem(1);




    }



    public void DisplayFragmentMain()
    {
        try {
            if (fragment_main == null) {
                fragment_main = Fragment_Main.newInstance();
            }
            if (fragment_bills != null && fragment_bills.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_bills).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }

            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }

            binding.setTitle(getString(R.string.home));
            binding.ahBottomNav.setCurrentItem(0,false);
        }catch (Exception e){}



    }

    public void DisplayFragmentBills()
    {
        try {
            if (fragment_bills == null) {
                fragment_bills = Fragment_Bills.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }

            if (fragment_bills.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_bills).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_bills, "fragment_bills").addToBackStack("fragment_bills").commit();

            }
            binding.setTitle(getString(R.string.bills));
            binding.ahBottomNav.setCurrentItem(1,false);
        }catch (Exception e){}



    }

    public void DisplayFragmentProfile()
    {
        try {
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_bills != null && fragment_bills.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_bills).commit();
            }

            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }
            binding.setTitle(getString(R.string.profile));
            binding.ahBottomNav.setCurrentItem(2,false);
        }catch (Exception e){}



    }






    public void refreshActivity(String lang) {
        new Handler()
                .postDelayed(() -> {
                    preferences.selectedLanguage(HomeActivity.this, lang);
                    Paper.book().write("lang", lang);
                    LanguageHelper.setNewLocale(HomeActivity.this, lang);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }
    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {

        if (userModel==null)
        {
            navigateToSinInActivity();
        }else
            {
                if (fragment_main!=null&&fragment_main.isAdded()&&fragment_main.isVisible())
                {
                    finish();

                }else
                    {
                        DisplayFragmentMain();
                    }
            }
    }

    public void navigateToSinInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment :fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
