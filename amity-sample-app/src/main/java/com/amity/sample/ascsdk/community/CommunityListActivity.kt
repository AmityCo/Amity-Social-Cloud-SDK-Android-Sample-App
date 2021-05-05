package com.amity.sample.ascsdk.community

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.common.extensions.showDialog
import com.amity.sample.ascsdk.common.extensions.showToast
import com.amity.sample.ascsdk.community.create.CreateCommunityActivity
import com.amity.sample.ascsdk.intent.OpenCommunityFeedIntent
import com.amity.sample.ascsdk.intent.OpenUpdateCommunityIntent
import com.amity.socialcloud.sdk.social.AmitySocialClient
import com.amity.socialcloud.sdk.social.community.AmityCommunity
import com.amity.socialcloud.sdk.social.community.AmityCommunityFilter
import com.amity.socialcloud.sdk.social.community.AmityCommunitySortOption
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_community_list.*

class CommunityListActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()
    private val communityRepository by lazy { AmitySocialClient.newCommunityRepository() }
    private val adapter = CommunityAdapter()
    private var checkedItem = 2
    private var disposableSearchCommunity: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_list)
        setUpCommunityRecycleView()
        setUpListeners()
        getCommunities("", "", AmityCommunitySortOption.values()[checkedItem], true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_community_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_community -> {
                startActivity(Intent(this, CreateCommunityActivity::class.java))
                return true
            }
            R.id.get_community -> {
                getCommunity()
                return true
            }
            R.id.sort_community -> {
                sortCommunities()
            }
            R.id.include_deleted_community -> {
                includeDeletedCommunities(
                        keyword = edittext_community_search.text.toString(),
                        categoryId = edittext_category_ids.text.toString())
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpCommunityRecycleView() {
        community_list_recyclerview.adapter = adapter
    }

    private fun setUpListeners() {
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        Consumer {
                            onCommunityClick(it)
                        }, Consumer {}
                )


        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        Consumer {
                            onCommunityLongClick(it)
                        }, Consumer {}
                )

        edittext_community_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                getCommunities(
                        text.toString(),
                        edittext_category_ids.text.toString(),
                        AmityCommunitySortOption.values()[checkedItem],
                        true
                )
            }
        })

        edittext_category_ids.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                getCommunities(
                        edittext_community_search.text.toString(),
                        text.toString(),
                        AmityCommunitySortOption.values()[checkedItem],
                        true)
            }
        })
    }

    private fun onCommunityClick(community: AmityCommunity) {
        startActivity(OpenCommunityFeedIntent(this, community))
    }

    private fun onCommunityLongClick(community: AmityCommunity) {

        val options = mutableListOf<String>()
        val isJoined = community.isJoined()
        if (!isJoined) {
            options.add("Join")
        } else {
            options.add("Leave")
        }
        options.add("Update")
        options.add("Delete")

        MaterialDialog(this).show {
            listItems(items = options) { dialog, position, text ->
                when (text) {
                    "Join" -> {
                        compositeDisposable.add(
                                communityRepository
                                        .joinCommunity(community.getCommunityId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(Action { }, Consumer { }))
                    }
                    "Leave" -> {
                        compositeDisposable.add(
                                communityRepository
                                        .leaveCommunity(community.getCommunityId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ }, { }))
                    }
                    "Update" -> {
                        startActivity(OpenUpdateCommunityIntent(this@CommunityListActivity, community.getCommunityId()))
                    }
                    "Delete" -> {
                        compositeDisposable.add(
                                communityRepository
                                        .deleteCommunity(community.getCommunityId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({ }, { throwable ->
                                            throwable.message?.let { message ->
                                                showToast(message)
                                            }
                                        }))
                    }
                }
            }
        }

    }

    private fun getCommunity() {
        showDialog(R.string.get_community, "community id", "", false) { _, input ->
            val disposable = communityRepository
                    .getCommunity(input.toString())
                    .firstOrError()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        showCommunityString(it)
                    }, {
                        showToast(message = "Get failed !")
                    })
            compositeDisposable.add(disposable)
        }
    }

    private fun getCommunities(keyword: String, categoryId: String, sortBy: AmityCommunitySortOption, includeDeleted: Boolean) {
        setUpCommunityRecycleView()
        disposableSearchCommunity?.dispose()
        disposableSearchCommunity = communityRepository.getCommunities()
                .withKeyword(keyword)
                .sortBy(sortBy)
                .filter(AmityCommunityFilter.ALL)
                .categoryId(categoryId)
                .includeDeleted(includeDeleted)
                .build()
                .query()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { pagedList ->
                    adapter.submitList(pagedList)
                }
    }

    private fun sortCommunities() {
        val options = AmityCommunitySortOption.values().map { Pair(it.apiKey, false) }
        val choices = options.map { it.first }.toTypedArray()
        val checkedChoices = options.map { it.second }.toBooleanArray()
        checkedChoices[checkedItem] = true
        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.select_item)
                .setMultiChoiceItems(choices, checkedChoices) { dialog, which, isChecked ->
                    checkedItem = which
                    getCommunities(edittext_community_search.text.toString(),
                            edittext_category_ids.text.toString(),
                            AmityCommunitySortOption.values()[which],
                            true)
                    dialog.dismiss()
                }
                .show()
    }

    private fun includeDeletedCommunities(keyword: String, categoryId: String) {
        MaterialDialog(this)
                .title(R.string.include_deleted)
                .message(R.string.include_deleted_message)
                .show {
                    positiveButton(text = "YES") {
                        getCommunities(
                                "",
                                "",
                                AmityCommunitySortOption.values()[checkedItem],
                                true)
                    }
                    negativeButton(text = "NO") {
                        getCommunities(
                                "",
                                "",
                                AmityCommunitySortOption.values()[checkedItem],
                                false)
                    }
                }
    }

    private fun showCommunityString(community: AmityCommunity) {
        val communityString = "id: ${community.getCommunityId()} \n" +
                "display name: ${community.getDisplayName()} \n" +
                "description: ${community.getDescription()} \n" +
                "posts count: ${community.getPostCount()} \n" +
                "members count: ${community.getMemberCount()} \n" +
                "public: ${community.isPublic()} \n" +
                "only admin can post: ${community.onlyAdminCanPost()} \n" +
                "official: ${community.isOfficial()} \n" +
                "joined: ${community.isJoined()} \n" +
                "isDeleted: ${community.isDeleted()}"

        MaterialDialog(this).show {
            message(text = communityString)
        }

    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}