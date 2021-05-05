package com.amity.sample.ascsdk.stream

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.amity.socialcloud.sdk.video.AmityVideoClient
import com.amity.socialcloud.sdk.video.stream.AmityRecordingData
import com.amity.socialcloud.sdk.video.stream.AmityStream
import com.amity.socialcloud.sdk.video.stream.AmityWatcherData
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.StreamVideoPlayerIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_stream_amity_video_player.*


open class StreamAmityVideoPlayerActivity : AppCompatActivity() {

    var urlDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream_amity_video_player)
        supportActionBar?.hide()
        getVideoURL()
    }

    override fun onDestroy() {
        super.onDestroy()
        amity_video_player?.stop()
        if (urlDisposable?.isDisposed == false) {
            urlDisposable?.dispose()
        }
    }

    override fun onResume() {
        super.onResume()
        amity_video_player?.resume()
    }

    override fun onPause() {
        super.onPause()
        amity_video_player?.pause()
    }

    private fun getVideoURL() {
        val streamId = StreamVideoPlayerIntent.getStreamId(intent)
        urlDisposable = AmityVideoClient.newStreamRepository()
                .getStream(streamId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { startStreaming(it) }, URLErrorConsumer(this))
    }

    private fun startStreaming(stream: AmityStream) {
        val isLive = stream.getStatus() == AmityStream.Status.LIVE
        val isRecorded = stream.getStatus() == AmityStream.Status.RECORDED
        //If stream is still live, get live-stream url from watcherData
        if (isLive) {
            val url = stream.getWatcherData()?.getUrl(AmityWatcherData.Format.FLV)
            url?.let {
                amity_video_player.enableStopWhenPause()
                amity_video_player.play(stream.getStreamId(), it)
            }
        }
        //If stream is not live, get video-recording urls from recordings
        else if (isRecorded && stream.getRecordings().isNotEmpty()) {
            showRecordingVideoList(stream.getRecordings())
        }
    }


    private fun showRecordingVideoList(recordings: List<AmityRecordingData?>) {
        val streamId = StreamVideoPlayerIntent.getStreamId(intent)
        if (recordings.size > 1) {
            val builderSingle: AlertDialog.Builder = AlertDialog.Builder(this)
            builderSingle.setTitle("Select recordings")
            builderSingle.setCancelable(false)
            builderSingle.setNegativeButton("cancel") { dialog, _ -> dialog.dismiss(); finish() }

            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
            recordings.forEachIndexed { index, recordingData ->
                val number = index + 1;
                val text = "\n   $number\n"
                arrayAdapter.add(text)
            }

            builderSingle.setAdapter(arrayAdapter) { _, which ->
                val url = recordings[which]?.getUrl(AmityRecordingData.Format.FLV) ?: ""
                amity_video_player.play(streamId, url)
            }
            builderSingle.show()
        } else {
            amity_video_player.play(streamId, recordings[0]?.getUrl(AmityRecordingData.Format.FLV) ?: "")
        }
    }

    class URLErrorConsumer(val context: Context) : Consumer<Throwable> {
        override fun accept(t: Throwable?) {
            Toast.makeText(context, "Sorry, unable to play this stream", Toast.LENGTH_SHORT).show()
        }
    }
}