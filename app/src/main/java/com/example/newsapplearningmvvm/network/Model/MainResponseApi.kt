package com.example.newsapplearningmvvm.network.Model

data class MainResponseApi(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)