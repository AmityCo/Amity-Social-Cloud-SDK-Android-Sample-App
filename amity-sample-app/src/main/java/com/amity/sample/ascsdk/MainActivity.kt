package com.amity.sample.ascsdk

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amity.sample.ascsdk.channellist.ChannelListActivity
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.common.preferences.SamplePreferences
import com.amity.sample.ascsdk.common.view.StringListAdapter
import com.amity.sample.ascsdk.community.CommunityListActivity
import com.amity.sample.ascsdk.community.category.CommunityCategoryList
import com.amity.sample.ascsdk.community.recommended.CommunityRecommendedActivity
import com.amity.sample.ascsdk.community.trending.CommunityTrendingActivity
import com.amity.sample.ascsdk.intent.OpenCommentContentListIntent
import com.amity.sample.ascsdk.intent.OpenMyUserFollowInfoIntent
import com.amity.sample.ascsdk.myuser.MyUserActivity
import com.amity.sample.ascsdk.notificationsettings.AmityStreamNotificationHandler
import com.amity.sample.ascsdk.notificationsettings.NotificationSettingsActivity
import com.amity.sample.ascsdk.post.GlobalFeedActivity
import com.amity.sample.ascsdk.post.MyFeedActivity
import com.amity.sample.ascsdk.stream.LiveStreamListActivity
import com.amity.sample.ascsdk.stream.RecordedStreamListActivity
import com.amity.sample.ascsdk.stream.StreamerActivity
import com.amity.sample.ascsdk.userlist.UserListActivity
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.sdk_versioning.Environment
import com.amity.sdk_versioning.EnvironmentActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val channelList = "Channel list"
        const val communityList = "Community list"
        const val communityCategoryList = "Community Category list"
        const val recommendedCommunity = "Recommended Community List"
        const val trendingCommunity = "Trending Community List"
        const val userList = "User list"
        const val myFeed = "My Feed"
        const val globalFeed = "Global Feed"
        const val myUser = "My User"
        const val myFollowInfo = "My Follow Info"
        const val notificationSettings = "Notification Settings"
        const val commentContent = "Comment Content"
        const val liveStreaming = "Live Streaming List"
        const val recordedStreaming = "Recorded Streaming List"
        const val streamPublisher = "Stream Publisher"
    }

    private val menuItems = listOf(
        channelList, communityList, communityCategoryList, recommendedCommunity,
        trendingCommunity, userList, myFeed, globalFeed, myUser, myFollowInfo,
        notificationSettings, commentContent, liveStreaming, recordedStreaming, streamPublisher
    )
    private val myUserId = SamplePreferences.getMyUserId()
    private var compositeDisposable = CompositeDisposable()

    private val changeEnvContract = registerForActivityResult(
        EnvironmentActivity.ChangeEnvironmentContract()
    ){
        if (it != null) {
            SamplePreferences.getApiKey().set(it.apiKey)
            SamplePreferences.getHttpUrl().set(it.httpUrl)
            SamplePreferences.getSocketUrl().set(it.socketUrl)
            registerSession(SamplePreferences.getMyUserId().get())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerSession(SamplePreferences.getMyUserId().get())
        initView()
        handleVideoNotification()
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
                AmityCoreClient.unregisterDevice()
                    .andThen(AmityCoreClient.unregisterDeviceForPushNotification())
                    .subscribe()
                return true
            }
            R.id.action_change_environment -> {
                val env = Environment(
                    SamplePreferences.getApiKey().get(),
                    SamplePreferences.getHttpUrl().get(),
                    SamplePreferences.getSocketUrl().get()
                )
                changeEnvContract.launch(env)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        supportActionBar?.subtitle =
            String.format("SDK version %s", AmityCoreClient.getAmityCoreSdkVersion())
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
            recommendedCommunity -> {
                startActivity(Intent(this, CommunityRecommendedActivity::class.java))
            }
            trendingCommunity -> {
                startActivity(Intent(this, CommunityTrendingActivity::class.java))
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
            myFollowInfo -> {
                startActivity(OpenMyUserFollowInfoIntent(this))
            }
            notificationSettings -> {
                startActivity(Intent(this, NotificationSettingsActivity::class.java))
            }
            commentContent -> {
                startActivity(OpenCommentContentListIntent(this, "androidContentId"))
            }
            liveStreaming -> {
                startActivity(Intent(this, LiveStreamListActivity::class.java))
            }
            recordedStreaming -> {
                startActivity(Intent(this, RecordedStreamListActivity::class.java))
            }
            streamPublisher -> {
                startActivity(Intent(this, StreamerActivity::class.java))
            }
        }
    }

    private fun registerSession(userIdInput: CharSequence) {
        progressbar.visibility = View.VISIBLE
        val userId = userIdInput.toString().trim()
        val disposable = AmityCoreClient.registerDevice(userId).build().submit()
            .andThen(AmityCoreClient.registerDeviceForPushNotification())
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

    private fun handleVideoNotification() {

        if (intent?.extras?.get("videoStreamings") != null) {
            val data = intent?.extras?.get("videoStreamings")
            if (data is String) {
                AmityStreamNotificationHandler.navigate(this, data)
            }
        } else if (intent?.extras?.get("streamId") != null) {
            val data = intent?.extras?.get("streamId")
            if (data is String) {
                AmityStreamNotificationHandler.navigate(this, data)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}