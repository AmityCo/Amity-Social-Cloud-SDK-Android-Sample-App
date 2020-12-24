package com.ekoapp.sdk.community

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.community.membership.EkoCommunityMembership
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.intent.ViewCommunityMembershipIntent
import com.ekoapp.sdk.intent.ViewCommunityMembershipsIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_community_membership_list.*
import kotlinx.android.synthetic.main.item_community_membership.*
import kotlinx.android.synthetic.main.item_community_membership.view.*

class CommunitySingleMembershipActivity : AppCompatActivity() {
    private val communityRepository by lazy { EkoClient.newCommunityRepository() }
    private val compositeDisposable = CompositeDisposable()
    private var communityId = ""
    private var userId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_community_membership)

        communityId = ViewCommunityMembershipsIntent.getCommunityId(intent)
        userId = ViewCommunityMembershipIntent.getUserId(intent)
        getCommunityMembership()
    }

    private fun getCommunityMembership() {
        val disposable = communityRepository
                .membership(communityId)
                .getCommunityMembership(userId)
                .doOnNext { communityMembership ->
                    community_membership_textview.text = StringBuilder()
                            .append(communityMembership.getUserId())
                            .append("\ndisplay name: ")
                            .append(communityMembership.getUser()?.getDisplayName())
                            .append("\nchannel id: ")
                            .append(communityMembership.getChannelId())
                            .append("\ncommunity id: ")
                            .append(communityMembership.getCommunityId())
                            .append("\nmembership: ")
                            .append(communityMembership.getCommunityMembership())
                            .append("\nis banned: ")
                            .append(communityMembership.isBanned())
                            .append("\nrole: ")
                            .append(communityMembership.getRoles())
                            .toString()

                    community_membership_textview.setOnLongClickListener {
                        onCommunityMembershipLongClick(communityMembership)
                        return@setOnLongClickListener true
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun onCommunityMembershipLongClick(communityMembership: EkoCommunityMembership) {
        val options = mutableListOf<String>()
        val isBanned = communityMembership.isBanned()
        if (!isBanned) {
            options.add("Ban User")
        } else {
            options.add("Unban User")
        }
        options.add("Add Role")
        options.add("Remove Role")
        options.add("Remove")

        MaterialDialog(this).show {
            listItems(items = options) { dialog, position, text ->
                when (text) {
                    "Ban User" -> {
                        compositeDisposable.add(
                                communityRepository
                                        .moderate(communityMembership.getCommunityId())
                                        .banUsers(listOf(communityMembership.getUserId()))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            showToast("Banned")
                                        }, {
                                            showToast(it.message ?: "Error ban user!")
                                        }))
                    }
                    "Unban User" -> {
                        compositeDisposable.add(
                                communityRepository
                                        .moderate(communityMembership.getCommunityId())
                                        .unbanUsers(listOf(communityMembership.getUserId()))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            showToast("Unbanned")
                                        }, {
                                            showToast(it.message ?: "Error Unbanned!")
                                        }))
                    }
                    "Add Role" -> {
                        showDialog(R.string.add_role, "", "", false) { _, input ->
                            if (input.isNotEmpty()) {
                                addRole(communityMembership.getCommunityId(), input, communityMembership.getUserId())
                            }
                        }
                    }
                    "Remove Role" -> {
                        showDialog(R.string.remove_role, "", "", false) { _, input ->
                            if (input.isNotEmpty()) {
                                removeRole(communityMembership.getCommunityId(), input, communityMembership.getUserId())
                            }
                        }
                    }
                    "Remove" -> {
                        compositeDisposable.add(communityRepository
                                .membership(communityMembership.getCommunityId())
                                .removeUsers(listOf(communityMembership.getUserId()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe())
                    }
                }
            }
        }
    }

    private fun addRole(communityId: String, input: CharSequence, userId: String) {
        compositeDisposable.add(EkoClient.newCommunityRepository().moderate(communityId)
                .addRole(role = input.toString(), userIds = listOf(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    showToast("Successfully! Add role")
                }
                .doOnError {
                    showToast("Failed! Add role")
                }
                .subscribe())
    }

    private fun removeRole(communityId: String, input: CharSequence, userId: String) {
        compositeDisposable.add(EkoClient.newCommunityRepository().moderate(communityId)
                .removeRole(role = input.toString(), userIds = listOf(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    showToast("Successfully! Remove role")
                }
                .doOnError {
                    showToast("Failed! Remove role")
                }
                .subscribe())
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
