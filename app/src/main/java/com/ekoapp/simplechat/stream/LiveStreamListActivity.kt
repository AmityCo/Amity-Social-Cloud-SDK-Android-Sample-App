package com.ekoapp.simplechat.stream

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.sdk.BuildConfig
import com.ekoapp.ekosdk.stream.EkoStream
import com.ekoapp.simplechat.R
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_channel_list.toolbar
import kotlinx.android.synthetic.main.activity_stream_list.*

open class LiveStreamListActivity : AppCompatActivity() {

    private var streamDisposable: Disposable? = null
    private val streamRepository = EkoClient.newStreamRepository()

    private var adapter: StreamListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream_list)
        setupToolbar()
        observeStreamCollection()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (streamDisposable?.isDisposed == false) {
            streamDisposable?.dispose()
        }
    }

    open protected fun isLive(): Boolean = true

    open protected fun subtitle(): String = "Live Stream"

    private fun setupToolbar() {
        val appName = getString(R.string.app_name)
        toolbar.title = String.format("%s %s: %s", appName, "Eko SDK", BuildConfig.VERSION_NAME)
        toolbar.subtitle = String.format("%s: %s", subtitle(), baseContext.getString(R.string.sdk_environment))
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
    }

    private fun observeStreamCollection() {
        adapter = StreamListAdapter()
        stream_list_recyclerview.adapter = adapter
        streamDisposable = getLiveStreams()
                .subscribe({ onCollectionUpdated(it) }, { })
    }

    private fun onCollectionUpdated(pagedList: PagedList<EkoStream>) {
        adapter?.submitList(pagedList)
        val isEmpty = pagedList.isNullOrEmpty()
        if (isEmpty) {
            stream_list_recyclerview.visibility = View.GONE
            stream_empty_state_container.visibility = View.VISIBLE
        } else {
            stream_list_recyclerview.visibility = View.VISIBLE
            stream_empty_state_container.visibility = View.GONE
        }
    }

    private fun getLiveStreams(): Flowable<PagedList<EkoStream>> {
        return streamRepository
                .getStreamCollection()
                .setIsLive(isLive())
                .build()
                .query()
    }
}