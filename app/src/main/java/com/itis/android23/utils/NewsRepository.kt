package com.itis.android23.utils

import com.itis.android23.R
import com.itis.android23.model.News

object NewsRepository {
    private val newsList = mutableListOf<News>()

    init {
        // Переносим инициализацию вопросов в другую функцию
        initializeNewsList()
    }

    private fun initializeNewsList() {
        for (i in 1..45) {
            // Используем заглушки для изображений вместо реальных ссылок на фото
            val imageId = when (i % 3) {
                0 -> R.drawable.news_1
                1 -> R.drawable.news_2
                else -> R.drawable.news_3
            }

            val news = News("11.11.2011", imageId, "News Title $i", "Но́вости — информация, которая представляет политический, социальный или экономический интерес для аудитории в своей свежести, то есть сообщения о событиях, произошедших недавно или происходящих в данный момент.")
            newsList.add(news)
        }
    }

    fun getNewsList(countQuestions: Int): List<News> {
        // Очищаем список и затем заполняем его нужным количеством вопросов
        newsList.clear()
        initializeNewsList()

        // Ограничиваем количество вопросов в списке, чтобы не превышать countQuestions
        return newsList.take(countQuestions)
    }

    fun findNewsByTitle(title: String): News? {
        return newsList.find { it.title == title }
    }
}