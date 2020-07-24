package com.example.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.news.data.Article
import com.example.newsapp.R
import com.example.newsapp.callbacks.NewsCallbacks

class NewsArticleAdapter(
    private val mArticleList: List<Article>,
    private val newsCallbacks: NewsCallbacks
) :
    RecyclerView.Adapter<NewsArticleAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        )
    }

    override fun getItemCount() = mArticleList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.ic_image_grey_24dp)
            .error(R.drawable.ic_broken_image_grey_24dp)
        if (mArticleList.get(position).urlToImage != null)
            Glide.with(mContext).load(mArticleList.get(position).urlToImage).centerCrop()
                .apply(options).into(holder.imgage)
        holder.title.text = mArticleList.get(position).title
        if (mArticleList.get(position).description != null)
            holder.description.text = mArticleList.get(position).description.toString()
        if (mArticleList.get(position).publishedAt != null) {
            val time = mArticleList.get(position).publishedAt
            holder.published.text = time!!.substring(0, time.indexOf('T')).plus("   ")
                .plus(time.substring(time.indexOf('T') + 1, time.indexOf('Z')))
        }
        if (mArticleList.get(position).source!!.name != null)
            holder.source.text = mArticleList.get(position).source!!.name
        if (mArticleList.get(position).url != null)
            holder.card.setOnClickListener {
                newsCallbacks.launchNewsWebView(position)
            }
        holder.btnSave.setOnClickListener {
            newsCallbacks.saveArticle(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgage: ImageView = itemView.findViewById(R.id.iv_article_image)
        val title: TextView = itemView.findViewById(R.id.tv_article_title)
        val description: TextView = itemView.findViewById(R.id.tv_article_description)
        val published: TextView = itemView.findViewById(R.id.tv_published)
        val source: TextView = itemView.findViewById(R.id.tv_source)
        val card: CardView = itemView.findViewById(R.id.card_article)
        val btnSave: ImageButton = itemView.findViewById(R.id.btn_save)
    }
}