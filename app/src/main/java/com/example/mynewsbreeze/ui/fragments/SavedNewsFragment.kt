package com.example.mynewsbreeze.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsbreeze.R
import com.example.mynewsbreeze.adapters.NewsAdapter
import com.example.mynewsbreeze.model.Article
import com.example.mynewsbreeze.ui.MainActivity
import com.example.mynewsbreeze.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        var full_list  = ArrayList<Article>()
        back_button.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
            search_saved_news.setText("")
            newsAdapter.differ.submitList(it)
            full_list = it as ArrayList<Article>
        })
        newsAdapter.onItemClickListener = {
            val bundle = Bundle()
            val gson = Gson()
            val jsonArticle: String = gson.toJson(it)
            bundle.putSerializable("article",jsonArticle)
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_newsDetailFragment,
                bundle
            )
        }

        search_saved_news.addTextChangedListener{str->
               str?.let {
                   if(str.toString().isNotEmpty()){
                       filterList(str.toString(),newsAdapter,full_list)
                   }else if(str == null || str.toString() == ""){
                       newsAdapter.differ.submitList(full_list)
                   }
               }
        }
        val itemTouchHelperCallback = object:  ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[pos]
                viewModel.deleteArticle(article)
                Snackbar.make(view,"Successfully deleted",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvSavedNews)
        }
    }

    private fun filterList(str: String,newsAdapter: NewsAdapter,full_list: ArrayList<Article>){
        var curr = full_list
        var new = ArrayList<Article>()

        for(c in curr){
            if(c.title?.lowercase()?.contains(str.lowercase()) == true){
                new.add(c)
            }
        }
        newsAdapter.differ.submitList(new)

    }
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        newsAdapter.type = 2
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}