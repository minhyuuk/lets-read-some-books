package com.project.book.model

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("itemId") val id : Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String
)