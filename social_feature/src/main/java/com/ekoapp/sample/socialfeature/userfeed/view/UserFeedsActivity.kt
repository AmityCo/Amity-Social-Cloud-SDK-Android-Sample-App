package com.ekoapp.sample.socialfeature.userfeed.view

import android.content.Intent
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import com.ekoapp.sample.core.base.viewmodel.SingleViewModelActivity
import com.ekoapp.sample.core.ui.extensions.coreComponent
import com.ekoapp.sample.core.ui.extensions.observeNotNull
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.constants.*
import com.ekoapp.sample.socialfeature.createfeeds.CreateFeedsActivity
import com.ekoapp.sample.socialfeature.di.DaggerSocialActivityComponent
import com.ekoapp.sample.socialfeature.editfeeds.EditFeedsActivity
import com.ekoapp.sample.socialfeature.userfeed.view.list.EkoUserFeedsAdapter
import com.ekoapp.sample.socialfeature.users.data.UserData
import kotlinx.android.synthetic.main.component_touchable_create_feeds.view.*
import kotlinx.android.synthetic.main.fragment_user_feeds.*

class UserFeedsActivity : SingleViewModelActivity<UserFeedsViewModel>() {

    private val spaceFeeds = 1
    private lateinit var adapter: EkoUserFeedsAdapter

    override fun getLayout(): Int {
        return R.layout.fragment_user_feeds
    }

    override fun bindViewModel(viewModel: UserFeedsViewModel) {
        val item = intent.extras?.getParcelable<UserData>(EXTRA_USER_DATA)
        viewModel.setupIntent(item)
        friend_list.renderList(this, viewModel)
        renderList(viewModel)
        setupEvent(viewModel)
    }

    private fun setupEvent(viewModel: UserFeedsViewModel) {
        touchable_post_feeds.button_touchable_target_post.setOnClickListener {
            viewModel.getIntentUserData {
                val intent = Intent(this, CreateFeedsActivity::class.java)
                intent.putExtra(EXTRA_DISPLAY_NAME, it.userId)
                startActivityForResult(intent, REQUEST_CODE_CREATE_FEEDS)
            }
        }

        viewModel.observeEditFeedsPage().observeNotNull(this, {
            val intent = Intent(this, EditFeedsActivity::class.java)
            intent.putExtra(EXTRA_EDIT_FEEDS, it)
            startActivityForResult(intent, REQUEST_CODE_EDIT_FEEDS)
        })
    }

    private fun renderList(viewModel: UserFeedsViewModel) {
        viewModel.getIntentUserData { data ->
            viewModel.getUserFeeds(data).observeNotNull(this, {
                adapter = EkoUserFeedsAdapter(userFeedsViewModel = viewModel)
                RecyclerBuilder(this, recyclerView = recycler_feeds, spaceCount = spaceFeeds)
                        .builder()
                        .build(adapter)
                adapter.submitList(it)
            })
        }
    }

    override fun initDependencyInjection() {
        DaggerSocialActivityComponent
                .builder()
                .coreComponent(coreComponent())
                .build()
                .inject(this)
    }

    override fun getViewModelClass(): Class<UserFeedsViewModel> {
        return UserFeedsViewModel::class.java
    }
}