package com.example.mynewsbreeze.api

import com.example.mynewsbreeze.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.mynewsbreeze.util.Constants
import retrofit2.Response

interface NewsAPI  {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode : String = "in",
        @Query("page") pageNo : Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchStr : String ,
        @Query("page") pageNo : Int = 1,
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): Response<NewsResponse>
}