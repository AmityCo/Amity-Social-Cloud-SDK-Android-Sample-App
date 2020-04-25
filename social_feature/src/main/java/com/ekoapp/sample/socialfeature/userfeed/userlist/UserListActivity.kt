package com.ekoapp.sample.socialfeature.userfeed.userlist

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.EkoUser
import com.ekoapp.ekosdk.EkoUserSortOption
import com.ekoapp.ekosdk.sdk.BuildConfig
import com.ekoapp.sample.core.ui.BaseActivity
import com.ekoapp.sample.socialfeature.R
import com.ekoapp.sample.socialfeature.userfeed.post.UserFeedActivity
import kotlinx.android.synthetic.main.activity_user_list.*
import java.util.*

class UserListActivity : BaseActivity() {

    private var users: LiveData<PagedList<EkoUser>>? = null

    private val userRepository = EkoClient.newUserRepository()

    private var keyword = ""
    private var sortBy: EkoUserSortOption = EkoUserSortOption.DISPLAYNAME

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_list)
        toolbar.title = getString(R.string.toolbar_user_list)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white))
        setSupportActionBar(toolbar)
        observeUserCollection()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_user_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_search) {
            showDialog(R.string.search, "keyword", keyword, true, { dialog, input ->
                keyword = input.toString()
                observeUserCollection()
            })
            return true
        } else if (id == R.id.action_sort) {
            val sortingOptions = ArrayList<String>()
            sortingOptions.run {
                add("Displayname")
                add("Stack from top")
                add("Stack from end")
            }

            MaterialDialog(this).show {
                listItems(items = sortingOptions) { dialog, index, text ->
                    when (text.toString()) {
                        "Displayname" -> {
                            sortBy = EkoUserSortOption.DISPLAYNAME
                        }
                        "Stack from top" -> {
                            sortBy = EkoUserSortOption.STACK_FROM_TOP
                        }
                        "Stack from end" -> {
                            sortBy = EkoUserSortOption.STACK_FROM_END
                        }
                    }
                    observeUserCollection()
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeUserCollection() {
        users?.removeObservers(this)
        val listener = object : UserListAdapter.UserItemListener {
            override fun onClick(userId: String) {
                startActivity(Intent(this@UserListActivity, UserFeedActivity::class.java))
            }
        }
        val adapter = UserListAdapter(listener)
        user_list_recyclerview.adapter = adapter

        users = getUsersLiveData()
        users?.observe(this, Observer<PagedList<EkoUser>> { adapter.submitList(it) })
    }

    private fun getUsersLiveData(): LiveData<PagedList<EkoUser>> {
        return userRepository.searchUserByDisplayName(keyword, sortBy)
    }

    private fun showDialog(@StringRes title: Int, hint: CharSequence, prefill: CharSequence, allowEmptyInput: Boolean, callback: InputCallback) {
        MaterialDialog(this).show {
            title(title)
            input(inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS, hint = hint.toString(), prefill = prefill, allowEmpty = allowEmptyInput, callback = callback)
        }
    }

}