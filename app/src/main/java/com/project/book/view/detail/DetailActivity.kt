package com.project.book.view.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.book.R
import com.project.book.databinding.ActivityDetailBinding
import com.project.book.data.model.Book
import com.project.book.data.model.Review
import com.project.book.util.migration.AppDatabase
import com.project.book.util.migration.getAppDatabase


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var db: AppDatabase

    private var model: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        model = intent.getParcelableExtra("bookModel")

        renderView()
        initSaveButton()
        imageBackgroundTranslucent()
    }

    private fun initSaveButton() {
        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?: 0,
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }
    }

    private fun renderView() {

        binding.titleTextView.text = model?.title.orEmpty()

        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)


        // 저장된 리뷰 데이터 가져오기
        Thread {
            val review = db.reviewDao().getOne(model?.id?.toInt() ?: 0)
            review?.let {
                runOnUiThread {
                    binding.reviewEditText.setText(it.review)

                }
            }
        }.start()
    }

    private fun imageBackgroundTranslucent(){
        val alpha : Drawable = findViewById<ImageView>(R.id.coverImageView).background
        alpha.alpha = 50
    }
}


