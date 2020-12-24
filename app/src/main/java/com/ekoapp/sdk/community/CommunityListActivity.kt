package com.ekoapp.sdk.community

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.community.EkoCommunity
import com.ekoapp.ekosdk.community.query.EkoCommunityFilter
import com.ekoapp.ekosdk.community.query.EkoCommunitySortOption
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.common.extensions.showToast
import com.ekoapp.sdk.community.create.CreateCommunityActivity
import com.ekoapp.sdk.community.create.CreateCommunityByAdminActivity
import com.ekoapp.sdk.intent.OpenCommunityFeedIntent
import com.ekoapp.sdk.intent.OpenUpdateCommunityByAdminIntent
import com.ekoapp.sdk.intent.OpenUpdateCommunityIntent
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
    private val communityRepository by lazy { EkoClient.newCommunityRepository() }
    private val adapter = CommunityAdapter()
    private var checkedItem = 2
    private var disposableSearchCommunity: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_list)
        setUpCommunityRecycleView()
        setUpListeners()
        getCommunities("", "", EkoCommunitySortOption.values()[checkedItem], true)
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
            R.id.create_community_by_admin -> {
                startActivity(Intent(this, CreateCommunityByAdminActivity::class.java))
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
                        EkoCommunitySortOption.values()[checkedItem],
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
                        EkoCommunitySortOption.values()[checkedItem],
                        true)
            }
        })
    }

    private fun onCommunityClick(community: EkoCommunity) {
        startActivity(OpenCommunityFeedIntent(this, community))
    }

    private fun onCommunityLongClick(community: EkoCommunity) {

        val options = mutableListOf<String>()
        val isJoined = community.isJoined()
        if (!isJoined) {
            options.add("Join")
        } else {
            options.add("Leave")
        }
        options.add("Update")
        options.add("Update by admin")
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
                    "Update by admin" -> {
                        startActivity(OpenUpdateCommunityByAdminIntent(this@CommunityListActivity, community.getCommunityId()))
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

    private fun getCommunities(keyword: String, categoryId: String, sortBy: EkoCommunitySortOption, includeDeleted: Boolean) {
        setUpCommunityRecycleView()
        disposableSearchCommunity?.dispose()
        disposableSearchCommunity = communityRepository.getCommunityCollection()
                .withKeyword(keyword)
                .sortBy(sortBy)
                .filter(EkoCommunityFilter.ALL)
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
        val options = EkoCommunitySortOption.values().map { Pair(it.apiKey, false) }
        val choices = options.map { it.first }.toTypedArray()
        val checkedChoices = options.map { it.second }.toBooleanArray()
        checkedChoices[checkedItem] = true
        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.select_item)
                .setMultiChoiceItems(choices, checkedChoices) { dialog, which, isChecked ->
                    checkedItem = which
                    getCommunities(edittext_community_search.text.toString(),
                            edittext_category_ids.text.toString(),
                            EkoCommunitySortOption.values()[which],
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
                                EkoCommunitySortOption.values()[checkedItem],
                                true)
                    }
                    negativeButton(text = "NO") {
                        getCommunities(
                                "",
                                "",
                                EkoCommunitySortOption.values()[checkedItem],
                                false)
                    }
                }
    }

    private fun showCommunityString(community: EkoCommunity) {
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