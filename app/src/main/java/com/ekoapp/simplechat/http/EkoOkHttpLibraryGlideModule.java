package com.ekoapp.simplechat.http;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.ekoapp.ekosdk.internal.api.http.EkoOkHttp;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import timber.log.Timber;

@GlideModule
public class EkoOkHttpLibraryGlideModule extends AppGlideModule {

    private static final String TAG = com.ekoapp.ekosdk.internal.api.http.EkoOkHttpLibraryGlideModule.class.getName();


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        Timber.tag(TAG).i("glide: registerComponents: %s", TAG);
        try {
            OkHttpClient client = EkoOkHttp.newBuilder().build();
            registry.append(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
        } catch (NoClassDefFoundError e) {
            Timber.tag(TAG).w(e, "Please include com.github.bumptech.glide:okhttp3-integration to gradle dependencies");
        }
    }
}
