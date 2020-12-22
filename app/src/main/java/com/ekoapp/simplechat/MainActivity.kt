package com.ekoapp.simplechat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.simplechat.stream.LiveStreamListActivity
import com.ekoapp.simplechat.stream.RecordedStreamListActivity
import com.ekoapp.simplechat.channellist.ChannelListActivity
import com.ekoapp.simplechat.intent.OpenCommentContentListIntent
import com.ekoapp.simplechat.myuser.MyUserActivity
import com.ekoapp.simplechat.userlist.UserListActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val menuItems = PageListType.values().map { it.value }
    private val myUserId = SimplePreferences.getMyUserId()
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerSession(SimplePreferences.getMyUserId().get())
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_register -> {
                showDialog(R.string.register, "", myUserId.get(), false) { _, input ->
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
                val apiKeyStore = SimplePreferences.getApiKey()
                showDialog(R.string.change_api_key, "", apiKeyStore.get(), false) { _, input ->
                    val newApiKey = input.toString()
                    apiKeyStore.set(newApiKey)
                    EkoClient.setup(newApiKey)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun initView() {
        val adapter = StringListAdapter(menuItems, object : StringListAdapter.StringItemListener {
            override fun onClick(text: String) {
                onMainMenuItemSelected(text)
            }
        })
        menu_list.adapter = adapter

        val appName = getString(R.string.app_name)
        toolbar.title = String.format("%s %s: %s", appName, "Eko SDK", BuildConfig.VERSION_NAME)
        toolbar.subtitle = String.format("env: %s", getString(R.string.sdk_environment))
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
    }

    private fun onMainMenuItemSelected(item: String) {
        when (PageListType.enumOf(item)) {
            PageListType.CHANNEL_LIST -> {
                startActivity(Intent(this, ChannelListActivity::class.java))
            }
            PageListType.USER_LIST -> {
                startActivity(Intent(this, UserListActivity::class.java))
            }
            PageListType.MY_USER -> {
                startActivity(Intent(this, MyUserActivity::class.java))
            }
            PageListType.COMMENT_CONTENT -> {
                startActivity(OpenCommentContentListIntent(this, "androidContentId"))
            }
            PageListType.LIVE_STREAM_LIST -> {
                startActivity(Intent(this, LiveStreamListActivity::class.java))
            }
            PageListType.RECORDED_STREAM_LIST -> {
                startActivity(Intent(this, RecordedStreamListActivity::class.java))
            }
            else -> {
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

    enum class PageListType(val value: String) {
        CHANNEL_LIST("Channel list"),
        USER_LIST("User list"),
        MY_USER("My User"),
        COMMENT_CONTENT("Comment Content"),
        LIVE_STREAM_LIST("Live Streaming list"),
        RECORDED_STREAM_LIST("Recorded Streaming list");

        companion object {
            fun enumOf(value: String): PageListType? = values().find { it.value == value }
        }
    }
}