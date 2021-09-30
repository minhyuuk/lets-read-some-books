package com.project.book.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.project.book.AppDatabase
import com.project.book.R
import com.project.book.adapter.BookAdapter
import com.project.book.adapter.HistoryAdapter
import com.project.book.api.BookService
import com.project.book.databinding.ActivityMainBinding
import com.project.book.model.BestSellerDTO
import com.project.book.model.Book
import com.project.book.model.History
import com.project.book.model.SearchBookDTO
import com.project.book.util.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapter: BookAdapter
    private lateinit var bookService: BookService
    private lateinit var db: AppDatabase
    private lateinit var historyAdapter:HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dbSetting()
        initBookRecyclerView()
        retrofitClient()

    }

    private fun dbSetting(){
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()
    }

    private fun retrofitClient() {
        val retrofit = Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        bookService.getBestSellerBooks(API.KEY)
            .enqueue(object : Callback<BestSellerDTO> {
                override fun onResponse(
                    call: Call<BestSellerDTO>,
                    response: Response<BestSellerDTO>
                ) {
                    // 성공 처리
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "Not success")

                        return
                    }
                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        adapter.submitList(it.books)
                    }
                }

                override fun onFailure(call: Call<BestSellerDTO>, t: Throwable) {
                    // 실패 처리
                    Log.e(TAG, t.toString())
                }
            })

        binding.searchEditText.setOnKeyListener{v, keyCode, event->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN){
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun search(keyword: String){
        bookService.getBooksByMain(API.KEY,keyword)
            .enqueue(object : Callback<SearchBookDTO> {
                override fun onResponse(
                    call: Call<SearchBookDTO>,
                    response: Response<SearchBookDTO>
                ) {

                    saveSearchKeyword(keyword)
                    // 성공 처리
                    if (response.isSuccessful.not()) {
                        Log.e(TAG, "Not success")

                        return
                    }
                    response.body()?.let {
                        Log.d(TAG, it.toString())

                        it.books.forEach { book ->
                            Log.d(TAG, book.toString())
                        }
                        adapter.submitList(response.body()?.books.orEmpty())
                    }
                }

                override fun onFailure(call: Call<SearchBookDTO>, t: Throwable) {
                    // 실패 처리
                    Log.e(TAG, t.toString())
                }
            })
    }

    private fun showRecyclerView(){
        Thread{
            val keywords = db.historyDao().getAll().reversed()
        }
        binding.historyRecyclerView.isVisible = true
    }

    private fun hideRecyclerView(){

        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword:String){
        Thread{
            db.historyDao().insertHistory(History(null,keyword))
        }.start()
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter()

        binding.bookRecyclerView.adapter = adapter
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}