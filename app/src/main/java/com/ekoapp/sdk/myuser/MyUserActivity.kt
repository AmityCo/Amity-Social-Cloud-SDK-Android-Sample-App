package com.ekoapp.sdk.myuser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.ekoapp.core.utils.getCurrentClassAndMethodNames
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.permission.EkoPermission
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.common.view.StringListAdapter
import com.ekoapp.sdk.intent.OpenChangeMetadataIntent
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
                showDialog(R.string.change_display_name, "", EkoClient.getDisplayName(), false, { dialog, input ->
                    val displayName = input.toString()
                    EkoClient
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
                })
            }
            metadata -> {
                val metadata = EkoClient.getUserMetadata() ?: "Metadata has not been set"
                val intent = OpenChangeMetadataIntent(this)
                val context = this
                MaterialDialog(this).show {
                    title(R.string.change_metadata)
                    message(null, metadata.toString(), null)
                    positiveButton(
                            null,
                            "Set",
                            {
                                context.startActivity(intent)
                            })
                }
            }
            avatar -> {
                startActivity(
                        Intent(this, MyAvatarUpdateActivity::class.java)
                )
            }
            permission -> {
                showDialog(R.string.check_permission, "", EkoPermission.EDIT_USER.value, false) { dialog, input ->
                    val inputStr = input.toString()
                    if (EkoPermission.values().any { it.value == inputStr }) {
                        val disposable = EkoClient
                                .hasPermission(EkoPermission.valueOf(inputStr))
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

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}