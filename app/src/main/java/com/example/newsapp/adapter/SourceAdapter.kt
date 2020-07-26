package com.example.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.news.data.SourceData
import com.example.newsapp.R
import com.example.newsapp.callbacks.SourceCallback

class SourceAdapter(
    private val sourceDataList: List<SourceData.SourcesList>,
    private val sourceCallbacks: SourceCallback
) :
    RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_source, parent, false)
        )
    }

    override fun getItemCount() = sourceDataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (sourceDataList.get(position).name != null)
            holder.sourceName.text = sourceDataList.get(position).name

        if (sourceDataList.get(position).url != null)
            holder.url.text = sourceDataList.get(position).url
        holder.cardView.setOnClickListener {
            sourceCallbacks.launchWebView(position)
        }

        if (sourceDataList.get(position).description != null)
            holder.description.text = sourceDataList.get(position).description.toString()

        if (sourceDataList.get(position).category != null) {
            val categoryText = sourceDataList.get(position).category
            holder.sourceCategory.text =
                categoryText!!.substring(0, 1).toUpperCase().plus(categoryText.substring(1))
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sourceName: TextView = itemView.findViewById(R.id.tv_source_name)
        val description: TextView = itemView.findViewById(R.id.tv_source_description)
        val sourceCategory: TextView = itemView.findViewById(R.id.tv_category_sor)
        val url: TextView = itemView.findViewById(R.id.tv_source_url)
        val cardView: CardView = itemView.findViewById(R.id.card_source)
    }
}