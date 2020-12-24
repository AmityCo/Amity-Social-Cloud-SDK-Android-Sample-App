package com.ekoapp.sdk.post.reaction

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoFeedRepository
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.intent.OpenPostReactionActivityIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_message_reaction_list.*

class PostReactionActivity : AppCompatActivity() {

    lateinit var feedRepository: EkoFeedRepository
    val adapter = PostReactionAdapter()
    var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_reaction_list)

        val layoutManager = LinearLayoutManager(this)

        reaction_recyclerview.layoutManager = layoutManager
        reaction_recyclerview.adapter = adapter

        val post = OpenPostReactionActivityIntent.getPost(intent)
        postId = post.getPostId()

        feedRepository = EkoClient.newFeedRepository()
        feedRepository.getReactionCollection(post.getPostId())
                .build()
                .query()
                .doOnNext(adapter::submitList)
                .subscribe()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_reaction_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_search_reaction_name) {
            showDialog(R.string.delete_post, "reaction name", "", false) { dialog, input ->
                if (input.toString().isNotBlank()) {
                    feedRepository.getReactionCollection(postId)
                            .reactionName(input.toString())
                            .build()
                            .query()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext {
                                reaction_recyclerview.adapter = adapter
                                adapter.submitList(it)
                            }
                            .subscribe()
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}