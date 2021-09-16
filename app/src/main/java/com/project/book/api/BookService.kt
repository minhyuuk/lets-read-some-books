package com.project.book.api


import com.project.book.model.BestSellerDTO
import com.project.book.model.SearchBookDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/api/search.api?output=json")
    fun getBooksByMain(
        @Query("key") apiKey : String,
        @Query("query") keyword : String
    ): Call<SearchBookDTO>
    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ):Call<BestSellerDTO>
}