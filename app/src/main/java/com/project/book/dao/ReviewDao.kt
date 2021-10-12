package com.project.book.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.book.model.Review

@Dao
interface ReviewDao {

    @Query("SELECT * FROM Review WHERE id == :id")
    fun getReview(id:Int) : Review

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)
}