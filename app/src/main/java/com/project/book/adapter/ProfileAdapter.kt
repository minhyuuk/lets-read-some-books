package com.project.book.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.book.data.model.Book
import com.project.book.databinding.ActivityDetailBinding
import com.project.book.databinding.ItemHistoryBinding
import com.project.book.databinding.ItemReviewCollectorBinding

class ProfileAdapter() {
    private lateinit var dbinding: ActivityDetailBinding

    inner class ViewHolder(private val binding: ItemReviewCollectorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bookModel : Book){
//            binding.itemTitle.text = bookModel.title
//            binding.itemDetail.text = dbinding.reviewEditText.text
//
//            Glide
//                .with(binding.itemImage.context)
//                .load(bookModel.coverSmallUrl)
//                .into(binding.itemImage)
        }

    }


}