package com.ekoapp.sdk.stream

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.stream.EkoRecordingData
import com.ekoapp.ekosdk.stream.EkoStream
import com.ekoapp.ekosdk.stream.EkoWatcherData
import com.ekoapp.sdk.R
import com.ekoapp.sdk.intent.StreamVideoPlayerIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_stream_eko_video_player.*


open class StreamEkoVideoPlayerActivity : AppCompatActivity() {

    var urlDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream_eko_video_player)
        supportActionBar?.hide()
        getVideoURL()
    }

    override fun onDestroy() {
        super.onDestroy()
        eko_video_player?.stop()
        if (urlDisposable?.isDisposed == false) {
            urlDisposable?.dispose()
        }
    }

    override fun onResume() {
        super.onResume()
        eko_video_player?.resume()
    }

    override fun onPause() {
        super.onPause()
        eko_video_player?.pause()
    }

    private fun getVideoURL() {
        val streamId = StreamVideoPlayerIntent.getStreamId(intent)
        urlDisposable = EkoClient.newStreamRepository()
                .getStreamById(streamId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { startStreaming(it) }, URLErrorConsumer(this))
    }

    private fun startStreaming(stream: EkoStream) {
        val isLive = stream.getStatus() == EkoStream.Status.LIVE
        val isRecorded = stream.getStatus() == EkoStream.Status.RECORDED
        //If stream is still live, get live-stream url from watcherData
        if (isLive) {
            val url = stream.getWatcherData()?.getUrl(EkoWatcherData.Format.FLV)
            url?.let {
                eko_video_player.enableStopWhenPause()
                eko_video_player.play(it)
            }
        }
        //If stream is not live, get video-recording urls from recordings
        else if (isRecorded && stream.getRecordings().isNotEmpty()) {
            showRecordingVideoList(stream.getRecordings())
        }
    }


    private fun showRecordingVideoList(recordings: List<EkoRecordingData?>) {
        if (recordings.size > 1) {
            val builderSingle: AlertDialog.Builder = AlertDialog.Builder(this)
            builderSingle.setTitle("Select recordings")
            builderSingle.setCancelable(false)
            builderSingle.setNegativeButton("cancel") { dialog, _ -> dialog.dismiss(); finish() }

            val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item)
            recordings.forEachIndexed { index, ekoRecordingData ->
                val number = index + 1;
                val text = "\n   $number\n"
                arrayAdapter.add(text)
            }

            builderSingle.setAdapter(arrayAdapter) { _, which ->
                val url = recordings[which]?.getUrl(EkoRecordingData.Format.FLV) ?: ""
                eko_video_player.play(url)
            }
            builderSingle.show()
        } else {
            eko_video_player.play(recordings[0]?.getUrl(EkoRecordingData.Format.FLV) ?: "")
        }
    }

    class URLErrorConsumer(val context: Context) : Consumer<Throwable> {
        override fun accept(t: Throwable?) {
            Toast.makeText(context, "Sorry, unable to play this stream", Toast.LENGTH_SHORT).show()
        }
    }
}