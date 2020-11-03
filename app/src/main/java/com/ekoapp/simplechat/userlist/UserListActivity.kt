package com.ekoapp.simplechat.userlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.ekosdk.user.EkoUser
import com.ekoapp.ekosdk.user.query.EkoUserSortOption
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.simplechat.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_list.*
import java.util.*

class UserListActivity : AppCompatActivity() {

    private val userRepository = EkoClient.newUserRepository()
    private var keyword = ""
    private var sortBy: EkoUserSortOption = EkoUserSortOption.DISPLAYNAME
    private val adapter = UserListAdapter()
    private var compositeDisposable = CompositeDisposable()

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
            showDialog(R.string.search, "keyword", keyword, true) { _, input ->
                keyword = input.toString()
                observeUserCollection()
            }
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
        user_list_recyclerview.adapter = adapter
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ adapter.submitList(it) }, {})
        )
    }

    private fun getUsers(): Flowable<PagedList<EkoUser>> {
        return userRepository.getAllUsers()
                        .sortBy(sortBy)
                        .build()
                        .query()
    }

    @SuppressLint("CheckResult")
    private fun setUpListeners() {
        adapter.onClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, { })

        adapter.onLongClickFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                }, { })
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}
