package com.creative.share.apps.alforat.activities_fragment.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.activities_fragment.activity_home.activity.HomeActivity;
import com.creative.share.apps.alforat.activities_fragment.activity_sign_in.SignInActivity;
import com.creative.share.apps.alforat.databinding.ActivitySplashBinding;
import com.creative.share.apps.alforat.language.LanguageHelper;
import com.creative.share.apps.alforat.preferences.Preferences;
import com.creative.share.apps.alforat.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    private Preferences preferences;
    private ActivitySplashBinding binding;
    private Animation animation1,animation2;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        preferences = Preferences.newInstance();

        animation1= AnimationUtils.loadAnimation(getBaseContext(),R.anim.scale);
        animation2= AnimationUtils.loadAnimation(getBaseContext(),R.anim.translate);

        binding.image.startAnimation(animation1);

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.tv.setVisibility(View.VISIBLE);
                binding.tv.startAnimation(animation2);




            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String session = preferences.getSession(SplashActivity.this);
                if (session.equals(Tags.session_login))
                {
                    Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Intent intent=new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }
}
