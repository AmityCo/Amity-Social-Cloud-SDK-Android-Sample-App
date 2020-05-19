package com.ekoapp.sample.socialfeature.userfeed.view.list


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.core.base.list.BaseViewHolder
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeed.view.list.viewholder.CreateFeedsViewHolder
import com.ekoapp.sample.socialfeature.userfeed.view.list.viewholder.FriendsViewHolder
import com.ekoapp.sample.socialfeature.userfeed.view.list.viewholder.UserFeedsViewData
import com.ekoapp.sample.socialfeature.userfeed.view.list.viewholder.UserFeedsViewHolder

class EkoUserFeedsMultiViewAdapter(private val context: Context,
                                   private val lifecycleOwner: LifecycleOwner,
                                   private val viewModel: UserFeedsViewModel) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private val sections = ArrayList<Any>()

    companion object {
        private const val TYPE_FRIENDS = 0
        private const val TYPE_CREATE_FEEDS = 1
        private const val TYPE_USER_FEEDS = 2
    }

    init {
        sections.add(TYPE_FRIENDS)
        sections.add(TYPE_CREATE_FEEDS)
        sections.add(TYPE_USER_FEEDS)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            TYPE_FRIENDS -> {
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.item_friends_feeds, parent, false)
                FriendsViewHolder(view)
            }
            TYPE_CREATE_FEEDS -> {
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.item_touchable_create_feeds, parent, false)
                CreateFeedsViewHolder(view)
            }
            TYPE_USER_FEEDS -> {
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.item_feeds, parent, false)
                UserFeedsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is FriendsViewHolder -> {
                viewModel.getUserList().observeNotNull(lifecycleOwner, {
                    holder.bind(it)
                })
            }
            is CreateFeedsViewHolder -> {
                holder.bind { viewModel.createFeedsActionRelay.postValue(Unit) }
            }
            is UserFeedsViewHolder -> {
                viewModel.executeUserFeeds(viewModel.getMyProfile()).observeNotNull(lifecycleOwner, {
                    holder.bind(UserFeedsViewData(lifecycleOwner = lifecycleOwner, userFeedsViewSeal = it, viewModel = viewModel))
                })
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
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

