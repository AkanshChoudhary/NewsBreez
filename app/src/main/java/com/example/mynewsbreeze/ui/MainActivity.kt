package com.example.mynewsbreeze.ui

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mynewsbreeze.R
import com.example.mynewsbreeze.dao.CachedArticleDatabase
import com.example.mynewsbreeze.dao.SavedArticleDatabase
import com.example.mynewsbreeze.repository.NewsRepository
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val repository = NewsRepository(SavedArticleDatabase(this), CachedArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,repository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)
        val navHostFragment= supportFragmentManager.findFragmentById(R.id.newsNavigationHostFragment) as NavHostFragment
        val navController= navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        getSupportActionBar()?.hide()

    }
}