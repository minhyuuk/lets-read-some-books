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
import com.project.book.adapter.BookAdapter
import com.project.book.adapter.HistoryAdapter
import com.project.book.api.BookService
import com.project.book.databinding.ActivityMainBinding
import com.project.book.model.BestSellerDTO
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
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initBookRecyclerView()
        dbSetting()
        initHistoryRecyclerView()
        retrofitClient()

    }

    private fun dbSetting() {
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
//                        Log.d(TAG, it.toString())

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


    }

    private fun search(keyword: String) {
        bookService.getBooksByMain(API.KEY, keyword)
            .enqueue(object : Callback<SearchBookDTO> {
                override fun onResponse(
                    call: Call<SearchBookDTO>,
                    response: Response<SearchBookDTO>
                ) {
                    hideRecyclerView()
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
                    hideRecyclerView()
                    Log.e(TAG, t.toString())
                }
            })
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showRecyclerView()
            }
            return@setOnTouchListener false
        }
    }

    private fun showRecyclerView() {
        Log.d(TAG,"show history recycler view")
        Thread {
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
            Log.d(TAG,"${keywords.toString()}")
        }.start()
    }

    private fun hideRecyclerView() {
        Log.d(TAG,"history recyclerview 숨기기")
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Log.d(TAG,"검색한 키워드 저장하기")
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showRecyclerView()
        }.start()
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter()

        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = adapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickListener = {
            deleteSearchKeyword(it)
        })
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter

        initSearchEditText()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}