package com.ekoapp.sample.chatfeature.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.data.ReactionData
import com.ekoapp.sample.chatfeature.messages.view.list.ReactionsAdapter
import com.ekoapp.sample.core.base.list.RecyclerBuilder
import kotlinx.android.synthetic.main.component_reaction_and_reply.view.*

class ReactionAndReplyComponent : ConstraintLayout {

    init {
        LayoutInflater.from(context).inflate(R.layout.component_reaction_and_reply, this, true)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    fun setupView(items: ArrayList<ReactionData>, selectedReaction: (String) -> Unit, actionReply: () -> Unit) {
        image_reply.setOnClickListener { actionReply.invoke() }
        items.renderSelectReactions(selectedReaction)
    }

    private fun ArrayList<ReactionData>.renderSelectReactions(actionSelectedReaction: (String) -> Unit) {
        val adapter = ReactionsAdapter(context, this, actionSelectedReaction::invoke)
        RecyclerBuilder(context, recycler_reactions, size)
                .builder()
                .build(adapter)
    }
}