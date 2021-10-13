package com.project.book.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.project.book.AppDatabase
import com.project.book.databinding.ActivityDetailBinding
import com.project.book.getAppDatabase
import com.project.book.model.Book
import com.project.book.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbSetting()
        getModel()
    }


    private fun dbSetting() {
        db = getAppDatabase(this)
    }

    private fun getModel(){
        val model = intent.getParcelableExtra<Book>("bookModel")
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
                    binding.reviewEditText.setText(review?.review?.orEmpty())
                }
            }
        }.start()

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