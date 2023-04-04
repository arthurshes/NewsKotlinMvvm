package com.example.newsapplearningmvvm.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplearningmvvm.R
import com.example.newsapplearningmvvm.databinding.FragmentHomeBinding
import com.example.newsapplearningmvvm.network.Model.Article
import com.example.newsapplearningmvvm.network.Model.MainResponseApi
import com.example.newsapplearningmvvm.utils.Constants.QUERY_PAGE_SIZE
import com.example.newsapplearningmvvm.utils.Resourse
import com.example.newsapplearningmvvm.view.adapters.AdapterNews
import com.example.newsapplearningmvvm.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
private val viewModel:NewsViewModel by viewModels()
    private var adapters:AdapterNews?=null
private  var observer:Observer<Resourse<MainResponseApi>>?=null
    private var binding:FragmentHomeBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val deco = DividerItemDecoration(activity,DividerItemDecoration.VERTICAL)

        adapters?.onSaveClick = {
            viewModel.saveNews(it)
        }

       adapters?.onItemClick = {
           val action = HomeFragmentDirections.actionHomeFragmentToArticleNewsFragment(it)
           findNavController().navigate(action)
       }

        var isLoading = false
        var isLastPage = false
        var isScroll = false

        val scrollListen = object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding?.homeRecycler?.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                val isNoLoadedAndNoLastPage = !isLoading && !isLastPage
                val iAtLastItem = firstVisibleItemPosition + visibleItemCount >=  totalItemCount
                val isNotAtBeginning = firstVisibleItemPosition >= 0
                val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
                val shouldPaginate  = isNoLoadedAndNoLastPage && isLastPage && isNotAtBeginning && isTotalMoreThanVisible && isScroll
                if (shouldPaginate){
                    viewModel.getNewNews("ru")
                    isScroll = false
                }else{
                    binding?.homeRecycler?.setPadding(0,0,0,0)
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScroll = true
                }
            }
        }

        observer = Observer { response ->
            when(response){
                is Resourse.Success ->{
                    binding?.progressBar?.visibility = View.GONE
                    isLoading = false
                    response.data?.let { newsResponse->
                        adapters?.diffResult?.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.newNewsPage == totalPages
                    }
                }
                is Resourse.Error ->{
                    binding?.progressBar?.visibility = View.GONE
                    isLoading = false
                    response.message?.let {error->
                        Toast.makeText(activity,"Ошибка $error",Toast.LENGTH_LONG).show()
                    }
                }
                is Resourse.Loading ->{
                    binding?.progressBar?.visibility = View.VISIBLE
                    isLoading = true
                }
            }

        }


adapters = AdapterNews()
        binding?.apply {
            homeRecycler.apply {
                setHasFixedSize(true)
                adapter = adapters
                addItemDecoration(deco)
                addOnScrollListener(scrollListen)
            }
        }
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        observer?.let { viewModel.newNews.observe(viewLifecycleOwner, it) }
    }

    override fun onStop() {
        super.onStop()
        observer?.let { viewModel.newNews.removeObserver(it) }
    }

}


