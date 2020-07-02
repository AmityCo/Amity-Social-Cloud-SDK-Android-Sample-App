package com.ekoapp.sample.core.utils

import androidx.fragment.app.FragmentActivity
import com.ekoapp.sample.core.R
import com.google.android.material.snackbar.Snackbar

class SnackBarUtil(private val fragmentActivity: FragmentActivity) {

    fun info(text: CharSequence) {
        Snackbar.make(fragmentActivity.findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(R.string.general_ok) {}
                .show()
    }
}