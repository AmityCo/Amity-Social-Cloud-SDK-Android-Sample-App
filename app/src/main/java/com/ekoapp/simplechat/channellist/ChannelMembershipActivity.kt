package com.ekoapp.simplechat.channellist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.channel.membership.query.EkoChannelMembershipFilter
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.intent.ViewChannelMembershipsIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_channel_membership_list.*

class ChannelMembershipActivity : AppCompatActivity() {

    var channelId: String = ""
    val adapter = ChannelMembershipAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_membership_list)
        channelId = ViewChannelMembershipsIntent.getChannelId(intent)
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
        EkoClient.newChannelRepository()
                .membership(channelId)
                .getCollection()
                .filter(EkoChannelMembershipFilter.ALL)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adapter::submitList)
                .doOnError(Throwable::printStackTrace)
                .subscribe()
    }

    private fun getMembershipsByRole(input: CharSequence) {
        EkoClient.newChannelRepository()
                .membership(channelId)
                .getCollection()
                .filter(EkoChannelMembershipFilter.ALL)
                .role(input.toString())
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adapter::submitList)
                .doOnError(Throwable::printStackTrace)
                .subscribe()
    }
}