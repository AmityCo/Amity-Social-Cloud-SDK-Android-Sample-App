package com.ekoapp.sdk.userlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.user.EkoUser
import com.ekoapp.ekosdk.user.query.EkoUserSortOption
import com.ekoapp.sdk.R
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.sdk.intent.OpenUserFeedIntent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_list.*
import java.util.*

class UserListActivity : AppCompatActivity() {

    private var users: LiveData<PagedList<EkoUser>>? = null

    private val userRepository = EkoClient.newUserRepository()

    private var keyword = ""
    private var sortBy: EkoUserSortOption = EkoUserSortOption.DISPLAYNAME
    private val adapter = UserListAdapter()

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.activity_user_list)
        observeUserCollection()
        setUpListeners()
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
                add("First created")
                add("Last created")
            }

            MaterialDialog(this).show {
                listItems(items = sortingOptions) { dialog, index, text ->
                    when (text.toString()) {
                        "Displayname" -> {
                            sortBy = EkoUserSortOption.DISPLAYNAME
                        }
                        "First created" -> {
                            sortBy = EkoUserSortOption.FIRST_CREATED
                        }
                        "Last created" -> {
                            sortBy = EkoUserSortOption.LAST_CREATED
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
        user_list_recyclerview.adapter = adapter

        users = getUsersLiveData()
        users?.observe(this, Observer<PagedList<EkoUser>> { adapter.submitList(it) })
    }

    private fun getUsersLiveData(): LiveData<PagedList<EkoUser>> {
        return LiveDataReactiveStreams.fromPublisher(
                userRepository.getAllUsers()
                        .sortBy(sortBy)
                        .build()
                        .query()
        )
    }

    private fun setUpListeners() {
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {
                    startActivity(OpenUserFeedIntent(this, it.getUserId()))
                }, Consumer { })

        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer {

                }, Consumer { })
    }

}


