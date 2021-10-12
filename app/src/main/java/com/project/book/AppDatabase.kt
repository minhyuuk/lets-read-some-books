package com.project.book

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.book.dao.HistoryDao
import com.project.book.dao.ReviewDao
import com.project.book.model.History
import com.project.book.model.Review

@Database(entities = [History::class, Review::class],version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}