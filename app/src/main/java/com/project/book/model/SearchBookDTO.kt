package com.project.book.model

import com.google.gson.annotations.SerializedName

data class SearchBookDTO(
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
)
