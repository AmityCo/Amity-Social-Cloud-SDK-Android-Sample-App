package com.amity.sample.ascsdk.channellist.dialog

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.amity.sample.ascsdk.R
import com.amity.sample.ascsdk.intent.IntentRequestCode
import com.amity.sample.ascsdk.utils.dialog.AmityBottomSheetDialogFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_channel_inputs.*
import kotlinx.android.synthetic.main.dialog_amity_inputs.channelIdEditText
import kotlinx.android.synthetic.main.dialog_amity_inputs.leftButton
import kotlinx.android.synthetic.main.dialog_amity_inputs.rightButton
import kotlinx.android.synthetic.main.dialog_amity_inputs.displayNameInputEditText
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChannelInputsDialog : DialogFragment() {

    companion object {
        private const val CHANNEL_ID_HINT = "CHANNEL_ID_HINT"
        private const val DISPLAY_NAME_HINT = "DISPLAY_NAME_HINT"
        private const val USER_ID = "USER_ID"
        private const val TEXT_BUTTON_LEFT = "TEXT_BUTTON_LEFT"
        private const val TEXT_BUTTON_RIGHT = "TEXT_BUTTON_RIGHT"

        fun newInstance(channelId: String? = null, displayName: String? = null,
                        userId: String? = null, textBtnLeft: String? = null,
                        textBtnRight: String? = null): ChannelInputsDialog {
            val fragment = ChannelInputsDialog()
            val args = Bundle()
            args.putString(CHANNEL_ID_HINT, channelId)
            args.putString(DISPLAY_NAME_HINT, displayName)
            args.putString(USER_ID, userId)
            args.putString(TEXT_BUTTON_LEFT, textBtnLeft)
            args.putString(TEXT_BUTTON_RIGHT, textBtnRight)
            fragment.arguments = args
            return fragment
        }
    }

    private var channelId: String? = null
    private var displayName: String? = null
    private var userId: String? = null
    private var textBtnLeft: String? = null
    private var textBtnRight: String? = null

    private var photoUri: Uri? = null
    private var photoFile: File? = null

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
        return inflater.inflate(R.layout.dialog_channel_inputs, container, false)
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
        if (channelId.isNullOrEmpty()) {
            channelIdEditText.visibility = View.GONE
        } else {
            channelIdEditText.visibility = View.VISIBLE
            channelIdEditText.hint = channelId
        }

        if (displayName.isNullOrEmpty()) {
            displayNameInputEditText.visibility = View.GONE
        } else {
            displayNameInputEditText.visibility = View.VISIBLE
            displayNameInputEditText.hint = displayName
        }

        if (userId.isNullOrEmpty()) {
            userIdEditText.visibility = View.GONE
        } else {
            userIdEditText.visibility = View.VISIBLE
            userIdEditText.hint = userId
        }


        if (textBtnLeft.isNullOrBlank()) {
            leftButton.visibility = View.GONE
        } else {
            leftButton.visibility = View.VISIBLE
            leftButton.text = textBtnLeft
        }

        if (textBtnRight.isNullOrBlank()) {
            rightButton.visibility = View.GONE
        } else {
            rightButton.visibility = View.VISIBLE
            rightButton.text = textBtnRight
        }
    }

    private fun setUpListener() {
        leftButton.setOnClickListener {
            listener?.onLeftClick()
            dismissAllowingStateLoss()
        }
        rightButton.setOnClickListener {
            listener?.onRightClick(channelIdEditText.text.toString(),
                    displayNameInputEditText.text.toString(), userIdEditText.text.toString(), photoUri)
            dismissAllowingStateLoss()
        }

        avatarView.setOnClickListener {
            showOptionTakePhoto()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CHANNEL_ID_HINT, channelId)
        outState.putString(DISPLAY_NAME_HINT, displayName)
        outState.putString(USER_ID, userId)
        outState.putString(TEXT_BUTTON_LEFT, textBtnLeft)
        outState.putString(TEXT_BUTTON_RIGHT, textBtnRight)
        super.onSaveInstanceState(outState)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val fragmentManager = manager.beginTransaction()
        fragmentManager.add(this, tag)
        fragmentManager.commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IntentRequestCode.REQUEST_SELECT_PHOTO) {
                data?.data?.also { uri ->
                    photoUri = uri
                    Glide.with(this)
                            .load(photoUri)
                            .centerCrop()
                            .into(ivAvatar)
                }
            }
        }
    }

    private fun restoreArguments(bundle: Bundle?) {
        channelId = bundle?.getString(CHANNEL_ID_HINT)
        displayName = bundle?.getString(DISPLAY_NAME_HINT)
        userId = bundle?.getString(USER_ID)
        textBtnLeft = bundle?.getString(TEXT_BUTTON_LEFT)
        textBtnRight = bundle?.getString(TEXT_BUTTON_RIGHT)
    }

    private fun restoreInstanceState(bundle: Bundle?) {
        channelId = bundle?.getString(CHANNEL_ID_HINT)
        displayName = bundle?.getString(DISPLAY_NAME_HINT)
        userId = bundle?.getString(USER_ID)
        textBtnLeft = bundle?.getString(TEXT_BUTTON_LEFT)
        textBtnRight = bundle?.getString(TEXT_BUTTON_RIGHT)
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
        fun onLeftClick()
        fun onRightClick(channelId: String, displayName: String, userId: String, photoFileUri: Uri?)
    }

    open class Builder {
        private var channelIdHint: String? = null
        private var displayNameHint: String? = null
        private var userIdHint: String? = null
        private var textBtnLeft: String? = null
        private var textBtnRight: String? = null

        fun channelIdHint(channelIdHint: String): Builder {
            return apply { this.channelIdHint = channelIdHint }
        }

        fun displayNameHint(displayNameHint: String): Builder {
            return apply { this.displayNameHint = displayNameHint }
        }

        fun userIdHint(userIdHint: String): Builder {
            return apply { this.userIdHint = userIdHint }
        }

        fun textBtnLeftDialog(textBtnLeft: String): Builder {
            return apply { this.textBtnLeft = textBtnLeft }
        }

        fun textBtnRightDialog(textBtnRight: String): Builder {
            return apply { this.textBtnRight = textBtnRight }
        }

        fun build() = newInstance(channelIdHint, displayNameHint, userIdHint, textBtnLeft, textBtnRight)
    }

    private fun showOptionTakePhoto() {
        val fragment =
                AmityBottomSheetDialogFragment.newInstance(R.menu.amity_upload_profile_picture)

        fragment.show(childFragmentManager, AmityBottomSheetDialogFragment.toString())
        fragment.setOnNavigationItemSelectedListener(object :
                AmityBottomSheetDialogFragment.OnNavigationItemSelectedListener {
            override fun onItemSelected(item: MenuItem) {
                handleUploadPhotoOption(item)
            }
        })
    }

    private fun handleUploadPhotoOption(item: MenuItem) {
        if (item.itemId == R.id.actionTakePicture) {
            takePicture()
        } else if (item.itemId == R.id.actionPickPicture) {
            pickImage()
        }
    }

    private fun pickImage() {
        val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = false
            permissions.entries.forEach {
                permissionGranted = it.value
            }
            if (permissionGranted) {
                dispatchSearchFileIntent()
            }
        }
        cameraPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
    }

    private fun dispatchSearchFileIntent() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, IntentRequestCode.REQUEST_SELECT_PHOTO)
    }

    private fun takePicture() {
        val cameraPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var permissionGranted = false
            permissions.entries.forEach {
                permissionGranted = it.value
            }
            if (permissionGranted) {
                val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
                    if (it) {
                        setProfilePicture(photoFile)
                    }
                }
                createPhotoUri()
                takePhoto.launch(photoUri)
            }
        }

        cameraPermission.launch(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    private fun createPhotoUri() {
        photoFile = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }

        photoFile?.also {
            photoUri = FileProvider.getUriForFile(requireContext(),
                    requireContext().packageName,
                    it)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        )
    }

    private fun setProfilePicture(file: File?) {
        Glide.with(requireContext())
                .load(Uri.fromFile(file))
                .placeholder(R.drawable.ic_default_profile_64)
                .centerCrop()
                .into(ivAvatar)
    }
}