package com.project.book.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.book.AppDatabase
import com.project.book.adapter.BookAdapter
import com.project.book.adapter.HistoryAdapter
import com.project.book.data.api.BookService
import com.project.book.databinding.ActivityMainBinding
import com.project.book.getAppDatabase
import com.project.book.data.model.BestSellerDTO
import com.project.book.data.model.History
import com.project.book.data.model.SearchBooksDTO
import com.project.book.util.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var service: BookService

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = getAppDatabase(this@MainActivity)

        adapter = BookAdapter(clickListener = {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        })

        historyAdapter = HistoryAdapter(historyDeleteClickListener = {
            deleteSearchKeyword(it)
        })

        val retrofit = Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(BookService::class.java)
        service.getBestSeller(API.KEY)
            .enqueue(object: Callback<BestSellerDTO> {
                override fun onFailure(call: Call<BestSellerDTO>, t: Throwable) {

                }

                override fun onResponse(call: Call<BestSellerDTO>, response: Response<BestSellerDTO>) {
                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        adapter.submitList(it.books)
                    }
                }
            })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false

        }

        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }

            return@setOnTouchListener false
        }


        binding.historyRecyclerView.adapter = historyAdapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)


    }

    private fun search(text: String) {

        service.getBooksByName(API.KEY, text)
            .enqueue(object: Callback<SearchBooksDTO> {
                override fun onFailure(call: Call<SearchBooksDTO>, t: Throwable) {
                    hideHistoryView()
                }

                override fun onResponse(call: Call<SearchBooksDTO>, response: Response<SearchBooksDTO>) {
                    hideHistoryView()
                    saveSearchKeyword(text)

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        adapter.submitList(it.books)
                    }
                }

            })
    }

    private fun showHistoryView() {
        Thread(Runnable {
            db.historyDao().getAll().reversed().run {
                runOnUiThread {
                    binding.historyRecyclerView.isVisible = true
                    historyAdapter.submitList(this)
                }
            }

        }).start()

    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread(Runnable {
            db.historyDao().insertHistory(History(null, keyword))
        }).start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread(Runnable {
            db.historyDao().delete(keyword)
            showHistoryView()
        }).start()
    }
}