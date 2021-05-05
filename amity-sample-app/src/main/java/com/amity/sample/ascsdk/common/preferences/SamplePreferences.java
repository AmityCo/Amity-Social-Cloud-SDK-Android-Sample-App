package com.amity.sample.ascsdk.common.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.amity.socialcloud.sdk.chat.channel.AmityChannelFilter;
import com.amity.sample.ascsdk.SampleApp;
import com.ekoapp.sdk_versioning.SampleAPIKey;
import com.ekoapp.sdk_versioning.SampleUrl;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.Collections;
import java.util.Set;

public class SamplePreferences {

    private static final String API_KEY_KEY = "API_KEY_KEY";
    private static final String HTTP_URL_KEY = "HTTP_URL_KEY";
    private static final String SOCKET_URL_KEY = "SOCKET_URL_KEY";
    private static final String MY_USER_ID_KEY = "MY_USER_ID_KEY";
    private static final String CHANNEL_TYPE_OPTION_KEY = "CHANNEL_TYPE_OPTION_KEY";
    private static final String CHANNEL_MEMBERSHIP_OPTION_KEY = "CHANNEL_MEMBERSHIP_OPTION_KEY";
    private static final String INCLUDING_CHANNEL_TAGS_KEY = "INCLUDING_CHANNEL_TAGS_KEY";
    private static final String EXCLUDING_CHANNEL_TAGS_KEY = "EXCLUDING_CHANNEL_TAGS_KEY";
    private static final String CHANNEL_INCLUDE_DELETED_OPTION_KEY = "CHANNEL_INCLUDE_DELETED_OPTION_KEY";
    private static final String STACK_FROM_END_KEY = "STACK_FROM_END_KEY";
    private static final String REVERT_LAYOUT_KEY = "REVERT_LAYOUT_KEY";


    private static class SamplePreferencesHolder {
        private static final RxSharedPreferences INSTANCE = init();
    }

    private static RxSharedPreferences init() {
        SharedPreferences preferences = SampleApp.get()
                .getSharedPreferences("sample_preferences", Context.MODE_PRIVATE);
        return RxSharedPreferences.create(preferences);
    }

    public static RxSharedPreferences get() {
        return SamplePreferencesHolder.INSTANCE;
    }


    public static Preference<String> getApiKey() {
        return get().getString(API_KEY_KEY, SampleAPIKey.INSTANCE.get(SampleApp.get()));
    }

    public static Preference<String> getHttpUrl() {
        return get().getString(HTTP_URL_KEY, SampleUrl.INSTANCE.getHttpUrl(SampleApp.get()));
    }

    public static Preference<String> getSocketUrl() {
        return get().getString(SOCKET_URL_KEY, SampleUrl.INSTANCE.getSocketUrl(SampleApp.get()));
    }

    public static Preference<String> getMyUserId() {
        return get().getString(MY_USER_ID_KEY, "victimAndroid");
    }

    public static Preference<Set<String>> getChannelTypeOptions() {
        return get().getStringSet(CHANNEL_TYPE_OPTION_KEY, Collections.EMPTY_SET);
    }

    public static Preference<Boolean> getIncludeDeletedOptions() {
        return get().getBoolean(CHANNEL_INCLUDE_DELETED_OPTION_KEY, true);
    }

    public static Preference<String> getChannelMembershipOption() {
        return get().getString(CHANNEL_MEMBERSHIP_OPTION_KEY, AmityChannelFilter.ALL.getApiKey());
    }

    public static Preference<Set<String>> getIncludingChannelTags() {
        return get().getStringSet(INCLUDING_CHANNEL_TAGS_KEY, Collections.EMPTY_SET);
    }

    public static Preference<Set<String>> getExcludingChannelTags() {
        return get().getStringSet(EXCLUDING_CHANNEL_TAGS_KEY, Collections.EMPTY_SET);
    }

    public static Preference<Boolean> getStackFromEnd(String key, boolean defaultValue) {
        return get().getBoolean(String.format("%s_%s", STACK_FROM_END_KEY, key), defaultValue);
    }

    public static Preference<Boolean> getRevertLayout(String key, boolean defaultValue) {
        return get().getBoolean(String.format("%s_%s", REVERT_LAYOUT_KEY, key), defaultValue);
    }

}
