package com.ekoapp.simplechat.file

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.ekoapp.ekosdk.internal.api.http.EkoOkHttp
import com.ekoapp.ekosdk.message.EkoMessage
import com.ekoapp.simplechat.SampleApp
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Func
import com.tonyodev.fetch2okhttp.OkHttpDownloader
import timber.log.Timber
import java.io.File

class FileManager {
    companion object {
        fun openFile(context: Context, fileMessage: EkoMessage.Data.FILE) {
            val url = fileMessage.getUrl()
            val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
            val fileName = fileMessage.getFileName() ?: ""
            val filePath = "$dirPath/$fileName"

            val client = EkoOkHttp.newBuilder().build()

            val fetchConfiguration = FetchConfiguration.Builder(SampleApp.get())
                    .setDownloadConcurrentLimit(10)
                    .enableLogging(true)
                    .setHttpDownloader(OkHttpDownloader(client))
                    .build()

            val request = Request(url, filePath)
            request.priority = Priority.HIGH
            request.networkType = NetworkType.ALL

            val fetch = Fetch.getInstance(fetchConfiguration)
            fetch.addListener(object : FetchListener {
                override fun onAdded(download: Download) {

                }

                override fun onQueued(download: Download, b: Boolean) {

                }

                override fun onWaitingNetwork(download: Download) {

                }

                override fun onCompleted(download: Download) {
                    if (download.status == Status.COMPLETED) {
                        val uri = FileProvider.getUriForFile(context,
                                context.packageName,
                                File(request.file))
                        val viewIntent = Intent(Intent.ACTION_VIEW)
                        viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        viewIntent.data = uri
                        val activities: List<ResolveInfo> = context.packageManager.queryIntentActivities(viewIntent, 0)
                        if (activities.isNotEmpty()) {
                            val chooserIntent = Intent.createChooser(viewIntent, "Choose an application to open with:")
                            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(chooserIntent)
                        } else {
                            Toast.makeText(context, "No app to open this file", Toast.LENGTH_LONG).show()
                        }
                    }

                    if (!fetch.isClosed) {
                        fetch.close()
                    }
                }

                override fun onError(download: Download, error: Error, throwable: Throwable?) {
                    Timber.e(throwable, "download fail")
                    if (!fetch.isClosed) {
                        fetch.close()
                    }
                }

                override fun onDownloadBlockUpdated(download: Download, downloadBlock: DownloadBlock, i: Int) {

                }

                override fun onStarted(download: Download, list: List<DownloadBlock>, i: Int) {

                }

                override fun onProgress(download: Download, l: Long, l1: Long) {

                }

                override fun onPaused(download: Download) {

                }

                override fun onResumed(download: Download) {

                }

                override fun onCancelled(download: Download) {

                }

                override fun onRemoved(download: Download) {

                }

                override fun onDeleted(download: Download) {

                }
            })

            fetch.enqueue(request, Func {
                //Request was successfully enqueued for download.
            }, Func { error ->
                //An error occurred enqueuing the request.
                Timber.e(error.throwable, "enqueue fail")
            })

        }
    }
}