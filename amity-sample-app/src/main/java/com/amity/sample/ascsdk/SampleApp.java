package com.amity.sample.ascsdk;

import androidx.multidex.MultiDexApplication;

import com.amity.sample.ascsdk.common.preferences.SamplePreferences;
import com.amity.socialcloud.sdk.AmityCoreClient;
import com.amity.socialcloud.sdk.video.AmityStreamBroadcasterClient;
import com.amity.socialcloud.sdk.video.AmityStreamPlayerClient;

public class SampleApp extends MultiDexApplication {

    private static SampleApp APP;


    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
        AmityCoreClient.INSTANCE.setup(SamplePreferences.getApiKey().get(),
                SamplePreferences.getHttpUrl().get(),
                SamplePreferences.getSocketUrl().get());
        AmityStreamBroadcasterClient.setup(AmityCoreClient.INSTANCE.getConfiguration());
        AmityStreamPlayerClient.setup(AmityCoreClient.INSTANCE.getConfiguration());
    }

    public static SampleApp get() {
        return APP;
    }
}
