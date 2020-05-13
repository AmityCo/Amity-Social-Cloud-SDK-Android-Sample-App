package com.ekoapp.sample.socialfeature.users.renders

import android.content.Context
import android.view.View
import android.widget.TextView
import com.ekoapp.ekosdk.EkoUser

data class EkoUsersRenderData(val context: Context, val item: EkoUser)

fun EkoUsersRenderData.usersRender(
        view: View,
        body: TextView,
        eventClick: (EkoUsersRenderData) -> Unit) {

    body.text = item.userId
    view.setOnClickListener { eventClick.invoke(this) }
}