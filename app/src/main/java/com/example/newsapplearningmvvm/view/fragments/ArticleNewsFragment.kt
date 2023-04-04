package com.example.newsapplearningmvvm.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.newsapplearningmvvm.R
import com.example.newsapplearningmvvm.databinding.FragmentArticleNewsBinding
import com.example.newsapplearningmvvm.databinding.FragmentHomeBinding
import com.example.newsapplearningmvvm.network.Model.MainResponseApi
import com.example.newsapplearningmvvm.utils.Resourse
import com.example.newsapplearningmvvm.viewModel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [ArticleNewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ArticleNewsFragment : Fragment() {

    private var binding: FragmentArticleNewsBinding?=null
private val viewModel:NewsViewModel by viewModels()
    private val args: ArticleNewsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      binding = FragmentArticleNewsBinding.inflate(layoutInflater, container, false)
binding?.apply {
    webview.apply {
        webViewClient = WebViewClient()
        args?.article?.url?.let { loadUrl(it) }
    }
}
        return binding?.root
    }



}