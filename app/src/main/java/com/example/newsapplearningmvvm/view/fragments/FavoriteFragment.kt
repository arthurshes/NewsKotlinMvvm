package com.example.newsapplearningmvvm.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplearningmvvm.R
import com.example.newsapplearningmvvm.databinding.FragmentFavoriteBinding
import com.example.newsapplearningmvvm.network.Model.Article
import com.example.newsapplearningmvvm.network.Model.MainResponseApi
import com.example.newsapplearningmvvm.utils.Resourse
import com.example.newsapplearningmvvm.view.adapters.AdapterNews
import com.example.newsapplearningmvvm.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var addapters:AdapterNews?=null
    private var observer: Observer<List<Article>>?=null
private val viewModel: NewsViewModel by viewModels()
private var binding:FragmentFavoriteBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment\

    binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        addapters = AdapterNews()
        observer = Observer {
addapters?.diffResult?.submitList(it)
        }

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
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
              val position = viewHolder.adapterPosition
                val article = addapters?.diffResult?.currentList?.get(position)
                if (article != null) {
                    viewModel.deleteNews(article)
                }
                view?.let {
                    Snackbar.make(it,"Новость успешно удалена",Snackbar.LENGTH_LONG).apply {
setAction("Отменить"){
    if (article != null) {
        viewModel.saveNews(article)
    }
    show()
}
                    }
                }
            }

        }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding?.favoriteRecycler)
        }
        val deco = DividerItemDecoration(activity,DividerItemDecoration.VERTICAL)
        binding?.apply {
            favoriteRecycler.apply {
                setHasFixedSize(true)
                addItemDecoration(deco)
                adapter = addapters
            }
        }
        return binding?.root
    }
    override fun onStart() {
        super.onStart()
        observer?.let { viewModel.getAllSaveNews().observe(viewLifecycleOwner, it) }
    }

    override fun onStop() {
        super.onStop()
        observer?.let { viewModel.getAllSaveNews().removeObserver(it) }
    }

}