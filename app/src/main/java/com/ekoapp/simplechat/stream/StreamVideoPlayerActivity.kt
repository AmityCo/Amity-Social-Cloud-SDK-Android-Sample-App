package com.ekoapp.simplechat.stream

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.stream.EkoStreamFormat
import com.ekoapp.ekosdk.stream.EkoLiveStreamURLInfo
import com.ekoapp.simplechat.R
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_stream_video_player.*


open class StreamVideoPlayerActivity : AppCompatActivity() {

    var exoplayer: SimpleExoPlayer? = null
    var urlDisposable: Disposable? = null
    var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream_video_player)
        supportActionBar?.hide()
        getVideoURL()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoplayer?.stop()
        if (urlDisposable?.isDisposed == false) {
            urlDisposable?.dispose()
        }
    }

    private fun getVideoURL() {
        val streamId = StreamVideoPlayerIntent.getStreamId(intent)
        urlDisposable = EkoClient.newStreamRepository()
                .getLiveUrl(streamId, EkoStreamFormat.FLV)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { startStreaming(it) }, URLErrorConsumer(this))
    }

    private fun startStreaming(liveStreamUrlInfo: EkoLiveStreamURLInfo) {
        initPlayer()
        prepareVideo(liveStreamUrlInfo.url)
    }

    private fun initPlayer() {
        exoplayer = SimpleExoPlayer.Builder(this).build()
        exoplayer?.playWhenReady = true
        exoplayer?.addListener(object : Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
               seekToLastWhenResume(isPlaying)
            }
        })
        video_viewer.player = exoplayer
    }

    private fun seekToLastWhenResume(isPlaying: Boolean) {
        if (isPlaying && exoplayer?.playbackState == Player.STATE_READY) {
            if (isPaused) {
                exoplayer?.seekTo(Long.MAX_VALUE)
                isPaused = false
            }
        } else if (!isPlaying && exoplayer?.playbackState == Player.STATE_READY) {
            isPaused = true
        }
    }

    private fun prepareVideo(url: String) {
        val newUrl = url.replace("https", "http")
        video_viewer.requestFocus()
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(getDataSourceFactory(), DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(newUrl))
        exoplayer?.prepare(videoSource)
    }

    open protected fun getDataSourceFactory(): DataSource.Factory {
        return DefaultDataSourceFactory(this,
                Util.getUserAgent(this, resources.getString(R.string.app_name)))
    }

    class URLErrorConsumer(val context: Context) : Consumer<Throwable> {
        override fun accept(t: Throwable?) {
            Toast.makeText(context, "Sorry, unable to play this stream", Toast.LENGTH_SHORT).show()
        }
    }
}