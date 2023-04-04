package com.example.newsapplearningmvvm.network

import com.example.newsapplearningmvvm.network.Model.MainResponseApi
import com.example.newsapplearningmvvm.utils.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
@GET("/v2/everything")
suspend fun searchForNews(
    @Query("q") query: String,
    @Query("page") page:Int,
    @Query("apiKey") apiKey:String = API_KEY
):Response<MainResponseApi>

@GET("/v2/top-headlines")
suspend fun getNews(
    @Query("page") page: Int,
    @Query("country") country:String,
    @Query("category") category: String = "general",
    @Query("apiKey") apiKey: String = API_KEY
):Response<MainResponseApi>

}