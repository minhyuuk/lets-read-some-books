package com.project.book.model

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("itemId") val id : Long,
    @SerializedName("title") val title: String,
    @SerializedName("title") val description: String,
    @SerializedName("coverSmallUrl") val coverSmallUrl: String
)