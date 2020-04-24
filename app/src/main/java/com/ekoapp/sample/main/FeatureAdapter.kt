package com.ekoapp.sample.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ekoapp.sample.R
import kotlinx.android.synthetic.main.item_feature.view.*

class FeatureAdapter(val listener: FeatureItemListener) : RecyclerView.Adapter<FeatureAdapter.FeatureViewHolder>() {

    private val dataSet: List<String> = Feature.values().map { feature -> feature.featureName }

    class FeatureViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface FeatureItemListener {
        fun onClick(featureName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureAdapter.FeatureViewHolder {
        val featureItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_feature, parent, false)
        return FeatureViewHolder(featureItemView)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        val featureName = dataSet[position]
        holder.view.textview.text = featureName
        holder.view.setOnClickListener {
            listener.onClick(featureName)
        }
    }

    override fun getItemCount() = dataSet.size
}