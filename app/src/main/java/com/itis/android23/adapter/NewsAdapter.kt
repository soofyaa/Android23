package com.itis.android23.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.itis.android23.R
import com.itis.android23.diffutils.NewsDiffCallback
import com.itis.android23.holder.GridViewHolder
import com.itis.android23.holder.ListViewHolder
import com.itis.android23.model.News
import java.util.Random

class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // я знаю, что сделала очень плохо, но уже не успеваю исправить :(

    var newsList = ArrayList<News>()
    var onFavoriteClick: ((Int) -> Unit)? = null
    var onItemClick: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutResId = if (viewType == VIEW_TYPE_VERTICAL) R.layout.item_list_news else R.layout.item_grid_news
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return if (viewType == VIEW_TYPE_VERTICAL) ListViewHolder(view) else GridViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> {
                if (position % 8 == 0) {
                    holder.binding.tvDate.visibility = View.VISIBLE
                    holder.binding.tvDate.text = "31.10.23"
                    holder.bind(newsList[position])
                    setupFavoriteButton(holder.binding.btnFavorite, newsList[position])
                } else {
                    holder.binding.tvDate.visibility = View.GONE
                    holder.bind(newsList[position])
                    setupFavoriteButton(holder.binding.btnFavorite, newsList[position])
                }
            }

            is GridViewHolder -> {
                if (position % 8 == 0 || position % 8 == 1) {
                    holder.binding.tvDate.visibility = View.VISIBLE
                    if (position % 8 == 1) {
                        holder.binding.tvDate.text = " "
                    } else {
                        holder.binding.tvDate.text = "31.10.23"
                    }
                    holder.bind(newsList[position])
                    setupFavoriteButton(holder.binding.btnFavorite, newsList[position])
                } else {
                    holder.binding.tvDate.visibility = View.GONE
                    holder.bind(newsList[position])
                    setupFavoriteButton(holder.binding.btnFavorite, newsList[position])
                }
            }
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    private fun setupFavoriteButton(button: ImageButton, news: News) {
        button.setOnClickListener {
            onFavoriteClick?.invoke(newsList.indexOf(news))
        }

        val isFavorite = news.isFavorite
        val imageResource = if (isFavorite) R.drawable.favorite else R.drawable.not_favorite
        button.setBackgroundResource(imageResource)
    }

    fun addNews(newNews: List<News>) {
        val currentNews = newsList.toMutableList()
        val random = java.util.Random()
        newNews.shuffled(random).forEach { newsItem ->
            val randomPosition = random.nextInt(currentNews.size + 1) // Генерация случайной позиции
            currentNews.add(randomPosition, newsItem)
        }
        submitList(currentNews)
    }

    private fun submitList(newList: List<News>) {
        val diffResult = DiffUtil.calculateDiff(NewsDiffCallback(newList, newsList))
        newsList.clear()
        newsList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (newsList.size <= 12) VIEW_TYPE_VERTICAL else VIEW_TYPE_GRID
    }

    companion object {
        private const val VIEW_TYPE_VERTICAL = 1
        private const val VIEW_TYPE_GRID = 2
    }
}