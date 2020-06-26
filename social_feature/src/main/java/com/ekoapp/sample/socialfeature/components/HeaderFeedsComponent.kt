package com.ekoapp.sample.socialfeature.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ekoapp.ekosdk.EkoPost
import com.ekoapp.sample.core.seals.ReportSealType
import com.ekoapp.sample.core.utils.getTimeAgo
import com.ekoapp.sample.core.utils.setTint
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.dialogs.FeedsMoreHorizBottomSheetFragment
import com.ekoapp.sample.socialfeature.enums.ReactionTypes
import kotlinx.android.synthetic.main.component_header_feeds.view.*


class HeaderFeedsComponent : ConstraintLayout {

    private var isFavorited = false

    init {
        LayoutInflater.from(context).inflate(R.layout.component_header_feeds, this, true)
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

    fun EkoPost.setMoreHorizView(edit: (Boolean) -> Unit,
                                 delete: (Boolean) -> Unit,
                                 report: (ReportSealType) -> Unit) {
        image_more_horiz.setOnClickListener {
            renderBottomSheet(edit, delete, report)
        }
    }

    private fun EkoPost.renderBottomSheet(edit: (Boolean) -> Unit,
                                          delete: (Boolean) -> Unit,
                                          report: (ReportSealType) -> Unit) {
        val feedsMoreHorizBottomSheet = FeedsMoreHorizBottomSheetFragment(context, this)
        feedsMoreHorizBottomSheet.show((context as AppCompatActivity).supportFragmentManager, feedsMoreHorizBottomSheet.tag)

        feedsMoreHorizBottomSheet.editFeeds(edit)
        feedsMoreHorizBottomSheet.deleteFeeds(delete)
        feedsMoreHorizBottomSheet.reportFeeds(report)
    }

    private fun FeedsMoreHorizBottomSheetFragment.editFeeds(edit: (Boolean) -> Unit) {
        renderEdit {
            edit.invoke(it)
            dialog?.cancel()
        }
    }

    private fun FeedsMoreHorizBottomSheetFragment.deleteFeeds(delete: (Boolean) -> Unit) {
        renderDelete {
            delete.invoke(it)
            dialog?.cancel()
        }
    }

    private fun FeedsMoreHorizBottomSheetFragment.reportFeeds(report: (ReportSealType) -> Unit) {
        renderReport {
            report.invoke(it)
            dialog?.cancel()
        }
    }

    fun onClickFullName(action: () -> Unit) {
        text_full_name.setOnClickListener {
            action.invoke()
        }
    }
}