package com.amity.sample.ascsdk.myuser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.permission.AmityPermission
import com.ekoapp.core.utils.getCurrentClassAndMethodNames
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.common.view.StringListAdapter
import com.amity.sample.ascsdk.intent.OpenChangeMetadataIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MyUserActivity : AppCompatActivity() {

    companion object {
        const val displayname = "Displayname"
        const val metadata = "Metadata"
        const val avatar = "Avatar"
        const val permission = "Check Permission"
    }

    private val menuItems = listOf(displayname, metadata, avatar, permission)

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val adapter = StringListAdapter(menuItems, object : StringListAdapter.StringItemListener {
            override fun onClick(text: String) {
                onMenuItemSelected(text)
            }
        })
        menu_list.adapter = adapter
    }

    private fun onMenuItemSelected(item: String) {
        when (item) {
            displayname -> {
                AmityCoreClient.getCurrentUser()
                        .map { it.getDisplayName() ?: "" }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { showUpdateNameDialog(it) }
            }
            metadata -> {
                AmityCoreClient.getCurrentUser()
                        .map { it.getMetadata() ?: "Metadata has not been set" }
                        .map { it.toString() }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { showUpdateMetaDataDialog(it) }
            }
            avatar -> {
                startActivity(
                        Intent(this, MyAvatarUpdateActivity::class.java)
                )
            }
            permission -> {
                showDialog(R.string.check_permission, "", AmityPermission.EDIT_USER.value, false) { dialog, input ->
                    val inputStr = input.toString()
                    if (AmityPermission.values().any { it.value == inputStr }) {
                        val disposable = AmityCoreClient
                                .hasPermission(AmityPermission.valueOf(inputStr))
                                .atGlobal()
                                .check()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext {
                                    showToast("You have permission or not?: $it")
                                }
                                .doOnError { Timber.d("${getCurrentClassAndMethodNames()}${it.message}") }
                                .subscribe()
                        compositeDisposable.addAll(disposable)
                    } else {
                        showToast("Your permission is invalid")
                    }
                }
            }
        }
    }

    private fun showUpdateNameDialog(currentDisplayName: String) {
        showDialog(R.string.change_display_name, "", currentDisplayName, false) { dialog, input ->
            val displayName = input.toString()
            AmityCoreClient
                    .updateUser()
                    .displayName(displayName)
                    .build()
                    .update()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        finish()
                    }, {
                        showToast(message = "Update failed !")
                    })
        }
    }

    private fun showUpdateMetaDataDialog(metaJson: String) {
        val intent = OpenChangeMetadataIntent(this)
        val context = this
        MaterialDialog(this).show {
            title(R.string.change_metadata)
            message(null, metaJson, null)
            positiveButton(
                    null,
                    "Set"
            ) { context.startActivity(intent) }
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}