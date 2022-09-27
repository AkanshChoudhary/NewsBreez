package com.example.mynewsbreeze.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mynewsbreeze.R
import com.example.mynewsbreeze.model.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*
import kotlinx.android.synthetic.main.saved_list_item.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    var onItemClickListener: ((Article) -> Unit)? = null
    var onSaveButtonClick: ((Article) -> Unit)? = null
    var type : Int = 0
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        if(type==1){
            return ArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_article_preview,
                    parent,
                    false
                )
            )
        }else{
            return ArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.saved_list_item,
                    parent,
                    false
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }



    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        var article = differ.currentList[position]
        if(type == 1){
            holder.itemView.apply {
                if(article.urlToImage==null || article.urlToImage ==""){
                    ivArticleImage.setImageResource(R.drawable.no_image)
                }
                else
                    Glide.with(this).load(article.urlToImage).into(ivArticleImage)
                tvSource.text = article.source.name
                tvTitle.text = article.title
                tvDescription.text = article.description
                tvPublishedAt.text = article.publishedAt?.subSequence(0,10)
            }
            holder.itemView.save_button.setOnClickListener {
                article.saved = true
                onSaveButtonClick?.invoke(article)
            }
            holder.itemView.read_button.setOnClickListener {
                onItemClickListener?.invoke(article)
            }
        }else{
            holder.itemView.apply {
                if(article.urlToImage==null || article.urlToImage ==""){
                    saved_ivArticleImage.setImageResource(R.drawable.no_image)
                }
                else
                    Glide.with(this).load(article.urlToImage).into(saved_ivArticleImage)
                saved_tvSource.text = article.source.name
                saved_tvTitle.text = article.title
                saved_tvDescription.text = article.description
                saved_tvPublishedAt.text = article.publishedAt?.subSequence(0,10)
            }
            holder.itemView.setOnClickListener{
                onItemClickListener?.invoke(article)
            }
        }

    }

//    fun setOnItemClickListener(listener: (Article) -> Unit) {
//        onItemClickListener = listener
//    }

}
