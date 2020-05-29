package com.ekoapp.sample.socialfeature.users.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ekoapp.ekosdk.adapter.EkoUserAdapter
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.users.view.UsersViewModel
import com.ekoapp.sample.socialfeature.users.view.list.viewholder.EkoUserData
import com.ekoapp.sample.socialfeature.users.view.list.viewholder.UsersViewHolder

class EkoUsersAdapter(private val context: Context,
                      private val viewModel: UsersViewModel) : EkoUserAdapter<BaseViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is UsersViewHolder -> {
                getItem(position)?.apply {
                    holder.bind(EkoUserData(this, viewModel))
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}

