package com.ekoapp.sample.core.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

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

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.remove(EKO_CLIENT_PREF_REGISTER_DEVICE)
                it.remove(EKO_CLIENT_PREF_DISPLAY_NAME)
//                it.clear()
            }
        }
}