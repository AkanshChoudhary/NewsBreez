package com.example.mynewsbreeze.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.mynewsbreeze.NewsApplication
import com.example.mynewsbreeze.model.Article
import com.example.mynewsbreeze.model.NewsResponse
import com.example.mynewsbreeze.repository.NewsRepository
import com.example.mynewsbreeze.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel (
        app: Application,
val newsRepository: NewsRepository
        ): AndroidViewModel(app) {
        val breakingNews: MutableLiveData<Resource<NewsResponse>> =
                MutableLiveData()
        var breakingNewsPage = 1
        var breakingNewsResponse: NewsResponse?= null
        val searchNews: MutableLiveData<Resource<NewsResponse>> =
                MutableLiveData()
        var searchNewsPage = 1
        var searchNewsResponse: NewsResponse?= null
        init {
            getBreakingNews("in")
        }
        fun getBreakingNews(countryCode:String) = viewModelScope.launch {
                safeBreakingNewsCall(countryCode)

        }

        fun searchNews(searchQuery: String)=viewModelScope.launch {
               safeSearchNewsCall(searchQuery)
        }

        fun handleBreakingNewsResponse(response:Response<NewsResponse>): Resource<NewsResponse>{
                if(response.isSuccessful){
                        response.body()?.let {
                                breakingNewsPage++
                                if(breakingNewsResponse==null){
                                        breakingNewsResponse=it
                                }else{
                                        breakingNewsResponse?.articles?.addAll(it.articles)
                                }
                                return Resource.Success(breakingNewsResponse?:it)
                        }
                }
                return Resource.Error(response.message())
        }

        fun handleSearchNewsResponse(response:Response<NewsResponse>): Resource<NewsResponse>{
                if(response.isSuccessful){
                        response.body()?.let {
                                return Resource.Success(it)
                        }
                }
                return Resource.Error(response.message())
        }

        fun saveArticle(article: Article)= viewModelScope.launch {
                newsRepository.insert(article)
        }

        fun getSavedNews() = newsRepository.getSavedNews()

        fun getCachedNews() = newsRepository.getCachedNews()

        fun deleteAllCached() =  newsRepository.deleteCachedNews()
        fun deleteArticle(article: Article) = viewModelScope.launch {
                newsRepository.deleteArticle(article)
        }

        private suspend fun safeSearchNewsCall(searchQuery: String){
                searchNews.postValue(Resource.Loading())
                try{
                        if(hasInternet()){
                                val response = newsRepository.searchNews(searchQuery,searchNewsPage)

                                searchNews.postValue(handleSearchNewsResponse(response))
                        }else{
                                searchNews.postValue(Resource.Error("No Network"))
                        }
                }catch (t: Throwable){
                        when(t){
                                is IOException-> searchNews.postValue(Resource.Error("Network Failure"))
                                else-> searchNews.postValue(Resource.Error("Conversion Error"))
                        }
                }
        }
        private suspend fun safeBreakingNewsCall(countryCode: String){
                breakingNews.postValue(Resource.Loading())
                try{
                        if(hasInternet()){
                                val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
                                for(i in response.body()?.articles!!){
                                        newsRepository.insertCacheNews(i)
                                }
                                breakingNews.postValue(handleBreakingNewsResponse(response))
                        }else{
                                breakingNews.postValue(Resource.Error("No Internet"))
                        }
                }catch (t: Throwable){
                        when(t){
                                is IOException-> breakingNews.postValue(Resource.Error("Network Failure"))
                                else-> breakingNews.postValue(Resource.Error("Conversion Error"))
                        }
                }
        }
        fun hasInternet(): Boolean{
                val connectivityManager = getApplication<NewsApplication>().getSystemService(
                        Context.CONNECTIVITY_SERVICE
                ) as ConnectivityManager

                val activeNetwork = connectivityManager.activeNetwork?:return false
                val capabalities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false

                return when{
                        capabalities.hasTransport(TRANSPORT_WIFI)-> true
                        capabalities.hasTransport(TRANSPORT_CELLULAR)-> true
                        capabalities.hasTransport(TRANSPORT_ETHERNET)-> true
                        else -> false
                }
        }
}