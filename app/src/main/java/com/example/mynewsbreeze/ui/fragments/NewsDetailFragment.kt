package com.example.mynewsbreeze.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mynewsbreeze.R
import com.example.mynewsbreeze.ui.MainActivity
import com.example.mynewsbreeze.ui.NewsViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_news_detail.*
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsDetailFragment : Fragment(R.layout.fragment_news_detail) {
    lateinit var viewModel: NewsViewModel
    val args: NewsDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        val article : Map<String,Any> = Gson().fromJson(args.article,mapType)

        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article["url"].toString())
        }

    }
}