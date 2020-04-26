package com.ekoapp.sample.chatfeature.messagereactionlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoMessageRepository
import com.ekoapp.ekosdk.internal.data.model.EkoMessageReaction
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.intent.OpenMessageReactionListIntent
import kotlinx.android.synthetic.main.activity_message_reaction_list.*

class MessageReactionListActivity : AppCompatActivity() {
    lateinit var messageRepository: EkoMessageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_reaction_list)

        val adapter = MessageReactionListAdapter()
        val layoutManager = LinearLayoutManager(this)

        reaction_recyclerview.layoutManager = layoutManager
        reaction_recyclerview.adapter = adapter

        val messageId = OpenMessageReactionListIntent.getMessageId(intent)

        messageRepository = EkoClient.newMessageRepository();
        messageRepository.getMessageReactionCollection(messageId)
                .observe(this, Observer<PagedList<EkoMessageReaction>> { adapter.submitList(it) })

    }
}