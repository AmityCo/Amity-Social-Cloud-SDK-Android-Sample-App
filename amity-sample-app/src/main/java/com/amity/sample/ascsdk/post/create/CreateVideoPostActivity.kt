package com.amity.sample.ascsdk.post.create

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.AmityFile
import com.amity.socialcloud.sdk.core.file.AmityUploadResult
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.feed.AmityPost
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.IntentRequestCode
import com.amity.sample.ascsdk.intent.OpenCreateFilePostIntent
import com.amity.sample.ascsdk.intent.OpenCreateImagePostIntent
import com.amity.sample.ascsdk.utils.RealPathUtil
import com.amity.sample.ascsdk.utils.SnackBarUtil
import com.amity.socialcloud.sdk.core.AmityVideo
import com.jakewharton.rxbinding3.view.clicks
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post_create.*
import java.util.*

class CreateVideoPostActivity : CreatePostActivity() {

    private val videos = mutableListOf<AmityVideo>()

    private val videoItems = mutableListOf<FileViewItem>()

    private val adapter = FileListAdapter(videoItems)

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

    private fun createFilePost(): Single<AmityPost> {
        when {
            OpenCreateFilePostIntent.getTargetType(intent) == "community" -> {
                return AmitySocialClient.newPostRepository()
                        .createPost()
                        .targetCommunity(OpenCreateFilePostIntent.getTargetId(intent))
                        .video(*videos.toTypedArray())
                        .text(text.text.toString())
                        .build()
                        .post()
            }
            OpenCreateImagePostIntent.getTargetType(intent) == "myUser" -> {
                return AmitySocialClient.newPostRepository()
                        .createPost()
                        .targetMe()
                        .video(*videos.toTypedArray())
                        .text(text.text.toString())
                        .build()
                        .post()
            }
            else -> {
                return AmitySocialClient.newPostRepository()
                        .createPost()
                        .targetUser(OpenCreateFilePostIntent.getTargetId(intent))
                        .video(*videos.toTypedArray())
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
        if (getValidVideoCount() == 4) {
            SnackBarUtil(this).info("Cannot upload more than 4 videos")
            return
        }
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "video/*"
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
            AmityCoreClient.newFileRepository()
                    .uploadVideo(uri)
                    .build()
                    .transfer()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext {
                        when (it) {
                            is AmityUploadResult.COMPLETE -> {
                                videos.add(it.getFile())
                                val index = getItemIndex(fileItem.uId)
                                adapter.fileItems[index].progress = "Uploaded"
                                adapter.notifyItemChanged(index)
                                button_post.isEnabled = shouldEnablePostButton()
                            }
                            is AmityUploadResult.PROGRESS -> {
                                val index = getItemIndex(fileItem.uId)
                                if (adapter.fileItems[index].progress != "Uploaded") {
                                    adapter.fileItems[index].progress = String.format("%s %%", it.getUploadInfo().getProgressPercentage())
                                    adapter.notifyItemChanged(index)
                                }
                            }
                            is AmityUploadResult.ERROR, AmityUploadResult.CANCELLED -> {
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
        for (item in videoItems) {
            if (item.uId == uId) {
                return videoItems.indexOf(item)
            }
        }
        return -1
    }

    private fun shouldEnablePostButton(): Boolean {
        var shouldEnable = false
        for (item in videoItems) {
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

    private fun getValidVideoCount(): Int {
        var count = 0
        for (item in videoItems) {
            if (item.progress != "Failed") {
                count++
            }
        }
        return count
    }
}