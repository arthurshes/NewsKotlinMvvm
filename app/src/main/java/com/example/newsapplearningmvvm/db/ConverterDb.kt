package com.example.newsapplearningmvvm.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.newsapplearningmvvm.network.Model.Source

class ConverterDb {
    @TypeConverter
    fun fromSourese(source: Source):String {
        return source.name
    }

    @TypeConverter
    fun toSourse(name:String):Source {
        return Source(name,name)
    }
}