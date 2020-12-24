package com.ekoapp.sdk.utils.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import com.ekoapp.sdk.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_bottom_sheet_dialog.*
import kotlin.properties.Delegates


const val EXTRA_PARAM_NAV_MENU = "nav_menu"

class EkoBottomSheetDialogFragment private constructor() : BottomSheetDialogFragment() {
    private var navListener: OnNavigationItemSelectedListener? = null
    var menu by Delegates.notNull<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        menu = requireArguments().getInt(EXTRA_PARAM_NAV_MENU)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bottom_sheet_dialog, container, false)
    }

    fun setOnNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener) {
        this.navListener = listener
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationView.menu.clear();
        navigationView.inflateMenu(menu)
        navigationView.setNavigationItemSelectedListener { item ->
            navListener?.onItemSelected(item)
            dismiss()
            true
        }

    }


    companion object {
        fun newInstance(@MenuRes menu: Int): EkoBottomSheetDialogFragment {
            val args = Bundle()

            val fragment = EkoBottomSheetDialogFragment()
            args.putInt(EXTRA_PARAM_NAV_MENU, menu)
            fragment.arguments = args
            return fragment
        }
    }

    interface OnNavigationItemSelectedListener {
        fun onItemSelected(item: MenuItem)
    }
}