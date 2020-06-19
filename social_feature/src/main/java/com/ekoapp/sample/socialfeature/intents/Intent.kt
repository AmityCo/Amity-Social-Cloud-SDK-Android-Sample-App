package com.ekoapp.sample.socialfeature.intents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ekoapp.sample.socialfeature.constants.*
import com.ekoapp.sample.socialfeature.createfeeds.CreateFeedsActivity
import com.ekoapp.sample.socialfeature.editfeeds.EditFeedsActivity
import com.ekoapp.sample.socialfeature.editfeeds.data.EditUserFeedsData
import com.ekoapp.sample.socialfeature.reactions.data.UserReactionData
import com.ekoapp.sample.socialfeature.reactions.view.ReactionsSummaryFeedsActivity
import com.ekoapp.sample.socialfeature.search.SearchUsersActivity
import com.ekoapp.sample.socialfeature.userfeeds.data.FeedsData
import com.ekoapp.sample.socialfeature.userfeeds.view.BodyUserFeedsActivity
import com.ekoapp.sample.socialfeature.userfeeds.view.UserFeedsActivity
import com.ekoapp.sample.socialfeature.users.data.UserData
import com.ekoapp.sample.socialfeature.users.view.SeeAllUsersActivity

fun Fragment.openCreateFeedsPage(data: UserData?) {
    val intent = Intent(requireContext(), CreateFeedsActivity::class.java)
    intent.putExtra(EXTRA_USER_DATA, data)
    startActivityForResult(intent, REQUEST_CODE_CREATE_FEEDS)
}

fun Fragment.openEditFeedsPage(data: EditUserFeedsData?) {
    val intent = Intent(requireContext(), EditFeedsActivity::class.java)
    intent.putExtra(EXTRA_EDIT_FEEDS, data)
    startActivityForResult(intent, REQUEST_CODE_EDIT_FEEDS)
}

fun AppCompatActivity.openEditFeedsPage(data: EditUserFeedsData?) {
    val intent = Intent(this, EditFeedsActivity::class.java)
    intent.putExtra(EXTRA_EDIT_FEEDS, data)
    startActivityForResult(intent, REQUEST_CODE_EDIT_FEEDS)
}

fun Fragment.openSearchUsersPage() {
    startActivity(Intent(requireContext(), SearchUsersActivity::class.java))
}

fun Fragment.openSeeAllUsersPage(data: UserData) {
    val intent = Intent(requireContext(), SeeAllUsersActivity::class.java)
    intent.putExtra(EXTRA_USER_DATA, data)
    startActivityForResult(intent, REQUEST_CODE_SEE_ALL_USERS)
}

fun Fragment.openReactionsSummaryFeedsPage(data: UserReactionData?) {
    val intent = Intent(requireContext(), ReactionsSummaryFeedsActivity::class.java)
    intent.putExtra(EXTRA_USER_REACTION_DATA, data)
    startActivityForResult(intent, REQUEST_CODE_REACTIONS_SUMMARY)
}

fun AppCompatActivity.openReactionsSummaryFeedsPage(data: UserReactionData?) {
    val intent = Intent(this, ReactionsSummaryFeedsActivity::class.java)
    intent.putExtra(EXTRA_USER_REACTION_DATA, data)
    startActivityForResult(intent, REQUEST_CODE_REACTIONS_SUMMARY)
}

fun Fragment.openUserFeedsPage(data: UserData?) {
    val intent = Intent(requireContext(), UserFeedsActivity::class.java)
    intent.putExtra(EXTRA_USER_DATA, data)
    startActivityForResult(intent, REQUEST_CODE_USER_FEEDS)
}

fun AppCompatActivity.openUserFeedsPage(data: UserData?) {
    val intent = Intent(this, UserFeedsActivity::class.java)
    intent.putExtra(EXTRA_USER_DATA, data)
    startActivityForResult(intent, REQUEST_CODE_USER_FEEDS)
}

fun Fragment.openFeedsById(data: FeedsData?) {
    val intent = Intent(requireContext(), BodyUserFeedsActivity::class.java)
    intent.putExtra(EXTRA_EKO_POST, data)
    startActivityForResult(intent, REQUEST_CODE_USER_FEEDS)
}