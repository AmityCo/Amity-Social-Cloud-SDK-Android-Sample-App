package com.ekoapp.sample.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.sample.MainNavigationActivity
import com.ekoapp.sample.register.RegisterActivity

fun AppCompatActivity.openRegisterPage() {
    startActivity(Intent(this, RegisterActivity::class.java))
}

fun AppCompatActivity.openMainNavigationPage() {
    startActivity(Intent(this, MainNavigationActivity::class.java))
}