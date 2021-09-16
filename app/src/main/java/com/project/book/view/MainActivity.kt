package com.project.book.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.project.book.R
import com.project.book.api.BookService
import com.project.book.model.BestSellerDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks("162DC33D7A1AC38FC0B10A94587A93407BDA8368C523670B3BCB07014E1C183B")
            .enqueue(object :Callback<BestSellerDTO>{
                override fun onResponse(
                    call: Call<BestSellerDTO>,
                    response: Response<BestSellerDTO>
                ) {
                    // 성공 처리
                    if(response.isSuccessful.not()){
                        Log.e(TAG,"Not success")

                        return
                    }
                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.books.forEach{ book ->  
                            Log.d(TAG,book.toString())

                        }
                    }
                }

                override fun onFailure(call: Call<BestSellerDTO>, t: Throwable) {
                    // 실패 처리
                    Log.e(TAG,t.toString())
                }

            })
    }
    companion object{
        private const val TAG = "MainActivity"
    }
}