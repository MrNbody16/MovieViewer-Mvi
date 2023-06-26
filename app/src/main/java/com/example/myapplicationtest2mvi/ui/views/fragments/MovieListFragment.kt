package com.example.myapplicationtest2mvi.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplicationtest2mvi.R
import com.example.myapplicationtest2mvi.databinding.FragmentMovieListBinding
import com.example.myapplicationtest2mvi.model.apiServices.Repo.MovieListFetchHelperImp
import com.example.myapplicationtest2mvi.model.apiServices.RetrofitClient
import com.example.myapplicationtest2mvi.ui.viewintents.MovieListViewIntent
import com.example.myapplicationtest2mvi.ui.viewmodels.MovieListViewModel
import com.mr_nbody16.moviewviewer.models.ConfigurationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieListFragment : BaseFragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding: FragmentMovieListBinding
        get() {
            return _binding!!
        }


    private lateinit var configs : ConfigurationResponse

    private val viewModel : MovieListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieListViewModel(MovieListFetchHelperImp(
                    requireContext().resources.getString(R.string.apiKey) ,
                    RetrofitClient.getClient().apis
                )) as T
            }
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.collect {

            }

            viewModel.configState.collect {
                // logic of its viewState
            }

            viewModel.genresState.collect {
                // no logic required to change view state but we need these stuff in details page
            }
        }

    }


    override fun initData() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.dispatchIntent(MovieListViewIntent.Loading(1))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}