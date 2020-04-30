package com.ekoapp.sample.chatfeature.messagelist

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.checkbox.isCheckPromptChecked
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.*
import com.ekoapp.ekosdk.exception.EkoError
import com.ekoapp.ekosdk.messaging.data.DataType
import com.ekoapp.ekosdk.messaging.data.TextData
import com.ekoapp.sample.chatfeature.R
import com.ekoapp.sample.chatfeature.intent.OpenMessageReactionListIntent
import com.ekoapp.sample.chatfeature.intent.ViewChannelMembershipsIntent
import com.ekoapp.sample.chatfeature.messagelist.option.MessageOption
import com.ekoapp.sample.chatfeature.messagelist.option.ReactionOption
import com.ekoapp.sample.core.file.FileManager
import com.ekoapp.sample.core.preferences.SimplePreferences
import com.ekoapp.sample.core.base.BaseActivity
import com.google.common.base.Joiner
import com.google.common.collect.Sets
import com.google.gson.JsonObject
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_message_list.*

abstract class MessageListActivity : BaseActivity() {

    private var messages: LiveData<PagedList<EkoMessage>>? = null

    private var adapter: MessageListAdapter? = null

    protected val channelRepository = EkoClient.newChannelRepository()
    protected val messageRepository = EkoClient.newMessageRepository()
    protected val userRepository = EkoClient.newUserRepository()

    protected val includingTags: MutableSet<String> = Sets.newConcurrentHashSet()
    protected val excludingTags: MutableSet<String> = Sets.newConcurrentHashSet()

    protected val stackFromEnd = SimplePreferences.getStackFromEnd(javaClass.name, getDefaultStackFromEnd())
    protected val revertLayout = SimplePreferences.getRevertLayout(javaClass.name, getDefaultRevertLayout())

    protected val rxPermissions = RxPermissions(this)

    protected val disposable = CompositeDisposable()

    protected abstract fun getChannelId(): String

    protected abstract fun getMenu(): Int

    protected abstract fun getMessageCollection(): LiveData<PagedList<EkoMessage>>

    protected abstract fun getDefaultStackFromEnd(): Boolean

    protected abstract fun getDefaultRevertLayout(): Boolean

    protected abstract fun setTitleName()

    protected abstract fun setSubtitleName()

    protected abstract fun startReading()

    protected abstract fun stopReading()

    protected abstract fun onClick(message: EkoMessage)

    protected abstract fun createTextMessage(text: String): Completable

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setTitleName()
        setSubtitleName()
        setSupportActionBar(toolbar)
        setUpInputLayout()
        initialMessageCollection()
    }

    override fun onStart() {
        super.onStart()
        observeMessageCollection()
        startReading()
    }

    override fun onStop() {
        super.onStop()
        stopReading()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(getMenu(), menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_channel_membership) {
            startActivity(ViewChannelMembershipsIntent(this, getChannelId()))
            return true
        } else if (item?.itemId == R.id.action_leave_channel) {
            channelRepository.leaveChannel(getChannelId())
                    .doOnComplete(Action { this.finish() })
                    .subscribe()
            return true
        } else if (item?.itemId == R.id.action_with_tags) {
            showDialog(R.string.with_tag, "bnk48,football,concert", Joiner.on(",").join(includingTags), true, { dialog, input ->
                includingTags.clear()
                for (tag in input.toString().split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                    if (tag.length > 0) {
                        includingTags.add(tag)
                    }
                }
                initialMessageCollection()
                observeMessageCollection()
            })
            return true
        } else if (item?.itemId == R.id.action_without_tags) {
            showDialog(R.string.with_tag, "bnk48,football,concert", Joiner.on(",").join(excludingTags), true, { dialog, input ->
                excludingTags.clear()
                for (tag in input.toString().split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                    if (tag.length > 0) {
                        excludingTags.add(tag)
                    }
                }
                initialMessageCollection()
                observeMessageCollection()
            })
            return true
        } else if (item?.itemId == R.id.action_set_tags) {
            val liveData = channelRepository.getChannel(getChannelId())
            liveData.observeForever(object : Observer<EkoChannel> {
                override fun onChanged(channel: EkoChannel) {
                    liveData.removeObserver(this)
                    showDialog(R.string.set_tags, "bnk48,football,concert", Joiner.on(",").join(channel.tags), true, { dialog, input ->
                        val set = Sets.newConcurrentHashSet<String>()
                        for (tag in input.toString().split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                            if (tag.length > 0) {
                                set.add(tag)
                            }
                        }
                        channelRepository.setTags(channel.channelId, EkoTags(set))
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                    })
                }
            })
            return true
        } else if (item?.itemId == R.id.action_notification_for_current_channel) {
            channelRepository.notification(getChannelId())
                    .isAllowed
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess { allowed ->
                        MaterialDialog(this).show {
                            checkBoxPrompt(text = "allow notification for current channel", isCheckedDefault = allowed, onToggle = null)
                            positiveButton(text = "save changes") {
                                channelRepository.notification(getChannelId())
                                        .setAllowed(isCheckPromptChecked())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe()
                            }
                            negativeButton(text = "discard")

                        }
                    }
                    .subscribe()
            return true
        } else if (item?.itemId == R.id.action_stack_from_end) {
            MaterialDialog(this).show {
                checkBoxPrompt(text = getString(R.string.stack_from_end), isCheckedDefault = stackFromEnd.get(), onToggle = null)
                positiveButton(text = "save change") {
                    stackFromEnd.set(isCheckPromptChecked())
                    initialMessageCollection()
                    observeMessageCollection()
                }
                negativeButton(text = "discard")
            }

            return true
        } else if (item?.itemId == R.id.action_revert_layout) {

            MaterialDialog(this).show {
                checkBoxPrompt(text = getString(R.string.revert_layout), isCheckedDefault = revertLayout.get(), onToggle = null)
                positiveButton(text = "save change") {
                    revertLayout.set(isCheckPromptChecked())
                    initialMessageCollection()
                    observeMessageCollection()
                }
                negativeButton(text = "discard")
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpInputLayout() {

        message_edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(input: Editable?) {
                val text = input.toString().trim { it <= ' ' }
                message_send_button.isEnabled = !TextUtils.isEmpty(text)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }
        })

        message_send_button.setOnClickListener {
            val text = message_edittext.text.toString().trim()
            message_edittext.text = null
            createTextMessage(text)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { t ->
                        val ekoError = EkoError.from(t)
                        if (ekoError == EkoError.USER_IS_BANNED) {
                            message_edittext.post {
                                Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
                            }
                            message_edittext.postDelayed({
                                finish()
                            },
                                    500)
                        }
                    }
                    .doOnComplete {
                        scrollToBottom()
                    }
                    .subscribe()
        }

        EkoClient.newChannelRepository().getChannel(getChannelId()).observe(this, Observer {
            if (EkoChannel.Type.fromJson(it.channelType) == EkoChannel.Type.BROADCAST) {
                message_input_layout.visibility = View.GONE
            }
        })
    }

    private fun onLongClick(message: EkoMessage) {

        if (message.isDeleted) {
            return
        }

        val actionItems = getMessageOptions(message)
        MaterialDialog(this).show {
            listItems(items = actionItems) { dialog, position, text ->
                when (MessageOption.enumOf(text.toString())) {
                    MessageOption.FLAG_MESSAGE -> {
                        flagMessage(message)
                    }
                    MessageOption.FLAG_SENDER -> {
                        flagUser(message.user)
                    }
                    MessageOption.SET_TAG -> {
                        setTags(message)
                    }
                    MessageOption.ADD_REACTION -> {
                        showAddReactionDialog(message)
                    }
                    MessageOption.REMOVE_REACTION -> {
                        showRemoveReactionDialog(message)
                    }
                    MessageOption.EDIT -> {
                        editMessage(message)
                    }
                    MessageOption.DELETE -> {
                        deleteMessage(message)
                    }
                    MessageOption.OPEN_FILE -> {
                        openFile(message)
                    }
                    MessageOption.REACTION_HISTORY -> {
                        showReactionHistory(message)
                    }
                }
            }
        }
    }

    private fun getMessageOptions(message: EkoMessage): List<String> {
        val optionItems = mutableListOf<String>()
        optionItems.add(MessageOption.FLAG_MESSAGE.value)
        optionItems.add(MessageOption.FLAG_SENDER.value)
        optionItems.add(MessageOption.SET_TAG.value)

        if (DataType.from(message.type) == DataType.FILE) {
            optionItems.add(MessageOption.OPEN_FILE.value)
        }

        if (message.userId == EkoClient.getUserId()) {
            if (DataType.from(message.type) == DataType.TEXT
                    || DataType.from(message.type) == DataType.CUSTOM) {
                optionItems.add(MessageOption.EDIT.value)
            }
            optionItems.add(MessageOption.DELETE.value)
        }

        optionItems.add(MessageOption.ADD_REACTION.value)
        if (message.myReactions.isNotEmpty()) {
            optionItems.add(MessageOption.REMOVE_REACTION.value)
        }
        optionItems.add(MessageOption.REACTION_HISTORY.value)

        return optionItems
    }

    private fun editMessage(message: EkoMessage) {
        when (DataType.from(message.type)) {
            DataType.TEXT -> {
                showTextMessageEditor(message)
            }

            DataType.CUSTOM -> {
                showCustomMessageEditor(message)
            }
        }
    }

    private fun deleteMessage(message: EkoMessage) {
        when (DataType.from(message.type)) {
            DataType.TEXT -> {
                message.textMessageEditor?.run {
                    delete().subscribe()
                }
            }
            DataType.IMAGE -> {
                message.imageMessageEditor?.run {
                    delete().subscribe()
                }
            }
            DataType.FILE -> {
                message.fileMessageEditor?.run {
                    delete().subscribe()
                }
            }
            DataType.CUSTOM -> {
                message.customMessageEditor?.run {
                    delete().subscribe()
                }
            }
        }
    }

    private fun flagMessage(message: EkoMessage) {
        if (message.isFlaggedByMe) {
            MaterialDialog(this).show {
                title = "un-flag a message"
                positiveButton(text = "un-flag a message") {
                    disposable.add(messageRepository.report(message.messageId)
                            .unflag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete {
                                Toast.makeText(this@MessageListActivity, "successfully un-flagged a message", Toast.LENGTH_SHORT).show()
                            }
                            .subscribe())
                }
            }

        } else {
            MaterialDialog(this).show {
                title = "flag a message"
                positiveButton(text = "flag") {
                    disposable.add(messageRepository.report(message.messageId)
                            .flag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete {
                                Toast.makeText(this@MessageListActivity, "successfully flagged the a message", Toast.LENGTH_SHORT).show()
                            }
                            .subscribe())
                }
            }
        }
    }

    private fun flagUser(user: EkoUser) {
        if (user.isFlaggedByMe) {
            MaterialDialog(this).show {
                title = "un-flag a sender"
                positiveButton(text = "un-flag") {
                    disposable.add(userRepository.report(user.userId)
                            .unflag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete { Toast.makeText(this@MessageListActivity, "successfully un-flagged a sender", Toast.LENGTH_SHORT).show() }
                            .subscribe())
                }
            }

        } else {
            MaterialDialog(this).show {
                title = "flag a sender"
                positiveButton(text = "flag") {
                    disposable.add(userRepository.report(user.userId)
                            .flag()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete { Toast.makeText(this@MessageListActivity, "successfully flagged a sender", Toast.LENGTH_SHORT).show() }
                            .subscribe())
                }
            }
        }
    }

    private fun setTags(message: EkoMessage) {
        showDialog(R.string.set_tags, "bnk48,football,concert", Joiner.on(",").join(message.tags), true, { dialog, input ->
            val set = Sets.newConcurrentHashSet<String>()
            for (tag in input.toString().split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                if (tag.length > 0) {
                    set.add(tag)
                }
            }
            messageRepository.setTags(message.messageId, EkoTags(set))
                    .subscribeOn(Schedulers.io())
                    .subscribe()
        })
    }

    private fun initialMessageCollection() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = stackFromEnd.get()
        layoutManager.reverseLayout = revertLayout.get()
        message_list_recyclerview.layoutManager = layoutManager

        adapter = MessageListAdapter()
        message_list_recyclerview.adapter = adapter

        disposable.clear()

        disposable.add(adapter!!.onLongClickFlowable
                .doOnNext {
                    onLongClick(it)
                }
                .subscribe())

        disposable.add(adapter!!.onClickFlowable
                .doOnNext {
                    onClick(it)
                }
                .subscribe())
    }

    private fun observeMessageCollection() {
        if (messages != null) {
            messages?.removeObservers(this)
        }

        messages = getMessageCollection()
        messages?.observe(this, Observer { adapter?.submitList(it) })
    }


    private fun showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
        MaterialDialog(this).show {
            title(title)
            input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            scrollToBottom()
        }

    }

    private fun scrollToBottom() {
        message_list_recyclerview.postDelayed({
            val lastPosition = adapter?.itemCount!! - 1
            message_list_recyclerview.scrollToPosition(lastPosition)
        }, 10)
    }

    private fun openFile(message: EkoMessage) {
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe({ granted ->
                    if (granted) {
                        FileManager.openFile(getApplicationContext(), message)
                    }
                })
    }

    private fun showTextMessageEditor(message: EkoMessage) {
        val currentText = message.getData(TextData::class.java).text
        showDialog(R.string.edit_text_message, "enter text", currentText, false, { dialog, input ->
            val modifiedText = input.toString()
            if (modifiedText != currentText) {
                message.textMessageEditor?.run {
                    text(modifiedText)
                            .subscribe()
                }
            }
        })
    }

    private fun showCustomMessageEditor(message: EkoMessage) {
        val dialog = MaterialDialog(this)
                .title(text = "Edit custom message")
                .customView(R.layout.view_edit_custom_message, scrollable = true)

        val customView = dialog.getCustomView()
        val keyEditText = customView.findViewById<EditText>(R.id.key_edittext)
        val valueEditText = customView.findViewById<EditText>(R.id.value_edittext)
        val sendButton = customView.findViewById<Button>(R.id.send_button)

        keyEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sendButton.isEnabled = s?.isNotEmpty() ?: false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }
        })

        sendButton.setOnClickListener {
            it.isEnabled = false
            sendEditCustomMessageRequest(message, keyEditText.text.toString().trim(), valueEditText.text.toString())
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun sendEditCustomMessageRequest(message: EkoMessage, key: String, value: String) {
        val customData = JsonObject()
        customData.addProperty(key, value)

        val editor = message.customMessageEditor
        editor?.run {
            custom(customData)
                    .subscribe()
        }
    }

    private fun showAddReactionDialog(message: EkoMessage) {
        val reactionItems = mutableListOf<String>()
        ReactionOption.values().filter {
            !message.myReactions.contains(it.value())
        }.forEach {
            reactionItems.add(it.value())
        }

        if (reactionItems.isNullOrEmpty()) {
            return
        }

        MaterialDialog(this).show {
            listItems(items = reactionItems) { dialog, position, text ->
                message.react()
                        .addReaction(text.toString())
                        .subscribe()
            }
        }
    }

    private fun showRemoveReactionDialog(message: EkoMessage) {
        val reactionItems = message.myReactions
        MaterialDialog(this).show {
            listItems(items = reactionItems) { dialog, position, text ->
                message.react()
                        .removeReaction(text.toString())
                        .subscribe()
            }
        }
    }

    private fun showReactionHistory(message: EkoMessage) {
        startActivity(OpenMessageReactionListIntent(this, message.messageId))
    }

    override fun getLayout(): Int {
        return R.layout.activity_message_list
    }

}