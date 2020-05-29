package com.ekoapp.sample.socialfeature.userfeeds.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeeds.view.list.viewholder.*
import com.ekoapp.sample.socialfeature.users.data.UserData

class MainUserFeedsAdapter(private val context: Context,
                           private val lifecycleOwner: LifecycleOwner,
                           private val viewModel: UserFeedsViewModel) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private val sections = ArrayList<Any>()
    private val userData: UserData = viewModel.getIntentUserData { }

    companion object {
        private const val TYPE_PROFILE = 0
        private const val TYPE_FRIENDS = 1
        private const val TYPE_CREATE_FEEDS = 2
        private const val TYPE_USER_FEEDS = 3
    }

    init {
        sections.add(TYPE_PROFILE)
        sections.add(TYPE_FRIENDS)
        sections.add(TYPE_CREATE_FEEDS)
        sections.add(TYPE_USER_FEEDS)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_PROFILE -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_profile_feeds, parent, false)
                ProfileViewHolder(view)
            }
            TYPE_FRIENDS -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_friends_feeds, parent, false)
                FriendsViewHolder(view)
            }
            TYPE_CREATE_FEEDS -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_touchable_create_feeds, parent, false)
                CreateFeedsViewHolder(view)
            }
            TYPE_USER_FEEDS -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_user_feeds, parent, false)
                UserFeedsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is ProfileViewHolder -> {
                holder.bind(userData)
            }
            is FriendsViewHolder -> {
                viewModel.bindUserList().observeNotNull(lifecycleOwner, {
                    holder.bind(FriendsViewData(
                            items = it,
                            actionFindUsers = {
                                viewModel.findUsersActionRelay.postValue(Unit)
                            },
                            actionSeeAllUsers = {
                                viewModel.seeAllUsersActionRelay.postValue(Unit)
                            },
                            viewModel = viewModel))
                })
            }
            is CreateFeedsViewHolder -> {
                holder.bind { viewModel.createFeedsActionRelay.postValue(userData) }
            }
            is UserFeedsViewHolder -> {
                viewModel.bindUserFeedsSeal(userData).observeNotNull(lifecycleOwner, {
                    holder.bind(UserFeedsViewData(
                            context = context,
                            lifecycleOwner = lifecycleOwner,
                            userFeedsViewSeal = it,
                            viewModel = viewModel))
                })
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            sections[position] == TYPE_PROFILE -> {
                TYPE_PROFILE
            }
            sections[position] == TYPE_FRIENDS -> {
                TYPE_FRIENDS
            }
            sections[position] == TYPE_CREATE_FEEDS -> {
                TYPE_CREATE_FEEDS
            }
            else -> {
                TYPE_USER_FEEDS
            }
        }
    }

    override fun getItemCount(): Int {
        return sections.size
    }
}

