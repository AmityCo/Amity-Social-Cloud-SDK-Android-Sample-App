package com.ekoapp.simplechat.messagereactionlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekoapp.ekosdk.reaction.EkoReaction
import com.ekoapp.simplechat.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_message_reaction_list.*

abstract class ReactionListActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_reaction_list)

        val adapter = MessageReactionListAdapter()
        val layoutManager = LinearLayoutManager(this)

        reaction_recyclerview.layoutManager = layoutManager
        reaction_recyclerview.adapter = adapter

        disposable.add(
                getReactionCollection()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ adapter.submitList(it) }
                                , {})
        )

    }

    abstract fun getReactionCollection(): Flowable<PagedList<EkoReaction>>

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}