package com.amity.sample.ascsdk.channellist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.socialcloud.sdk.chat.AmityChatClient
import com.amity.socialcloud.sdk.chat.channel.AmityChannelMembershipFilter
import com.amity.socialcloud.sdk.chat.channel.AmityChannelMembershipSortOption
import com.amity.socialcloud.sdk.core.permission.AmityRoles

import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.ViewChannelMembershipsIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_channel_membership_list.*


class ChannelMembershipActivity : AppCompatActivity() {

    var channelId: String = ""
    val adapter = ChannelMembershipAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_membership_list)
        channelId = ViewChannelMembershipsIntent.getChannelId(intent)
        initialListener()
        toolbar.title = channelId
        toolbar.subtitle = "member"
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
        channel_membership_list_recyclerview.layoutManager = LinearLayoutManager(this)
        channel_membership_list_recyclerview.adapter = adapter
        getAllMembership()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_channel_member_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_role) {
            val mockRole = "moderator"
            showDialog(R.string.role, "", mockRole, false) { _, input ->
                getMembershipsByRole(input)
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getAllMembership() {
        val disposable = AmityChatClient.newChannelRepository()
                .membership(channelId)
                .getMembers()
                .filter(AmityChannelMembershipFilter.ALL)
                .sortBy(AmityChannelMembershipSortOption.LAST_CREATED)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adapter::submitList)
                .doOnError(Throwable::printStackTrace)
                .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun getMembershipsByRole(input: CharSequence) {
        val roles = AmityRoles(input.toString().split(",").toSet())
        val disposable = AmityChatClient.newChannelRepository()
                .membership(channelId)
                .getMembers()
                .filter(AmityChannelMembershipFilter.ALL)
                .roles(roles)
                .sortBy(AmityChannelMembershipSortOption.LAST_CREATED)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adapter::submitList)
                .doOnError(Throwable::printStackTrace)
                .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun initialListener() {
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        Consumer {
                            //TODO handle when click a user view item.
                        }, Consumer {}
                )


        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        Consumer {
                            onChannelMembershipLongClick(it.getChannelId(), it.getUserId())
                        }, Consumer {}
                )
    }

    private fun getChannelMembershipOptions(): List<String> {
        val optionItems = mutableListOf<String>()
        optionItems.add(ChannelMembershipOption.ADD_ROLE.value)
        optionItems.add(ChannelMembershipOption.REMOVE_ROLE.value)
        return optionItems
    }

    private fun onChannelMembershipLongClick(channelId: String, userId: String) {
        MaterialDialog(this).show {
            listItems(items = getChannelMembershipOptions()) { _, _, text ->
                when (ChannelMembershipOption.enumOf(text.toString())) {
                    ChannelMembershipOption.ADD_ROLE -> {
                        showDialog(R.string.add_role, "", "", false) { _, input ->
                            if (input.isNotEmpty()) {
                                addRole(channelId, input, userId)
                            }
                        }
                    }
                    ChannelMembershipOption.REMOVE_ROLE -> {
                        showDialog(R.string.remove_role, "", "", false) { _, input ->
                            if (input.isNotEmpty()) {
                                removeRole(channelId, input, userId)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addRole(channelId: String, input: CharSequence, userId: String) {
        compositeDisposable.add(AmityChatClient.newChannelRepository().moderation(channelId)
                .addRole(role = input.toString(), userIds = listOf(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    showToast("Successfully! Add role")
                }
                .doOnError {
                    showToast("Failed! Add role")
                }
                .subscribe())
    }

    private fun removeRole(channelId: String, input: CharSequence, userId: String) {
        compositeDisposable.add(AmityChatClient.newChannelRepository().moderation(channelId)
                .removeRole(role = input.toString(), userIds = listOf(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    showToast("Successfully! Remove role")
                }
                .doOnError {
                    showToast("Failed! Remove role")
                }
                .subscribe())
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
