package com.ekoapp.simplechat.messagereactionlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekoapp.ekosdk.reaction.EkoReaction
import com.ekoapp.simplechat.R
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
                .observe(this, Observer<PagedList<EkoReaction>> { adapter.submitList(it) })

    }

    abstract fun getReactionCollection(): Flowable<PagedList<EkoReaction>>

}