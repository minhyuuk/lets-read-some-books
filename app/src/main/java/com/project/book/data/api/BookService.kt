package com.project.book.data.api


import com.project.book.data.model.BestSellerDTO
import com.project.book.data.model.SearchBooksDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBooksDTO>

    @GET("/api/bestSeller.api?categoryId=100&output=json")
    fun getBestSeller(@Query("key") apiKey: String): Call<BestSellerDTO>
}