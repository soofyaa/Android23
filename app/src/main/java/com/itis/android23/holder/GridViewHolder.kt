package com.itis.android23.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.itis.android23.databinding.ItemGridNewsBinding
import com.itis.android23.model.News

class GridViewHolder (item: View) : RecyclerView.ViewHolder(item) {
    val binding = ItemGridNewsBinding.bind(item)

    fun bind(news: News) = with(binding) {
        tvTitle.text = news.title
        ivCover.setImageResource(news.imageId)
    }
}