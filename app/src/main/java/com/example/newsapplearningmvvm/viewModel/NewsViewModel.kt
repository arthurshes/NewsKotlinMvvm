package com.example.newsapplearningmvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplearningmvvm.network.Model.Article
import com.example.newsapplearningmvvm.network.Model.MainResponseApi
import com.example.newsapplearningmvvm.repository.MainRepo
import com.example.newsapplearningmvvm.utils.Resourse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repo: MainRepo): ViewModel() {
    val newNews: MutableLiveData<Resourse<MainResponseApi>> = MutableLiveData()
    var newNewsPage = 1

    val searchNews: MutableLiveData<Resourse<MainResponseApi>> = MutableLiveData()
    var searchPage = 1
var breakingNewsResponse : MainResponseApi?=null
    var searchNewsResponse:MainResponseApi?=null
    init {
        getNewNews("ru")
    }

fun getNewNews(country:String){
    viewModelScope.launch {
        newNews.postValue(Resourse.Loading())
        val response = repo.getNewNews(country,newNewsPage)
        newNews.postValue(handleBreakingResponse(response))
    }

    }

    private fun handleBreakingResponse(response:Response<MainResponseApi>):Resourse<MainResponseApi>{
        if (response.isSuccessful){
            response.body()?.let {
                newNewsPage++
                if (breakingNewsResponse == null){
                    breakingNewsResponse = it
                }else{
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticl = it.articles
                    oldArticle?.addAll(newArticl)
                }
                return Resourse.Success(breakingNewsResponse ?: it)
            }
        }
        return Resourse.Error(response.message())
    }

    private fun handleSearchNewsResponse(response:Response<MainResponseApi>):Resourse<MainResponseApi>{
        if (response.isSuccessful){
            response.body()?.let {
                searchPage++
                if (searchNewsResponse == null){
                    searchNewsResponse = it
                }else{
                    val oldArticle = searchNewsResponse?.articles
                    val newArticl = it.articles
                    oldArticle?.addAll(newArticl)
                }
                return Resourse.Success(searchNewsResponse ?: it)
            }
        }
        return Resourse.Error(response.message())
    }

    fun searchNews(query: String) = viewModelScope.launch {
        searchNews.postValue(Resourse.Loading())
        val response = repo.searchNews(query,searchPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    fun saveNews(article: Article) = viewModelScope.launch {
        repo.insertNews(article)
    }

    fun getAllSaveNews() = repo.getAllSaveNews()

    fun deleteNews(article: Article) = viewModelScope.launch { repo.deleteNews(article) }
}