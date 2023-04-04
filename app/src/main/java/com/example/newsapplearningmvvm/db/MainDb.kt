package com.example.newsapplearningmvvm.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapplearningmvvm.network.Model.Article

@Database(entities = [Article::class], version = 2, exportSchema = false)
@TypeConverters(ConverterDb::class)
abstract class MainDb:RoomDatabase() {

    abstract fun getDao():Dao
}