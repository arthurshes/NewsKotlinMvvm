package com.example.newsapplearningmvvm.db
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.newsapplearningmvvm.network.Model.Article

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(article: Article):Long

    @Query("SELECT * FROM NewsTable")
    fun getFavoriteNews():LiveData<List<Article>>

    @Delete
    suspend fun deleteNews(article: Article)
}