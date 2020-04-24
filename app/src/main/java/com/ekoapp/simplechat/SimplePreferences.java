package com.ekoapp.simplechat;

import android.content.Context;
import android.content.SharedPreferences;

import com.ekoapp.ekosdk.EkoChannelFilter;
import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.Collections;
import java.util.Set;

public class SimplePreferences {

    private static final String API_KEY_KEY = "API_KEY_KEY";
    private static final String CHANNEL_TYPE_OPTION_KEY = "CHANNEL_TYPE_OPTION_KEY";
    private static final String CHANNEL_MEMBERSHIP_OPTION_KEY = "CHANNEL_MEMBERSHIP_OPTION_KEY";
    private static final String INCLUDING_CHANNEL_TAGS_KEY = "INCLUDING_CHANNEL_TAGS_KEY";
    private static final String EXCLUDING_CHANNEL_TAGS_KEY = "EXCLUDING_CHANNEL_TAGS_KEY";
    private static final String STACK_FROM_END_KEY = "STACK_FROM_END_KEY";
    private static final String REVERT_LAYOUT_KEY = "REVERT_LAYOUT_KEY";

    private static class SimplePreferencesHolder {
        private static final RxSharedPreferences INSTANCE = init();
    }

    private static RxSharedPreferences init() {
        SharedPreferences preferences = SimpleChatApp.Companion.get()
                .getSharedPreferences("simple_preferences", Context.MODE_PRIVATE);
        return RxSharedPreferences.create(preferences);
    }

    public static RxSharedPreferences get() {
        return SimplePreferencesHolder.INSTANCE;
    }

    public static Preference<String> getApiKey() {
        return get().getString(API_KEY_KEY, SimpleConfig.DEFAULT_API_KEY);
    }

    public static Preference<Set<String>> getChannelTypeOptions() {
        return get().getStringSet(CHANNEL_TYPE_OPTION_KEY, Collections.EMPTY_SET);
    }

    public static Preference<String> getChannelMembershipOption() {
        return get().getString(CHANNEL_MEMBERSHIP_OPTION_KEY, EkoChannelFilter.ALL.getApiKey());
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
