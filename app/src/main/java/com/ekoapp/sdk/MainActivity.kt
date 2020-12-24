package com.ekoapp.sdk

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sdk.channellist.ChannelListActivity
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.common.preferences.SamplePreferences
import com.ekoapp.sdk.common.view.StringListAdapter
import com.ekoapp.sdk.community.CommunityListActivity
import com.ekoapp.sdk.community.category.CommunityCategoryList
import com.ekoapp.sdk.intent.OpenCommentContentListIntent
import com.ekoapp.sdk.myuser.MyUserActivity
import com.ekoapp.sdk.notificationsettings.NotificationSettingsActivity
import com.ekoapp.sdk.post.GlobalFeedActivity
import com.ekoapp.sdk.post.MyFeedActivity
import com.ekoapp.sdk.stream.StreamListActivity
import com.ekoapp.sdk.userlist.UserListActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val channelList = "Channel list"
        const val communityList = "Community list"
        const val communityCategoryList = "Community Category list"
        const val userList = "User list"
        const val myFeed = "My Feed"
        const val globalFeed = "Global Feed"
        const val myUser = "My User"
        const val notificationSettings = "Notification Settings"
        const val commentContent = "Comment Content"
        const val liveStreaming = "Live Streaming"
    }

    private val menuItems = listOf(channelList, communityList, communityCategoryList,
            userList, myFeed, globalFeed, myUser, notificationSettings, commentContent, liveStreaming)
    private val myUserId = SamplePreferences.getMyUserId()
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerSession(SamplePreferences.getMyUserId().get())
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_register -> {
                showDialog(R.string.register, "", myUserId.get(), false) { dialog, input ->
                    val userId = input.toString()
                    registerSession(userId)
                }
                return true
            }
            R.id.action_unregister -> {
                EkoClient.unregisterDevice().subscribe()
                return true
            }
            R.id.action_change_api_key -> {
                val apiKeyStore = SamplePreferences.getApiKey()
                showDialog(R.string.change_api_key, "", apiKeyStore.get(), false) { dialog, input ->
                    val newApiKey = input.toString()
                    apiKeyStore.set(newApiKey)
                    EkoClient.setup(newApiKey)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        val adapter = StringListAdapter(menuItems, object : StringListAdapter.StringItemListener {
            override fun onClick(text: String) {
                onMainMenuItemSelected(text)
            }
        })
        menu_list.adapter = adapter
    }

    private fun onMainMenuItemSelected(item: String) {
        when (item) {
            channelList -> {
                startActivity(Intent(this, ChannelListActivity::class.java))
            }
            communityList -> {
                startActivity(Intent(this, CommunityListActivity::class.java))
            }
            communityCategoryList -> {
                startActivity(Intent(this, CommunityCategoryList::class.java))
            }
            userList -> {
                startActivity(Intent(this, UserListActivity::class.java))
            }
            globalFeed -> {
                startActivity(Intent(this, GlobalFeedActivity::class.java))
            }
            myFeed -> {
                startActivity(Intent(this, MyFeedActivity::class.java))
            }
            myUser -> {
                startActivity(Intent(this, MyUserActivity::class.java))
            }
            notificationSettings -> {
                startActivity(Intent(this, NotificationSettingsActivity::class.java))
            }
            commentContent -> {
                //TODO use the real content id
                startActivity(OpenCommentContentListIntent(this, "androidContentId"))
            }
            liveStreaming -> {
                startActivity(Intent(this, StreamListActivity::class.java))
            }
        }
    }

    private fun registerSession(userIdInput: CharSequence) {

        progressbar.visibility = View.VISIBLE
        val userId = userIdInput.toString().trim()
        val disposable = EkoClient.registerDevice(userId, userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    progressbar.visibility = View.GONE
                    myUserId.set(userId)
                    showToast("Register user successful !")
                }, {
                    showToast("Register user failed !")
                    progressbar.visibility = View.GONE
                })
        compositeDisposable.add(disposable)

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}