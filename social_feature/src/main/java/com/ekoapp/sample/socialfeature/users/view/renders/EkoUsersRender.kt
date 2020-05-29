package com.ekoapp.sample.socialfeature.users.view.renders

import android.view.View
import android.widget.TextView
import com.ekoapp.ekosdk.EkoUser

data class EkoUsersRenderData(val item: EkoUser)

fun EkoUsersRenderData.usersRender(
        view: View,
        body: TextView,
        eventClick: (EkoUsersRenderData) -> Unit) {

    body.text = item.userId
    view.setOnClickListener { eventClick.invoke(this) }
}