package com.ekoapp.simplechat.myuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.ekoapp.ekosdk.EkoClient
import com.ekoapp.sdk.common.extensions.showDialog
import com.ekoapp.simplechat.R
import com.ekoapp.simplechat.StringListAdapter
import com.ekoapp.simplechat.intent.OpenChangeMetadataIntent
import kotlinx.android.synthetic.main.activity_main.*

class MyUserActivity : AppCompatActivity() {

    companion object {
        const val displayname = "Displayname"
        const val metadata = "Metadata"
    }

    private val menuItems = listOf(displayname, metadata)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val adapter = StringListAdapter(menuItems, object : StringListAdapter.StringItemListener {
            override fun onClick(text: String) {
                onMenuItemSelected(text)
            }
        })
        menu_list.adapter = adapter
    }

    private fun onMenuItemSelected(item: String) {
        when (item) {
            displayname -> {
                showDialog(R.string.change_display_name, "", EkoClient.getDisplayName(), false) { dialog, input ->
                    val displayName = input.toString()
                    EkoClient.setDisplayName(displayName)
                            .subscribe()
                }
            }
            metadata -> {
                val metadata = EkoClient.getUserMetadata() ?: "Metadata has not been set"
                val intent = OpenChangeMetadataIntent(this)
                val context = this
                MaterialDialog(this).show {
                    title(R.string.change_metadata)
                    message(null, metadata.toString(), null)
                    positiveButton(null, "Set") {
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

}