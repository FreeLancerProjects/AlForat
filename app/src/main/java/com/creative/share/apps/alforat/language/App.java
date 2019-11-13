package com.creative.share.apps.alforat.language;

import android.app.Application;
import android.content.Context;

import com.creative.share.apps.alforat.preferences.Preferences;


public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageHelper.updateResources(base, Preferences.newInstance().getSelectedLanguage(base)));
    }


}
