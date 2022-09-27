package com.example.mynewsbreeze.dao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mynewsbreeze.model.Article

@Database(
    entities = [Article::class],
    version = 3
)
@TypeConverters(
    Converter::class
)
abstract class CachedArticleDatabase : RoomDatabase() {
    abstract fun getArticleDAO(): ArticleDAO

    companion object{

        @Volatile
        private var instance: CachedArticleDatabase?=null
        private val lock = Any()

        operator fun invoke(context: Context)= instance?: synchronized(lock){
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                CachedArticleDatabase::class.java,
                "cached_article_db.db"
            ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
    }
}