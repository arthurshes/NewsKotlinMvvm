package com.example.newsapplearningmvvm.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapplearningmvvm.databinding.ItemBinding
import com.example.newsapplearningmvvm.network.Model.Article
import kotlinx.coroutines.channels.ReceiveChannel

class AdapterNews:RecyclerView.Adapter<AdapterNews.MyViewHolder>() {
    inner class MyViewHolder(val binding:ItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
return MyViewHolder(ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return diffResult.currentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
val newsPosition = diffResult.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView).load(newsPosition.urlToImage).into(imageView)
            textView.text = newsPosition.title
            textView2.text = newsPosition.description
            textView3.text = newsPosition.publishedAt
            imageView2.setOnClickListener {
                onSaveClick?.invoke(newsPosition)
                Log.d("clickItem","clickSaveNews")
            }
        }
        holder.itemView.setOnClickListener {
           onItemClick?.invoke(newsPosition)
            Log.d("clickItem","clickNews")
        }
    }

    private val diffCall = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
         return oldItem == newItem
        }

    }

    val diffResult = AsyncListDiffer(this,diffCall)

 var onItemClick: ((Article) -> Unit)?=null

  var onSaveClick:((Article)->Unit)?=null
}