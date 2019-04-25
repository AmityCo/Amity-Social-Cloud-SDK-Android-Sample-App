package com.ekoapp.simplechat;

import android.content.Context;
import android.content.SharedPreferences;

import com.f2prateek.rx.preferences2.Preference;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.Collections;
import java.util.Set;

public class SimplePreferences {

    private static final String API_KEY_KEY = "API_KEY_KEY";
    private static final String INCLUDING_TAGS_KEY = "INCLUDING_TAGS_KEY";
    private static final String EXCLUDING_TAGS_KEY = "EXCLUDING_TAGS_KEY";


    private static class SimplePreferencesHolder {
        private static final RxSharedPreferences INSTANCE = init();
    }

    private static RxSharedPreferences init() {
        SharedPreferences preferences = SimpleChatApp.get()
                .getSharedPreferences("simple_preferences", Context.MODE_PRIVATE);
        return RxSharedPreferences.create(preferences);
    }

    public static RxSharedPreferences get() {
        return SimplePreferencesHolder.INSTANCE;
    }

    public static Preference<String> getApiKey() {
        return get().getString(API_KEY_KEY, SimpleConfig.DEFAULT_API_KEY);
    }

    public static Preference<Set<String>> getIncludingTags() {
        return get().getStringSet(INCLUDING_TAGS_KEY, Collections.EMPTY_SET);
    }

    public static Preference<Set<String>> getExcludingTags() {
        return get().getStringSet(EXCLUDING_TAGS_KEY, Collections.EMPTY_SET);
    }
}
