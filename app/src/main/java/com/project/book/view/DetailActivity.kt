package com.project.book.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.bumptech.glide.Glide
import com.project.book.AppDatabase
import com.project.book.R
import com.project.book.databinding.ActivityDetailBinding
import com.project.book.model.Book
import com.project.book.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db : AppDatabase
    private val model = intent.getParcelableExtra<Book>("bookModel")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbSetting()
        getModel()
        saveComment()
    }


    private fun dbSetting() {
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()
    }

    private fun getModel(){
        // 상세 정보 값 가져오기
        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread{
            val review = db.reviewDao().getReview(model?.id?.toInt() ?: 0)
            binding.saveButton.setOnClickListener{
                runOnUiThread{
                    binding.reviewEditText.setText(review?.review.orEmpty())
                }
            }
        }.start()
    }

    private fun saveComment() {
        binding.saveButton.setOnClickListener{
            Thread{
                db.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?: 0,
                            binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }
    }
}