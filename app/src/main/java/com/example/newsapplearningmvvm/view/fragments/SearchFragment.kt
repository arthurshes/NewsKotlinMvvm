package com.example.newsapplearningmvvm.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.newsapplearningmvvm.R
import com.example.newsapplearningmvvm.databinding.FragmentSearchBinding
import com.example.newsapplearningmvvm.network.Model.MainResponseApi
import com.example.newsapplearningmvvm.utils.Resourse
import com.example.newsapplearningmvvm.view.adapters.AdapterNews
import com.example.newsapplearningmvvm.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var observer:Observer<Resourse<MainResponseApi>>?=null
private var binding:FragmentSearchBinding?=null
    private var adapters:AdapterNews?=null
    private val viewModel:NewsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment
        adapters = AdapterNews()
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        val deco = DividerItemDecoration(activity,DividerItemDecoration.VERTICAL)
        adapters?.onSaveClick = {
            viewModel.saveNews(it)
        }
        adapters?.onItemClick = {
            val action = SearchFragmentDirections.actionSearchFragmentToArticleNewsFragment(it)
            findNavController().navigate(action)
        }
        binding?.apply {
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotEmpty()) {
                        viewModel.searchNews(it)
                    }
                }
               return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {




                return false
            }

        } )

            observer = Observer { response->
                when(response){
                    is Resourse.Success ->{
                        binding?.progressBar2?.visibility = View.GONE
                        response.data?.let { newsResponse->
                            adapters?.diffResult?.submitList(newsResponse.articles)
                        }
                    }
                    is Resourse.Error ->{
                        binding?.progressBar2?.visibility = View.GONE
                        response.message?.let {error->
                            Toast.makeText(activity,"Ошибка $error", Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resourse.Loading ->{
                        binding?.progressBar2?.visibility = View.VISIBLE
                    }
                }
            }
            searchRecycler.apply {
                setHasFixedSize(true)
                addItemDecoration(deco)
                adapter = adapters
            }
        }
        return binding?.root
    }

    override fun onStart() {
        super.onStart()
        observer?.let { viewModel.searchNews.observe(viewLifecycleOwner, it) }
    }

    override fun onStop() {
        super.onStop()
        observer?.let { viewModel.searchNews.removeObserver(it) }
    }

}