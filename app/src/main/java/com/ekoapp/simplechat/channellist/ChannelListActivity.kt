package com.ekoapp.simplechat.channellist

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.checkbox.isCheckPromptChecked
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoChannel
import com.ekoapp.ekosdk.EkoChannelFilter
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoTags
import com.ekoapp.ekosdk.sdk.BuildConfig
import com.ekoapp.simplechat.BaseActivity
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.SimpleConfig
import com.ekoapp.simplechat.SimplePreferences
import com.ekoapp.simplechat.channellist.option.ChannelTypeOption
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.common.base.Joiner
import com.google.common.base.MoreObjects
import com.google.common.collect.Sets
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.bson.types.ObjectId
import java.util.*


class ChannelListActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    @JvmField var toolbar: Toolbar? = null

    @BindView(R.id.total_unread_textview)
    @JvmField var totalUnreadTextView: TextView? = null

    @BindView(R.id.filter_spinner)
    @JvmField var spinner: Spinner? = null

    @BindView(R.id.fab)
    @JvmField var fab: FloatingActionButton? = null

    @BindView(R.id.channel_list_recyclerview)
    @JvmField var channelListRecyclerView: RecyclerView? = null

    private var filter = EkoChannelFilter.ALL

    private var channels: LiveData<PagedList<EkoChannel>>? = null

    private val channelRepository = EkoClient.newChannelRepository()

    private val includingTags = SimplePreferences.getIncludingChannelTags()
    private val excludingTags = SimplePreferences.getExcludingChannelTags()


    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_channel_list)
        val appName = getString(R.string.app_name)
        toolbar?.title = String.format("%s %s: %s", appName, "Eko SDK", BuildConfig.VERSION_NAME)
        toolbar?.subtitle = String.format("%s", BuildConfig.EKO_HTTP_URL)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar?.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)

        val userId = MoreObjects.firstNonNull(EkoClient.getUserId(), SimpleConfig.DEFAULT_USER_ID)
        val displayName = MoreObjects.firstNonNull(EkoClient.getDisplayName(), "Android " + ObjectId.get())

        EkoClient.registerDevice(userId, displayName)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { this.setupChannelList() }
                .subscribe()

        fab?.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
        }

        val resources = getResources()

        LiveDataReactiveStreams.fromPublisher(channelRepository.getTotalUnreadCount())
                .observe(this, object : Observer<Int> {
                    override fun onChanged(totalUnreadCount: Int) {
                        val totalUnreadCountString = resources.getString(R.string.total_unread_d, totalUnreadCount)
                        totalUnreadTextView?.text = totalUnreadCountString
                    }
                })
    }

    private fun setupChannelList() {
        val modes = arrayOf<String>(EkoChannelFilter.ALL.apiKey, EkoChannelFilter.MEMBER.getApiKey(), EkoChannelFilter.NOT_MEMBER.apiKey)

        spinner?.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                modes)

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                filter = EkoChannelFilter.fromApiKey(modes[position])
                observeChannelCollection()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_channel_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_register) {
            showDialog(R.string.register, "", EkoClient.getUserId(), false, { dialog, input ->
                val userId = input.toString()
                EkoClient.registerDevice(userId, userId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete { observeChannelCollection() }
                        .subscribe()
            })
            return true
        } else if (id == R.id.action_unregister) {
            EkoClient.unregisterDevice()
                    .subscribe()
            return true
        } else if (id == R.id.action_change_display_name) {
            showDialog(R.string.change_display_name, "", EkoClient.getDisplayName(), false, { dialog, input ->
                val displayName = input.toString()
                EkoClient.setDisplayName(displayName)
                        .subscribe()
            })
            return true
        } else if (id == R.id.action_join_channel) {
            val channelTypeItems = ArrayList<String>()
            channelTypeItems.run {
                add(ChannelTypeOption.STANDARD.value())
            }

            MaterialDialog(this).show {
                listItems(items = channelTypeItems) { dialog, index, text ->
                    showDialog(R.string.join_channel, "", "", false, { d, input ->
                        val channelId = input.toString()
                        channelRepository.getOrCreateById(channelId, EkoChannel.Type.fromJson(text.toString()))
                    })
                }
            }
            return true
        } else if (id == R.id.action_change_api_key) {
            val apiKeyStore = SimplePreferences.getApiKey()
            showDialog(R.string.change_api_key, "", apiKeyStore.get(), false, { dialog, input ->
                val newApiKey = input.toString()
                apiKeyStore.set(newApiKey)
                EkoClient.setup(newApiKey)
            })
            return true
        } else if (id == R.id.action_with_tags) {
            showDialog(R.string.with_tag, "bnk48,football,concert", Joiner.on(",").join(includingTags.get()), true, { dialog, input ->
                val set = Sets.newConcurrentHashSet<String>()
                for (tag in input.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                    if (tag.length > 0) {
                        set.add(tag)
                    }
                }
                includingTags.set(set)
                observeChannelCollection()
            })
            return true
        } else if (id == R.id.action_without_tags) {
            showDialog(R.string.with_tag, "bnk48,football,concert", Joiner.on(",").join(excludingTags.get()), true, { dialog, input ->
                val set = Sets.newConcurrentHashSet<String>()
                for (tag in input.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()) {
                    if (tag.length > 0) {
                        set.add(tag)
                    }
                }
                excludingTags.set(set)
                observeChannelCollection()
            })
            return true
        } else if (id == R.id.action_register_push) {
            EkoClient.registerDeviceForPushNotification()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { Toast.makeText(this, String.format("register push for %s", EkoClient.getUserId()), Toast.LENGTH_SHORT).show() }
                    .subscribe()
            return true
        } else if (id == R.id.action_unregister_push) {
            EkoClient.unregisterDeviceForPushNotification(EkoClient.getUserId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { Toast.makeText(this, String.format("un-register push for %s", EkoClient.getUserId()), Toast.LENGTH_SHORT).show() }
                    .subscribe()
            return true
        } else if (id == R.id.action_unregister_push_for_all) {
            EkoClient.unregisterDeviceForPushNotification()
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnComplete { Toast.makeText(this, "un-register push for all users", Toast.LENGTH_SHORT).show() }
                    .subscribe()
            return true
        } else if (id == R.id.action_notification_for_current_user) {
            EkoClient.notification()
                    .isAllowed()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess{ allowed ->
                        MaterialDialog(this).show {
                            title(text = "Notification Settings")
                            checkBoxPrompt(text = "allow notification for current user", isCheckedDefault = allowed, onToggle = null)
                            positiveButton(text = "save change") {
                                EkoClient.notification()
                                        .setAllowed(isCheckPromptChecked())
                                        .subscribeOn(Schedulers.io())
                                        .subscribe()
                            }
                            negativeButton(text = "discard")
                        }
                    }
                    .subscribe()
            return true
        } else if (id == R.id.action_notification_request_write_settings_permission) {
            // required for baidu push
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + getPackageName())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeChannelCollection() {
        channels?.removeObservers(this)
        val adapter = ChannelListAdapter()
        channelListRecyclerView?.adapter = adapter

        channels = channelRepository.getChannelCollectionByTags(filter, EkoTags(includingTags.get()), EkoTags(excludingTags.get()))
        channels?.observe(this, Observer<PagedList<EkoChannel>> { adapter.submitList(it) })
    }


    private fun showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
        MaterialDialog(this).show {
            title(title)
            input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
        }
    }

}