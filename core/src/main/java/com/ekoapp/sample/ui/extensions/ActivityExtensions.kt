package com.ekoapp.sample.ui.extensions

import android.app.Activity
import com.ekoapp.sample.core.di.CoreComponentProvider

fun Activity.coreComponent() =
        (applicationContext as? CoreComponentProvider)?.provideCoreComponent()
                ?: throw IllegalStateException("CoreComponentProvider not implemented: $applicationContext")

