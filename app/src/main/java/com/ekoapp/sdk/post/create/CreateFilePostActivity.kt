package com.ekoapp.sdk.post.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.feed.EkoPost
import com.ekoapp.ekosdk.file.EkoFile
import com.ekoapp.ekosdk.file.upload.EkoUploadResult
import com.ekoapp.ekosdk.internal.util.RealPathUtil
import com.ekoapp.sdk.R
import com.ekoapp.sdk.intent.IntentRequestCode
import com.ekoapp.sdk.intent.OpenCreateFilePostIntent
import com.ekoapp.sdk.intent.OpenCreateImagePostIntent
import com.ekoapp.sdk.utils.SnackBarUtil
import com.jakewharton.rxbinding3.view.clicks
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_create.*
import java.util.*

class CreateFilePostActivity : CreatePostActivity() {

    private val files = mutableListOf<EkoFile>()

    private val fileItems = mutableListOf<FileViewItem>()

    private val adapter = FileListAdapter(fileItems)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        file_recyclerview.adapter = adapter
        button_post.isEnabled = false

        val rxPermissions = RxPermissions(this)
        findViewById<View>(R.id.button_add).clicks()
                .compose(rxPermissions.ensure<Unit>(Manifest.permission.READ_EXTERNAL_STORAGE))
                .subscribe({ granted ->
                    if (granted) {
                        dispatchSearchFileIntent()
                    }
                })
    }

    override fun post() {
        createFilePost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    finish()
                }
                .doOnError {
                    SnackBarUtil(this).info("Failed to create post")
                }
                .subscribe()
    }

    private fun createFilePost(): Single<EkoPost> {
        when {
            OpenCreateFilePostIntent.getTargetType(intent) == "community" -> {
                return EkoClient.newFeedRepository()
                        .createPost()
                        .targetCommunity(OpenCreateFilePostIntent.getTargetId(intent))
                        .file(*files.toTypedArray())
                        .text(text.text.toString())
                        .build()
                        .post()
            }
            OpenCreateImagePostIntent.getTargetType(intent) == "myUser" -> {
                return EkoClient.newFeedRepository()
                        .createPost()
                        .targetMe()
                        .file(*files.toTypedArray())
                        .text(text.text.toString())
                        .build()
                        .post()
            }
            else -> {
                return EkoClient.newFeedRepository()
                        .createPost()
                        .targetUser(OpenCreateFilePostIntent.getTargetId(intent))
                        .file(*files.toTypedArray())
                        .text(text.text.toString())
                        .build()
                        .post()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IntentRequestCode.REQUEST_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            uploadFile(data)
        }
    }

    private fun dispatchSearchFileIntent() {
        if (getValidFileCount() == 10) {
            SnackBarUtil(this).info("Cannot upload more than 10 files")
            return
        }
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        startActivityForResult(intent, IntentRequestCode.REQUEST_SELECT_FILE)
    }

    private fun uploadFile(data: Intent?) {
        data?.data?.also { uri ->
            button_post.isEnabled = false
            val fileName = RealPathUtil.getFileName(this.contentResolver, uri)
            val uId = UUID.randomUUID().toString()
            val fileItem = FileViewItem(fileName, uId, "0 %")
            adapter.fileItems.add(fileItem)
            adapter.notifyDataSetChanged()
            EkoClient.newFileRepository().uploadFile(uri)
                    .build()
                    .transfer()
                    .subscribeOn(Schedulers.io())
                    .doOnNext {
                        when (it) {
                            is EkoUploadResult.COMPLETE -> {
                                files.add(it.getFile())
                                val index = getItemIndex(fileItem.uId)
                                adapter.fileItems[index].progress = "Uploaded"
                                adapter.notifyItemChanged(index)
                                button_post.isEnabled = shouldEnablePostButton()
                            }
                            is EkoUploadResult.PROGRESS -> {
                                val index = getItemIndex(fileItem.uId)
                                if (adapter.fileItems[index].progress != "Uploaded") {
                                    adapter.fileItems[index].progress = String.format("%s %%", it.getUploadInfo().getProgressPercentage())
                                    adapter.notifyItemChanged(index)
                                }
                            }
                            is EkoUploadResult.ERROR, EkoUploadResult.CANCELLED -> {
                                val index = getItemIndex(fileItem.uId)
                                adapter.fileItems[index].progress = "Failed"
                                adapter.notifyItemChanged(index)
                            }

                        }
                    }
                    .doOnError {
                        SnackBarUtil(this).info("Error: " + it.message)
                    }
                    .subscribe()
        }
    }

    private fun getItemIndex(uId: String): Int {
        for (item in fileItems) {
            if (item.uId == uId) {
                return fileItems.indexOf(item)
            }
        }
        return -1
    }

    private fun shouldEnablePostButton(): Boolean {
        var shouldEnable = false
        for (item in fileItems) {
            if (item.progress == "Uploaded") {
                shouldEnable = true
            }
            if (item.progress.contains('%')) {
                shouldEnable = false
                break
            }
        }
        return shouldEnable
    }

    private fun getValidFileCount(): Int {
        var count = 0
        for (item in fileItems) {
            if (item.progress != "Failed") {
                count++
            }
        }
        return count
    }
}