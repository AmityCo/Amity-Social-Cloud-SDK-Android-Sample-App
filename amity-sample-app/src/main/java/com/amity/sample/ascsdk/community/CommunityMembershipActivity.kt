package com.amity.sample.ascsdk.community

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.intent.ViewCommunityMembershipsIntent
import com.amity.socialcloud.sdk.core.permission.AmityRoles
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunityMember
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_community_membership_list.*

class CommunityMembershipActivity : AppCompatActivity() {
    private val communityRepository by lazy { AmitySocialClient.newCommunityRepository() }
    private val compositeDisposable = CompositeDisposable()
    private val compositeDisposableMemberships = CompositeDisposable()
    private val compositeDisposableMembershipsByRole = CompositeDisposable()
    private val adapter = CommunityMembershipAdapter()
    private var communityId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_membership_list)
        initialListener()

        communityId = ViewCommunityMembershipsIntent.getCommunityId(intent)
        toolbar.apply {
            title = communityId
            subtitle = "member"
            setTitleTextColor(ContextCompat.getColor(this@CommunityMembershipActivity, android.R.color.white))
            setSubtitleTextColor(ContextCompat.getColor(this@CommunityMembershipActivity, android.R.color.white))
        }
        getCommunityMembership(communityId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add_user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_user -> {
                showDialog(R.string.add_user, "user id", "", false) { dialog, input ->
                    val userId = input.toString()
                    compositeDisposable.add(communityRepository
                            .membership(communityId)
                            .addMembers(listOf(userId))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe())
                }
                return true
            }
            R.id.action_role -> {
                val mockRole = "moderator"
                showDialog(R.string.role, "", mockRole, false) { _, input ->
                    getMembershipsByRole(communityId, input)
                }
                return true
            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialListener() {
        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        Consumer {
                            communityId = it.getCommunityId()
                            onCommunityMembershipLongClick(it)
                        }, Consumer {}
                )
    }

    private fun onCommunityMembershipLongClick(communityMember: AmityCommunityMember) {
        val options = mutableListOf<String>()
        val isBanned = communityMember.isBanned()
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
                                        .moderation(communityMember.getCommunityId())
                                        .banUsers(listOf(communityMember.getUserId()))
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
                                        .moderation(communityMember.getCommunityId())
                                        .unbanUsers(listOf(communityMember.getUserId()))
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
                                addRole(communityMember.getCommunityId(), input, communityMember.getUserId())
                            }
                        }
                    }
                    "Remove Role" -> {
                        showDialog(R.string.remove_role, "", "", false) { _, input ->
                            if (input.isNotEmpty()) {
                                removeRole(communityMember.getCommunityId(), input, communityMember.getUserId())
                            }
                        }
                    }
                    "Remove" -> {
                        compositeDisposable.add(communityRepository
                                .membership(communityMember.getCommunityId())
                                .removeMembers(listOf(communityMember.getUserId()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe())
                    }
                }
            }
        }
    }

    private fun getCommunityMembership(communityId: String) {
        community_membership_list_recyclerview.layoutManager = LinearLayoutManager(this)
        community_membership_list_recyclerview.adapter = adapter

        compositeDisposableMembershipsByRole.clear()
        val disposable = communityRepository.membership(communityId)
                .getMembers()
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { pagedList ->
                    adapter.submitList(pagedList)
                }
        compositeDisposableMemberships.add(disposable)
    }

    private fun getMembershipsByRole(communityId: String, input: CharSequence) {
        val roles = AmityRoles(input.toString().split(",").toSet())
        compositeDisposableMemberships.clear()
        val disposable = communityRepository
                .membership(communityId)
                .getMembers()
                .roles(roles)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(adapter::submitList)
                .doOnError(Throwable::printStackTrace)
                .subscribe()
        compositeDisposableMembershipsByRole.add(disposable)
    }

    private fun addRole(communityId: String, input: CharSequence, userId: String) {
        compositeDisposable.add(AmitySocialClient.newCommunityRepository().moderation(communityId)
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
        compositeDisposable.add(AmitySocialClient.newCommunityRepository().moderation(communityId)
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
        compositeDisposableMemberships.clear()
        compositeDisposableMembershipsByRole.clear()
        super.onDestroy()
    }
}
