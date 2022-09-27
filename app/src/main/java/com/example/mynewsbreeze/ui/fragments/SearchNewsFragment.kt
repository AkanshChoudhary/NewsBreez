package com.example.mynewsbreeze.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsbreeze.R
import com.example.mynewsbreeze.adapters.NewsAdapter
import com.example.mynewsbreeze.ui.MainActivity
import com.example.mynewsbreeze.ui.NewsViewModel
import com.example.mynewsbreeze.util.Resource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    var TAG = "SearchNewsFragment"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        newsAdapter.onItemClickListener = {
            val bundle = Bundle()
            val gson = Gson()
            val jsonArticle: String = gson.toJson(it)
            bundle.putSerializable("article",jsonArticle)
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_newsDetailFragment,
                bundle
            )
        }
        var job : Job?= null

        etSearch.addTextChangedListener{str->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                str?.let {
                    if(str.toString().isNotEmpty()){
                        viewModel.searchNews(str.toString())
                    }
                }
            }
        }

        newsAdapter.onSaveButtonClick = {
            var res = viewModel.saveArticle(it)
            res.invokeOnCompletion {
                Toast.makeText(context,"Article Saved",Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer
        { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = (newsResponse.totalResults / 20) + 2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if(isLastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity,"Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount= layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20

            val shouldPaginate = isNotLoadingAndNotLastPage && isLastItem && isNotAtBeginning
                    &&isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.searchNews(etSearch.text.toString())
                isScrolling = false
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        newsAdapter.type = 1

        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }
}