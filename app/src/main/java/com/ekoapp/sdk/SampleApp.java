package com.ekoapp.sdk;

import androidx.multidex.MultiDexApplication;

import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.sdk.common.preferences.SamplePreferences;

public class SampleApp extends MultiDexApplication {

    private static SampleApp APP;


    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
        EkoClient.setup(SamplePreferences.getApiKey().get());
    }

    public static SampleApp get() {
        return APP;
    }
}
