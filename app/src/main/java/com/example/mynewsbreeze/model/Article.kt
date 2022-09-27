package com.example.mynewsbreeze.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable;


@Entity(
    tableName = "articles" ,
    indices = [Index(value = ["url"], unique = true)]
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
    var saved: Boolean?
) : Serializable