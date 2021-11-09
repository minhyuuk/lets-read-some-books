package com.project.book.util.migration

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.project.book.data.dao.HistoryDao
import com.project.book.data.dao.ReviewDao
import com.project.book.data.model.History
import com.project.book.data.model.Review

@Database(entities = [History::class, Review::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao

}

fun getAppDatabase(context: Context): AppDatabase {

    val migration_1_2 = object : Migration(1,2){
        override fun migrate(database: SupportSQLiteDatabase) {

            database.execSQL("CREATE TABLE `REVIEW` ('id' INTEGER, `review` TEXT," + "PRIMARY KEY(`id`))")
        }

    }

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration_1_2)
        .build()
}