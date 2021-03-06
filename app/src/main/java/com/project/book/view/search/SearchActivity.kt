package com.project.book.view.search

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.book.R
import com.project.book.adapter.BookAdapter
import com.project.book.adapter.HistoryAdapter
import com.project.book.base.BaseActivity
import com.project.book.data.api.BookService
import com.project.book.data.model.BestSellerDTO
import com.project.book.data.model.History
import com.project.book.data.model.SearchBooksDTO
import com.project.book.databinding.ActivitySearchBinding
import com.project.book.databinding.FragmentHomeBinding
import com.project.book.databinding.ItemHomeBestSellerBinding
import com.project.book.util.API
import com.project.book.util.migration.AppDatabase
import com.project.book.util.migration.getAppDatabase
import com.project.book.view.detail.DetailActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {

    private lateinit var bookRecyclerViewAdapter: BookAdapter
    private lateinit var bookService: BookService
    private lateinit var historyAdapter: HistoryAdapter

    private val db: AppDatabase by lazy {
        getAppDatabase(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()
        initBookService()
        bookServiceLoadBestSellers()

        setSupportActionBar(binding.toolBar)
        supportActionBar?.apply {
            title = "?????? ??????"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun bookServiceLoadBestSellers() {
        // get best sellers
        bookService.getBestSeller(API.KEY)
            .enqueue(object : Callback<BestSellerDTO> {
                // response
                override fun onResponse(
                    call: Call<BestSellerDTO>,
                    response: Response<BestSellerDTO>
                ) {
                    // response success
                    if (response.isSuccessful.not()) {
                        Log.e(M_TAG, "NOT!! SUCCESS")
                        return
                    }

                    // response
                    response.body()?.let {
                        Log.d(M_TAG, it.toString())

                        it.books.forEach { book ->
                            Log.d(M_TAG, book.toString())
                        }

                        // new list
                        bookRecyclerViewAdapter.submitList(it.books)

                    }
                }

                // on failure
                override fun onFailure(call: Call<BestSellerDTO>, t: Throwable) {
                    Log.e(M_TAG, t.toString())
                }
            })
    }

    private fun initBookService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(API.BASE_URL) // base url
            .addConverterFactory(GsonConverterFactory.create()) // convert to gson
            .build()

        bookService = retrofit.create(BookService::class.java)
    }

    private fun initBookRecyclerView() {
        bookRecyclerViewAdapter = BookAdapter(clickListener = {
            val intent = Intent(this, DetailActivity::class.java)

            intent.putExtra("bookModel", it)
            startActivity(intent)
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = bookRecyclerViewAdapter


    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickListener = {
            deleteSearchKeyword(it)
        },this@SearchActivity)

        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter

        initSearchEditText()
    }


    private fun bookServiceSearchBook(keyword: String) {

        bookService.getBooksByName(API.KEY,keyword)
            .enqueue(object : Callback<SearchBooksDTO> {
                // success

                override fun onResponse(
                    call: Call<SearchBooksDTO>,
                    response: Response<SearchBooksDTO>
                ) {
                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {
                        return
                    }

                    bookRecyclerViewAdapter.submitList(response.body()?.books.orEmpty()) // ??? ???????????? ??????
                }

                // failure
                override fun onFailure(call: Call<SearchBooksDTO>, t: Throwable) {
                    hideHistoryView()
                    Log.e(M_TAG, t.toString())
                }
            })
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()
            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()

    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword)
            showHistoryView()
        }.start()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            // enter

            // enter -> search event
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                bookServiceSearchBook(binding.searchEditText.text.toString())
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
    }

    companion object {
        private const val M_TAG = "SearchActivity"
    }
}