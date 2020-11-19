package com.ekoapp.simplechat.stream

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.sdk.BuildConfig
import com.ekoapp.ekosdk.stream.EkoStream
import com.ekoapp.simplechat.R
import kotlinx.android.synthetic.main.activity_channel_list.toolbar
import kotlinx.android.synthetic.main.activity_stream_list.*

class StreamListActivity : AppCompatActivity() {

    private var streams: LiveData<PagedList<EkoStream>>? = null
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
        streams?.removeObservers(this)
    }

    private fun setupToolbar() {
        toolbar.title = String.format("%s : %s", "Eko SDK", BuildConfig.VERSION_NAME)
        toolbar.subtitle = String.format("Stream service")
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
    }

    private fun observeStreamCollection() {
        adapter = StreamListAdapter()
        stream_list_recyclerview.adapter = adapter
        streams?.removeObservers(this)
        streams = getStreamsLiveData()
        streams?.observe(this, Observer { onCollectionUpdated(it) })
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

    private fun getStreamsLiveData(): LiveData<PagedList<EkoStream>> {
        return LiveDataReactiveStreams
                .fromPublisher(streamRepository
                        .getLiveStreamCollection()
                        .build()
                        .query())
    }
}