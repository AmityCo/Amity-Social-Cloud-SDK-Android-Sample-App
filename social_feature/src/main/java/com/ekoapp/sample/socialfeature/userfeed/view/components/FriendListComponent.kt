package com.ekoapp.sample.socialfeature.userfeed.view.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.view.UserFeedsViewModel
import com.ekoapp.sample.socialfeature.userfeed.view.list.EkoFriendsFeedsAdapter
import kotlinx.android.synthetic.main.component_friend_list.view.*


class FriendListComponent : ConstraintLayout {
    private val spaceFriends = 3
    private var adapter: EkoFriendsFeedsAdapter

    init {
        LayoutInflater.from(context).inflate(R.layout.component_friend_list, this, true)
        adapter = EkoFriendsFeedsAdapter()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun renderList(lifecycleOwner: LifecycleOwner, viewModel: UserFeedsViewModel) {
        viewModel.getUserList().observeNotNull(lifecycleOwner, {
            text_total_friends.text = String.format(context.getString(R.string.temporarily_total_friends), it.size)
            RecyclerBuilder(context = context, recyclerView = recycler_friend_list, spaceCount = spaceFriends)
                    .builder()
                    .build(adapter)
            adapter.submitList(it)
        })
    }
}