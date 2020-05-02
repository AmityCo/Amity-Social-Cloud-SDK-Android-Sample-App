package com.ekoapp.sample.socialfeature.userfeed.view.renders

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.ekoapp.sample.socialfeature.userfeed.EXTRA_NAME_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.REQUEST_CODE_EDIT_FEEDS
import com.ekoapp.sample.socialfeature.userfeed.model.SampleFeedsResponse
import com.ekoapp.sample.socialfeature.userfeed.view.components.BodyFeedsComponent
import com.ekoapp.sample.socialfeature.userfeed.view.components.HeaderFeedsComponent
import com.ekoapp.sample.socialfeature.userfeed.view.editfeeds.EditFeedsActivity

data class UserFeedsRenderData(val context: Context, val feeds: SampleFeedsResponse)

fun UserFeedsRenderData.userFeedRender(
        header: HeaderFeedsComponent,
        body: BodyFeedsComponent,
        eventDelete: (Boolean) -> Unit) {

    header.setupView(feeds)
    header.editFeeds { (context as AppCompatActivity).navEditFeedsPage(feeds) }
    header.deleteFeeds(eventDelete::invoke)

    body.setupView(feeds)
}

fun FragmentActivity.navEditFeedsPage(feeds: SampleFeedsResponse) {
    val intent = Intent(this, EditFeedsActivity::class.java)
    intent.putExtra(EXTRA_NAME_FEEDS, feeds)
    startActivityForResult(intent, REQUEST_CODE_EDIT_FEEDS)
}