package com.ekoapp.sample.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.sample.core.constant.EKO_API_KEY

object PreferenceHelper {
    private const val PREF_EKO_API_KEY = "EKO_API_KEY"
    private const val PREF_CHANNEL_TYPE_OPTION_KEY = "CHANNEL_TYPE_OPTION_KEY"
    private const val PREF_CHANNEL_MEMBERSHIP_OPTION_KEY = "CHANNEL_MEMBERSHIP_OPTION_KEY"
    private const val PREF_INCLUDING_CHANNEL_TAGS_KEY = "INCLUDING_CHANNEL_TAGS_KEY"
    private const val PREF_EXCLUDING_CHANNEL_TAGS_KEY = "EXCLUDING_CHANNEL_TAGS_KEY"
    private const val PREF_STACK_FROM_END_KEY = "STACK_FROM_END_KEY"
    private const val PREF_REVERT_LAYOUT_KEY = "REVERT_LAYOUT_KEY"

    fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    inline fun SharedPreferences.Editor.put(pair: Pair<String, Any>) {
        val key = pair.first
        val value = pair.second
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitive types can be stored in SharedPreferences")
        }
    }

    var SharedPreferences.ekoApiKey
        get() = getString(PREF_EKO_API_KEY, EKO_API_KEY)
        set(value) {
            editMe {
                it.putString(PREF_EKO_API_KEY, value)
            }
        }

    var SharedPreferences.channelTypeOptions: MutableSet<String>?
        get() = getStringSet(PREF_CHANNEL_TYPE_OPTION_KEY, emptySet())
        set(value) {
            editMe {
                it.putStringSet(PREF_CHANNEL_TYPE_OPTION_KEY, value)
            }
        }

    var SharedPreferences.channelMembershipOption
        get() = getString(PREF_CHANNEL_MEMBERSHIP_OPTION_KEY, EkoChannelFilter.ALL.apiKey)
        set(value) {
            editMe {
                it.putString(PREF_CHANNEL_MEMBERSHIP_OPTION_KEY, value)
            }
        }

    var SharedPreferences.includingChannelTags: MutableSet<String>?
        get() = getStringSet(PREF_INCLUDING_CHANNEL_TAGS_KEY, emptySet())
        set(value) {
            editMe {
                it.putStringSet(PREF_INCLUDING_CHANNEL_TAGS_KEY, value)
            }
        }

    var SharedPreferences.excludingChannelTags: MutableSet<String>?
        get() = getStringSet(PREF_EXCLUDING_CHANNEL_TAGS_KEY, emptySet())
        set(value) {
            editMe {
                it.putStringSet(PREF_EXCLUDING_CHANNEL_TAGS_KEY, value)
            }
        }

    var SharedPreferences.stackFromEnd
        get() = getBoolean(String.format("%s_%s", PREF_STACK_FROM_END_KEY, javaClass.name), false)
        set(value) {
            editMe {
                it.putBoolean(String.format("%s_%s", PREF_STACK_FROM_END_KEY, javaClass.name), value)
            }
        }

    var SharedPreferences.revertLayout
        get() = getBoolean(String.format("%s_%s", PREF_REVERT_LAYOUT_KEY, javaClass.name), false)
        set(value) {
            editMe {
                it.putBoolean(String.format("%s_%s", PREF_REVERT_LAYOUT_KEY, javaClass.name), value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                //it.remove(prefName)
                it.clear()
            }
        }
}