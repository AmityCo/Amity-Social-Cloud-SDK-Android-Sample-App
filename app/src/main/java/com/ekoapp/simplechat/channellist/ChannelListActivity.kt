package com.ekoapp.simplechat.channellist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
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
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.SimpleConfig
import com.ekoapp.simplechat.SimplePreferences
import com.ekoapp.simplechat.channellist.filter.ChannelQueryFilterActivity
import com.ekoapp.simplechat.intent.IntentRequestCode
import com.ekoapp.simplechat.intent.OpenChangeMetadataIntent
import com.google.android.material.snackbar.Snackbar
import com.google.common.base.Joiner
import com.google.common.base.MoreObjects
import com.google.common.collect.FluentIterable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_channel_list.*
import org.bson.types.ObjectId
import java.util.*


class ChannelListActivity : AppCompatActivity() {

    private var channels: LiveData<PagedList<EkoChannel>>? = null

    private val channelRepository = EkoClient.newChannelRepository()


    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_channel_list)
        val appName = getString(R.string.app_name)
        toolbar.title = String.format("%s %s: %s", appName, "Eko SDK", BuildConfig.VERSION_NAME)
        toolbar.subtitle = String.format("%s", BuildConfig.EKO_HTTP_URL)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)

        val userId = MoreObjects.firstNonNull(EkoClient.getUserId(), SimpleConfig.DEFAULT_USER_ID)
        val displayName = MoreObjects.firstNonNull(EkoClient.getDisplayName(), "Android " + ObjectId.get())

        EkoClient.registerDevice(userId, displayName)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { this.observeChannelCollection() }
                .subscribe()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
        }

        query_filter_textview.setOnClickListener {
            startActivityForResult(Intent(this, ChannelQueryFilterActivity::class.java),
                    IntentRequestCode.REQUEST_CHANNEL_FILTER_OPTION)
        }

        LiveDataReactiveStreams.fromPublisher(channelRepository.getTotalUnreadCount())
                .observe(this, Observer<Int> { totalUnreadCount ->
                    val totalUnreadCountString = getString(R.string.total_unread_d, totalUnreadCount)
                    total_unread_textview.text = totalUnreadCountString

                })
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
            EkoClient.unregisterDevice().subscribe()
            return true
        } else if (id == R.id.action_change_display_name) {
            showDialog(R.string.change_display_name, "", EkoClient.getDisplayName(), false, { dialog, input ->
                val displayName = input.toString()
                EkoClient.setDisplayName(displayName)
                        .subscribe()
            })
            return true
        } else if (id == R.id.action_change_metadata) {
            val metadata = EkoClient.getUserMetadata() ?: "Metadata has not been set"
            val intent = OpenChangeMetadataIntent(this)
            val context = this
            MaterialDialog(this).show {
                title(R.string.change_metadata)
                message(null, metadata.toString(), null)
                positiveButton(
                        null,
                        "Set",
                        {
                            context.startActivity(intent)
                        })
            }
            return true
        } else if (id == R.id.action_join_channel) {
            val channelTypeItems = ArrayList<String>()
            channelTypeItems.run {
                add(EkoChannel.Type.STANDARD.apiKey)
                add(EkoChannel.Type.PRIVATE.apiKey)
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
                    .doOnSuccess { allowed ->
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
        channel_list_recyclerview.adapter = adapter

        displayQueryOptions()
        channels = getChannelsLiveData()
        channels?.observe(this, Observer<PagedList<EkoChannel>> { adapter.submitList(it) })
    }

    private fun getChannelsLiveData(): LiveData<PagedList<EkoChannel>> {

        val types = FluentIterable.from(SimplePreferences.getChannelTypeOptions().get())
                .transform {
                    EkoChannel.Type.fromJson(it)
                }.toSet()

        val filter = EkoChannelFilter.fromApiKey(SimplePreferences.getChannelMembershipOption().get())
        val includingTags = EkoTags(SimplePreferences.getIncludingChannelTags().get())
        val excludingTags = EkoTags(SimplePreferences.getExcludingChannelTags().get())

        return channelRepository
                .channelCollection
                .byTypes()
                .types(types)
                .filter(filter)
                .includingTags(includingTags)
                .excludingTags(excludingTags)
                .build()
                .query()
    }

    private fun displayQueryOptions() {
        val types = "Types: " + Joiner.on(",").join(SimplePreferences.getChannelTypeOptions().get())
        val filter = "\nMembership: " + SimplePreferences.getChannelMembershipOption().get()
        val includeTags = "\nincludeTags: " + Joiner.on(",").join(SimplePreferences.getIncludingChannelTags().get())
        val excludingTags = "\nexcludingTags: " + Joiner.on(",").join(SimplePreferences.getExcludingChannelTags().get())

        query_filter_textview.text = types + filter + includeTags + excludingTags
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IntentRequestCode.REQUEST_CHANNEL_FILTER_OPTION) {
            observeChannelCollection()
        }
    }

    private fun showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
        MaterialDialog(this).show {
            title(title)
            input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
        }
    }

}