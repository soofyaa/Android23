package com.itis.android23.model


data class News(
    val date: String,
    val imageId: Int,
    val title: String,
    val description: String,
    var isFavorite: Boolean = false,
    val id: Int = nextId(),
) {
    companion object {
        private var idCounter = 0

        private fun nextId(): Int {
            return idCounter++
        }
    }
}
