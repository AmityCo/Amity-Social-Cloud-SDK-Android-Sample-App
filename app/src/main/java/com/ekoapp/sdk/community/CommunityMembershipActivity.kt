package com.ekoapp.sdk.community

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_community_membership_list.*

class CommunityMembershipActivity : AppCompatActivity() {
    private val communityRepository by lazy { EkoClient.newCommunityRepository() }
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
                            .addUsers(listOf(userId))
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
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        Consumer {
                            startActivity(ViewCommunityMembershipIntent(this, communityId, it.getUserId()))
                        }, Consumer {}
                )


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

    private fun getCommunityMembership(communityId: String) {
        community_membership_list_recyclerview.layoutManager = LinearLayoutManager(this)
        community_membership_list_recyclerview.adapter = adapter

        compositeDisposableMembershipsByRole.clear()
        val disposable = communityRepository.membership(communityId)
                .getCollection()
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
        compositeDisposableMemberships.clear()
        val disposable = communityRepository
                .membership(communityId)
                .getCollection()
                .roles(listOf(input.toString()))
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
        compositeDisposableMemberships.clear()
        compositeDisposableMembershipsByRole.clear()
        super.onDestroy()
    }
}
