package com.example.mynewsbreeze.repository

import com.example.mynewsbreeze.api.RetrofitInstance
import com.example.mynewsbreeze.dao.CachedArticleDatabase
import com.example.mynewsbreeze.dao.SavedArticleDatabase
import com.example.mynewsbreeze.model.Article

class NewsRepository(val db: SavedArticleDatabase,val cdb: CachedArticleDatabase) {
    suspend fun getBreakingNews(countryCode:String,pageNo:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNo)

    suspend fun searchNews(query: String,pageNo: Int)=
        RetrofitInstance.api.searchNews(query,pageNo)

    suspend fun insert(article: Article) = db.getArticleDAO().insert(article)

    fun getSavedNews() = db.getArticleDAO().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDAO().deleteArticle(article)

    fun getCachedNews() = cdb.getArticleDAO().getAllArticles()

    suspend fun insertCacheNews(article: Article) = cdb.getArticleDAO().insert(article)

    fun deleteCachedNews() = cdb.getArticleDAO().deleteAllCached()
}