package com.ekoapp.sdk.utils.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ekoapp.sdk.R
import kotlinx.android.synthetic.main.dialog_eko_inputs.*

class EkoInputsDialog : DialogFragment() {

    companion object {
        private const val FIRST_INPUT_HINT = "FIRST_INPUT_HINT"
        private const val SECOND_INPUT_HINT = "SECOND_INPUT_HINT"
        private const val TEXT_BUTTON_LEFT = "TEXT_BUTTON_LEFT"
        private const val TEXT_BUTTON_RIGHT = "TEXT_BUTTON_RIGHT"

        fun newInstance(firstInputHint: String?, secondInputHint: String?,
                        textBtnLeft: String?, textBtnRight: String?): EkoInputsDialog {
            val fragment = EkoInputsDialog()
            val args = Bundle()
            args.putString(FIRST_INPUT_HINT, firstInputHint)
            args.putString(SECOND_INPUT_HINT, secondInputHint)
            args.putString(TEXT_BUTTON_LEFT, textBtnLeft)
            args.putString(TEXT_BUTTON_RIGHT, textBtnRight)
            fragment.arguments = args
            return fragment
        }
    }

    private var firstInputHint: String? = null
    private var secondInputHint: String? = null
    private var textBtnLeft: String? = null
    private var textBtnRight: String? = null

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
        return inflater.inflate(R.layout.dialog_eko_inputs, container, false)
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
        if (firstInputHint.isNullOrEmpty()) {
            firstInputEditText.visibility = View.GONE
        } else {
            firstInputEditText.visibility = View.VISIBLE
            firstInputEditText.hint = firstInputHint
        }

        if (secondInputHint.isNullOrEmpty()) {
            secondInputEditText.visibility = View.GONE
        } else {
            secondInputEditText.visibility = View.VISIBLE
            secondInputEditText.hint = secondInputHint
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
            listener?.onRightClick(firstInputEditText.text.toString(), secondInputEditText.text.toString())
            dismissAllowingStateLoss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(FIRST_INPUT_HINT, firstInputHint)
        outState.putString(SECOND_INPUT_HINT, secondInputHint)
        outState.putString(TEXT_BUTTON_LEFT, textBtnLeft)
        outState.putString(TEXT_BUTTON_RIGHT, textBtnRight)
        super.onSaveInstanceState(outState)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val fragmentManager = manager.beginTransaction()
        fragmentManager.add(this, tag)
        fragmentManager.commitAllowingStateLoss()
    }

    private fun restoreArguments(bundle: Bundle?) {
        firstInputHint = bundle?.getString(FIRST_INPUT_HINT)
        secondInputHint = bundle?.getString(SECOND_INPUT_HINT)
        textBtnLeft = bundle?.getString(TEXT_BUTTON_LEFT)
        textBtnRight = bundle?.getString(TEXT_BUTTON_RIGHT)
    }

    private fun restoreInstanceState(bundle: Bundle?) {
        firstInputHint = bundle?.getString(FIRST_INPUT_HINT)
        secondInputHint = bundle?.getString(SECOND_INPUT_HINT)
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
        fun onRightClick(firstInput: String, secondInput: String)
    }

    open class Builder {
        private var firstInputHint: String? = null
        private var secondInputHint: String? = null
        private var textBtnLeft: String? = null
        private var textBtnRight: String? = null

        fun firstInputHint(firstInputHint: String): Builder {
            return apply { this.firstInputHint = firstInputHint }
        }

        fun secondInputHint(secondInputHint: String): Builder {
            return apply { this.secondInputHint = secondInputHint }
        }

        fun textBtnLeftDialog(textBtnLeft: String): Builder {
            return apply { this.textBtnLeft = textBtnLeft }
        }

        fun textBtnRightDialog(textBtnRight: String): Builder {
            return apply { this.textBtnRight = textBtnRight }
        }

        fun build() = newInstance(firstInputHint, secondInputHint, textBtnLeft, textBtnRight)
    }
}