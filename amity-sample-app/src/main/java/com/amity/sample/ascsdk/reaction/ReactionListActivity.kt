package com.amity.sample.ascsdk.reaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.amity.socialcloud.sdk.core.reaction.AmityReaction
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.messagelist.MessageReactionListAdapter
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_message_reaction_list.*

abstract class ReactionListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_reaction_list)

        val adapter = MessageReactionListAdapter()
        val layoutManager = LinearLayoutManager(this)

        reaction_recyclerview.layoutManager = layoutManager
        reaction_recyclerview.adapter = adapter

        LiveDataReactiveStreams.fromPublisher(getReactionCollection())
                .observe(this, Observer<PagedList<AmityReaction>> { adapter.submitList(it) })

    }

    abstract fun getReactionCollection(): Flowable<PagedList<AmityReaction>>

}