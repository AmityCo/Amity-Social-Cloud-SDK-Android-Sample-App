package com.ekoapp.sample.socialfeature.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.utils.getTimeAgo
import com.ekoapp.sample.core.utils.setTint
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.dialogs.FeedsMoreHorizBottomSheetFragment
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import kotlinx.android.synthetic.main.component_header_feeds.view.*


class HeaderFeedsComponent : ConstraintLayout {

    private var isFavorited = false
    private var feedsMoreHorizBottomSheet: FeedsMoreHorizBottomSheetFragment

    init {
        LayoutInflater.from(context).inflate(R.layout.component_header_feeds, this, true)
        feedsMoreHorizBottomSheet = FeedsMoreHorizBottomSheetFragment()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(item: EkoPost) {
        text_full_name.text = item.postedUserId
        text_time.text = item.createdAt.toDate().getTimeAgo()

        //TODO Use get my reaction from value view model instead
        val match = item.myReactions.filter { ReactionTypes.FAVORITE.text.contains(it, ignoreCase = true) }
        isFavorited = match.contains(ReactionTypes.FAVORITE.text)
        selectorFavorite(isFavorited)

        setMoreHorizView(item)
    }

    private fun selectorFavorite(isFavorite: Boolean) = if (isFavorite) favoritedView() else favoriteView()

    fun favoriteFeeds(actionFavorite: (Boolean) -> Unit) {
        image_favorite.setOnClickListener {
            actionFavorite.invoke(!isFavorited)
        }
    }

    @SuppressLint("PrivateResource")
    private fun favoriteView() {
        image_favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite))
        image_favorite.setTint(ContextCompat.getColor(context, R.color.colorFavorite))
    }

    private fun favoritedView() {
        image_favorite.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorited))
        image_favorite.setTint(ContextCompat.getColor(context, R.color.colorFavorited))
    }

    private fun setMoreHorizView(item: EkoPost) {
        if (item.postedUserId != EkoClient.getUserId()) {
            image_more_horiz.visibility = View.GONE
        } else {
            image_more_horiz.visibility = View.VISIBLE
        }
        image_more_horiz.setOnClickListener { context.renderBottomSheet() }
    }

    private fun Context.renderBottomSheet() {
        feedsMoreHorizBottomSheet.show((this as AppCompatActivity).supportFragmentManager, feedsMoreHorizBottomSheet.tag)
    }

    fun editFeeds(edit: (Boolean) -> Unit) {
        feedsMoreHorizBottomSheet.renderEdit {
            edit.invoke(it)
            feedsMoreHorizBottomSheet.dialog?.cancel()
        }
    }

    fun deleteFeeds(delete: (Boolean) -> Unit) {
        feedsMoreHorizBottomSheet.renderDelete {
            delete.invoke(it)
            feedsMoreHorizBottomSheet.dialog?.cancel()
        }
    }

    fun onClickFullName(action: () -> Unit) {
        text_full_name.setOnClickListener {
            action.invoke()
        }
    }
}