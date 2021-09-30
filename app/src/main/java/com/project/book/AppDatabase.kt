package com.project.book

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.book.dao.HistoryDao
import com.project.book.model.History

@Database(entities = [History::class],version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao

}