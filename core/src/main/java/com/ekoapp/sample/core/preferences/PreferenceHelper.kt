package com.ekoapp.sample.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.sample.core.enums.ReportTypes

object PreferenceHelper {

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

    var SharedPreferences.registerDevice
        get() = getBoolean(EKO_CLIENT_PREF_REGISTER_DEVICE, false)
        set(value) {
            editMe {
                it.putBoolean(EKO_CLIENT_PREF_REGISTER_DEVICE, value)
            }
        }

    var SharedPreferences.displayName
        get() = getString(EKO_CLIENT_PREF_DISPLAY_NAME, "")
        set(value) {
            editMe {
                it.putString(EKO_CLIENT_PREF_DISPLAY_NAME, value)
            }
        }

    var SharedPreferences.joinChannel
        get() = getBoolean(PREF_JOIN_CHANNEL, false)
        set(value) {
            editMe {
                it.putBoolean(PREF_JOIN_CHANNEL, value)
            }
        }

    var SharedPreferences.channelTypes: Set<String>?
        get() = getStringSet(PREF_SETTINGS_CHANNEL_TYPES, emptySet())
        set(value) {
            editMe {
                it.putStringSet(PREF_SETTINGS_CHANNEL_TYPES, value)
            }
        }

    var SharedPreferences.membership
        get() = getString(PREF_SETTINGS_MEMBERSHIP, EkoChannelFilter.ALL.apiKey)
        set(value) {
            editMe {
                it.putString(PREF_SETTINGS_MEMBERSHIP, value)
            }
        }

    var SharedPreferences.includeTags: Set<String>?
        get() = getStringSet(PREF_SETTINGS_INCLUDE_TAGS, emptySet())
        set(value) {
            editMe {
                it.putStringSet(PREF_SETTINGS_INCLUDE_TAGS, value)
            }
        }

    var SharedPreferences.excludeTags: Set<String>?
        get() = getStringSet(PREF_SETTINGS_EXCLUDE_TAGS, emptySet())
        set(value) {
            editMe {
                it.putStringSet(PREF_SETTINGS_EXCLUDE_TAGS, value)
            }
        }

    var SharedPreferences.report
        get() = getString(PREF_REPORT_POST, ReportTypes.UNFLAG.text)
        set(value) {
            editMe {
                it.putString(PREF_REPORT_POST, value)
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
//                it.remove(EKO_CLIENT_PREF_REGISTER_DEVICE)
//                it.remove(EKO_CLIENT_PREF_DISPLAY_NAME)
                it.clear()
            }
        }
}