package com.ekoapp.simplechat;

import android.content.Context;
import android.util.Log;

import com.ekoapp.push.EkoBaiduMessageReceiver;

public class SampleBaiduMessageReceiver extends EkoBaiduMessageReceiver {

    @Override
    public void onMessage(Context context, String message, String customContentString) {
        Log.e("baidu_message", "message -> " + message);
        Log.e("baidu_message", "customContentString -> " + customContentString);
    }
}
