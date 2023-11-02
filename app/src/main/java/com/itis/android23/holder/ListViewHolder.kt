package com.itis.android23.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.itis.android23.databinding.ItemListNewsBinding
import com.itis.android23.model.News

class ListViewHolder(item: View) : RecyclerView.ViewHolder(item) {
    val binding = ItemListNewsBinding.bind(item)

    fun bind(news: News) = with(binding) {
        tvTitle.text = news.title
        ivCover.setImageResource(news.imageId)
    }
}