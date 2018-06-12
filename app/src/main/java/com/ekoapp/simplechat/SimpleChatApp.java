package com.ekoapp.simplechat;

import android.support.multidex.MultiDexApplication;

import com.ekoapp.ekosdk.EkoClient;

public class SimpleChatApp extends MultiDexApplication {

    private static SimpleChatApp APP;


    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
        EkoClient.setup(SimplePreferences.getApiKey().get());
    }


    public static SimpleChatApp get() {
        return APP;
    }
}
