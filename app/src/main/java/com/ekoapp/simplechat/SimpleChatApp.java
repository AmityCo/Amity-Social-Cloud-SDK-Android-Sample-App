package com.ekoapp.simplechat;

import androidx.multidex.MultiDexApplication;

import com.ekoapp.ekosdk.EkoClient;
import com.ekoapp.push.EkoBaidu;

public class SimpleChatApp extends MultiDexApplication {

    private static SimpleChatApp APP;


    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
        EkoClient.setup(SimplePreferences.getApiKey().get())
                .andThen(EkoBaidu.create(this).setup("BZ2CnTh6qphSUl66c16Xk7AG"))
                .subscribe();
    }


    public static SimpleChatApp get() {
        return APP;
    }
}
