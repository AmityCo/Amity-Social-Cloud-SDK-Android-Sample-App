package com.amity.sample.ascsdk.userlist

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.OpenUserFollowerListIntent
import com.amity.sample.ascsdk.intent.OpenUserFollowingListIntent
import com.amity.socialcloud.sdk.AmityCoreClient
import com.amity.socialcloud.sdk.core.user.AmityFollowStatus
import com.amity.socialcloud.sdk.core.user.AmityUser
import com.amity.socialcloud.sdk.core.user.AmityUserFollowInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_user_follow_info.*

class UserFollowInfoDialog : DialogFragment() {

    companion object {
        private const val USER_KEY = "USER_KEY"

        fun newInstance(user: AmityUser): UserFollowInfoDialog {
            val fragment = UserFollowInfoDialog()
            val args = Bundle()
            args.putParcelable(USER_KEY, user)
            fragment.arguments = args
            return fragment
        }
    }

    private var user: AmityUser? = null
    private val userRepository = AmityCoreClient.newUserRepository()

    private var listener: OnDialogListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = getOnDialogListener()
        savedInstanceState?.let {
            restoreInstanceState(it)
        } ?: run {
            restoreArguments(arguments)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_user_follow_info, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var mDialog: Dialog? = null
        context?.let { c ->
            val frameLayout = FrameLayout(c)
            frameLayout.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            mDialog = Dialog(c).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setContentView(frameLayout)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
        return mDialog ?: dialog!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpListener()
    }

    private fun setUpView() {
        user?.getUserId()?.let { userId ->
            userRepository.relationship()
                    .user(userId)
                    .getFollowInfo()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ followInfo ->
                        followInfo?.let(::showFollowInfo)
                        followInfo?.getStatus()?.let { status ->
                            user?.getUserId()?.let { userId ->
                                showFollowStatus(userId, status)
                            }
                        }
                    }, {

                    })
        }
    }

    private fun showFollowInfo(userFollowInfo: AmityUserFollowInfo) {
        follower_count.text = String.format("Follower: %d", userFollowInfo.getFollowerCount())
        following_count.text = String.format("Following: %d", userFollowInfo.getFollowingCount())
    }

    private fun showFollowStatus(userId: String, status: AmityFollowStatus) {
        follow_status_button.text = String.format("Request Status: %s", status.apiKey)
        follow_relationship.text = when (status) {
            AmityFollowStatus.ACCEPTED -> {
                "Unfollow"
            }
            AmityFollowStatus.PENDING -> {
                "Cancel follow"
            }
            AmityFollowStatus.NONE -> {
                "Follow"
            }
        }

        follow_relationship?.setOnClickListener {
            when (status) {
                AmityFollowStatus.ACCEPTED -> {
                    userRepository.relationship()
                            .me()
                            .unfollow(userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Toast.makeText(context, "Unfollowed", Toast.LENGTH_LONG).show()
                                dismissAllowingStateLoss()
                            }, {
                                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                dismissAllowingStateLoss()
                            })
                }
                AmityFollowStatus.PENDING -> {
                    userRepository.relationship()
                            .me()
                            .unfollow(userId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Toast.makeText(context, "Follow cancelled", Toast.LENGTH_LONG).show()
                                dismissAllowingStateLoss()
                            }, {
                                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                dismissAllowingStateLoss()
                            })
                }
                AmityFollowStatus.NONE -> {
                    userRepository.relationship()
                            .user(userId)
                            .follow()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Toast.makeText(context, "Follow request sent", Toast.LENGTH_LONG).show()
                                dismissAllowingStateLoss()
                            }, {
                                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                                dismissAllowingStateLoss()
                            })
                }
            }
        }
    }

    private fun setUpListener() {
        following_button?.setOnClickListener {
            user?.getUserId()?.let { userId ->
                startActivity(OpenUserFollowingListIntent(requireContext(), userId))
            }
        }

        follower_button?.setOnClickListener {
            user?.getUserId()?.let { userId ->
                startActivity(OpenUserFollowerListIntent(requireContext(), userId))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(USER_KEY, user)
        super.onSaveInstanceState(outState)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val fragmentManager = manager.beginTransaction()
        fragmentManager.add(this, tag)
        fragmentManager.commitAllowingStateLoss()
    }

    private fun restoreArguments(bundle: Bundle?) {
        user = bundle?.getParcelable(USER_KEY)
    }

    private fun restoreInstanceState(bundle: Bundle?) {
        user = bundle?.getParcelable(USER_KEY)
    }

    private fun getOnDialogListener(): OnDialogListener? {
        val fragment = parentFragment
        try {
            return if (fragment != null) {
                fragment as OnDialogListener?
            } else {
                activity as OnDialogListener?
            }
        } catch (ignored: ClassCastException) {
        }
        return null
    }

    interface OnDialogListener {

    }
}