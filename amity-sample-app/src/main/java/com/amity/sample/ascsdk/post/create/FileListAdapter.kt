package com.amity.sample.ascsdk.post.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amity.sample.ascsdk.R
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_file.view.*

class FileListAdapter(val fileItems: MutableList<FileViewItem>) : RecyclerView.Adapter<FileListAdapter.FileViewHolder>() {

    private val onLongClickSubject = PublishSubject.create<FileViewItem>()
    private val onClickSubject = PublishSubject.create<FileViewItem>()

    val onLongClickFlowable: Flowable<FileViewItem>
        get() = onLongClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    val onClickFlowable: Flowable<FileViewItem>
        get() = onClickSubject.toFlowable(BackpressureStrategy.BUFFER)

    override fun getItemCount(): Int {
        return fileItems.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = fileItems[position]
        holder.itemView.file_name_textview.text = item.fileName
        holder.itemView.progress_textview.text = item.progress

        holder.itemView.setOnClickListener {
            onClickSubject.onNext(item)
        }

        holder.itemView.setOnLongClickListener {
            onLongClickSubject.onNext(item)
            true
        }

    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
