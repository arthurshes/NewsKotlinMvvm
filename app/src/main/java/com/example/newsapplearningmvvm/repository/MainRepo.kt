package com.example.newsapplearningmvvm.repository

import com.example.newsapplearningmvvm.db.MainDb
import com.example.newsapplearningmvvm.network.ApiService
import com.example.newsapplearningmvvm.network.Model.Article
import javax.inject.Inject

class MainRepo @Inject constructor(private val apiService: ApiService,private val db: MainDb) {
    private val dao = db.getDao()
    suspend fun getNewNews(country:String,page:Int) =
        apiService.getNews(page,country)

    suspend fun searchNews(query:String,page: Int) =
        apiService.searchForNews(query,page)

    suspend fun insertNews(article: Article) = dao.insertNews(article)

    fun getAllSaveNews() = dao.getFavoriteNews()

    suspend fun deleteNews(article: Article) = dao.deleteNews(article)
}