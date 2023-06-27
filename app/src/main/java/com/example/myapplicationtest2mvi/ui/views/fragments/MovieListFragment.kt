package com.example.myapplicationtest2mvi.ui.views.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplicationtest2mvi.model.apiServices.Repo.MovieListFetchHelperImp
import com.example.myapplicationtest2mvi.model.apiServices.RetrofitClient
import com.example.myapplicationtest2mvi.model.entities.Go2DetailsHolder
import com.example.myapplicationtest2mvi.ui.viewintents.MovieListViewIntent
import com.example.myapplicationtest2mvi.ui.viewmodels.MovieListViewModel
import com.example.myapplicationtest2mvi.ui.viewstate.MovieListViewState
import com.example.myapplicationtest2mvi.utils.Constants
import com.example.myapplicationtest2mvi.utils.interfaces.MovieListItemSelector
import com.google.gson.Gson
import com.mr_nbody16.helperClasses.NavOpt
import com.mr_nbody16.moviewviewer.models.ConfigurationResponse
import com.mr_nbody16.moviewviewer.models.MovieListResponse
import com.mr_nbody16.moviewviewer.view.adapters.MovieListAdapter
import com.mr_nbody16_movieViwer_mvi.R
import com.mr_nbody16_movieViwer_mvi.databinding.FragmentMovieListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MovieListFragment : BaseFragment(), MovieListItemSelector {

    companion object {
        private const val mainListError = 1
        private const val configError = 2
        private const val genresError = 3
    }

    private var _binding: FragmentMovieListBinding? = null
    private val binding: FragmentMovieListBinding
        get() {
            return _binding!!
        }

    private lateinit var adapter: MovieListAdapter

    private val errorSets: MutableSet<Int> = mutableSetOf()

    private var firstBoot = true

    //declaring this property in here might be a bad design but for now i don't have any ideas
    private var configHolder: String = ""

    private val viewModel: MovieListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieListViewModel(
                    MovieListFetchHelperImp(
                        requireContext().resources.getString(R.string.apiKey),
                        RetrofitClient.getClient().apis
                    )
                ) as T
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)

        adapter = MovieListAdapter(requireContext(), this)

        binding.movieListRecycler.apply {
            adapter = this@MovieListFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.collect {
                Log.i(Constants.TAG, "MainState : $it")
                updateMainUiDetails(it)
            }


        }

        lifecycleScope.launch {
            viewModel.configState.collect {
                Log.i(Constants.TAG, "ConfigState : $it")
                updateConfigStat(it as MovieListViewState)
            }
        }


        lifecycleScope.launch {
            viewModel.genresState.collect {
                // no logic required to change view state but we need these stuff in details page
                Log.i(Constants.TAG, "GenresState : $it")
                updateGenreState(it as MovieListViewState)
            }
        }

        binding.warningIcon.setOnClickListener {
            showWarningErrors()
        }

        initData()

        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                delay(10000)
                Log.i(Constants.TAG, "errorSet : $errorSets")
                Log.i(Constants.TAG, "MainList : ${viewModel.state.value}")
                Log.i(Constants.TAG, "Config : ${viewModel.configState.value}")
                Log.i(Constants.TAG, "Genres : ${viewModel.genresState.value}")
            }
        }

    }


    override fun initData() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.dispatchIntent(MovieListViewIntent.LoadAllData(viewModel.pageCounter.value))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createWarningMessage(errorCode: Int, message: String): String {
        if (message.isEmpty()) {
            return message + when (errorCode) {
                mainListError -> "MovieViewer could not load movie's list check your internet connection"
                configError -> "MovieViewer could not load required configurations check your internet connection"
                genresError -> "MovieViewer could not load required configurations(genres) check"
                else -> {}
            }
        } else {
            return message + "\n" + when (errorCode) {
                mainListError -> "MovieViewer could not load movie's list check your internet connection"
                configError -> "MovieViewer could not load required configurations check your internet connection"
                genresError -> "MovieViewer could not load required configurations(genres) check"
                else -> {}
            }
        }
    }

    private fun showWarningErrors() {
        var message = ""
        errorSets.forEach {
            message = createWarningMessage(it, message)
        }
        AlertDialog.Builder(requireContext())
            .setTitle("Warning!!")
            .setMessage(message)
            .setCancelable(true)
            .setNegativeButton("Never mind") { dialogInterface, i ->
                dialogInterface.dismiss()
                wiggleWarningIcon(25F, 20L)
            }.setPositiveButton("Refresh") { dialogInterface, i ->
                dialogInterface.dismiss()
                errorSets.clear()
                initData()
            }.create().show()
    }

    private fun updateMainUiDetails(state: MovieListViewState?) {
        /*if (state == null) {
            errorSets.plus(mainListError)
            binding.pageCounterLinear.visibility = View.GONE
            binding.warningIcon.visibility = View.VISIBLE
            binding.movieListRecycler.visibility = View.GONE
            Log.i(Constants.TAG , "state is null - MainUiDetails")
            wiggleWarningIcon(25F, 20L)
        } else {*/
        when (state) {
            is MovieListViewState.Loading -> {
                binding.warningIcon.visibility = View.GONE
                binding.loadingIndicator.visibility = View.VISIBLE
                binding.pageCounterLinear.visibility = View.GONE
                binding.movieListRecycler.visibility = View.GONE
                adapter.setMovieList(MovieListResponse())
            }

            is MovieListViewState.DataISLoaded<*> -> {
                if (state.data is Go2DetailsHolder) {
                    val bundle = Bundle().also {
                        it.putInt("movieId", state.data.movieId ?: 0)
                        it.putString("config", configHolder)
                    }
                    findNavController().navigate(R.id.list2Detials, bundle, NavOpt.build())
                } else {
                    with(state.data as MovieListResponse) {
                        binding.loadingIndicator.visibility = View.GONE
                        binding.pageCounterLinear.visibility = View.VISIBLE
                        binding.movieListRecycler.visibility = View.VISIBLE
                        Log.i(Constants.TAG, "UpdatingMainUiDetails : $this")
                        binding.pageCounter.text =
                            "${viewModel.pageCounter.value.toString()} / ${this.total_pages}"
                        adapter.setMovieList(this)
                    }
                }
            }

            is MovieListViewState.Exception -> {
                errorSets.plus(mainListError)
                binding.pageCounterLinear.visibility = View.GONE
                binding.warningIcon.visibility = View.VISIBLE
                binding.movieListRecycler.visibility = View.GONE
                binding.loadingIndicator.visibility = View.GONE
                Log.i(
                    Constants.TAG,
                    "state has exceptions { ${state.callError} } - MainUiDetails"
                )
                wiggleWarningIcon(25F, 20L)
            }

            else -> {}
        }

//        }
    }

    private fun updateConfigStat(state: MovieListViewState) {
        when (state) {
            is MovieListViewState.DataISLoaded<*> -> {
                adapter.setConfiguration(state.data as ConfigurationResponse)

            }

            else -> {
                adapter.setConfiguration(null)
                errorSets.plus(configError)
                binding.warningIcon.visibility = View.VISIBLE
                Log.i(Constants.TAG, "Config has a problem $state - UpdateConfigStat")
                wiggleWarningIcon(25F, 20L)
            }
        }
    }

    private fun updateGenreState(state: MovieListViewState) {
        when (state) {
            is MovieListViewState.DataISLoaded<*> -> {
                adapter.setGenresList(state.data as HashMap<Int, String>)
            }

            else -> {
                adapter.setGenresList(null)
                errorSets.plus(genresError)
                binding.warningIcon.visibility = View.VISIBLE
                Log.i(Constants.TAG, "Genres has a problem $state - UpdateGenresStat")
                wiggleWarningIcon(25F, 20L)
            }
        }
    }


    private fun wiggleWarningIcon(offset: Float, duration: Long) {
        binding.warningIcon.startAnimation(TranslateAnimation(-offset, offset, 0F, 0F)
            .also {
                it.duration = duration
                it.repeatMode = Animation.REVERSE
                it.repeatCount = 5
            })
    }

    override fun onItemSelect(movieId: Int, config: String) {
        configHolder = config
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.dispatchIntent(MovieListViewIntent.Go2DetailsIntent(movieId))
        }
    }


}