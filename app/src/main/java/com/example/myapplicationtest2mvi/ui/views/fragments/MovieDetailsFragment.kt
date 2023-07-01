package com.example.myapplicationtest2mvi.ui.views.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.myapplicationtest2mvi.model.apiServices.Repo.MovieDetailsFetchHelperImp
import com.example.myapplicationtest2mvi.model.apiServices.RetrofitClient
import com.example.myapplicationtest2mvi.ui.viewintents.MovieDetailsViewIntent
import com.example.myapplicationtest2mvi.ui.viewmodels.MovieDetailsViewModel
import com.example.myapplicationtest2mvi.ui.viewstate.MovieDetailsViewState
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mr_nbody16.moviewviewer.models.ConfigurationResponse
import com.mr_nbody16.moviewviewer.models.MovieDetailsResponse
import com.mr_nbody16.moviewviewer.view.adapters.CompaniesAdapter
import com.mr_nbody16_movieViwer_mvi.R
import com.mr_nbody16_movieViwer_mvi.databinding.FragmentMovieDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MovieDetailsFragment : BaseFragment() {


    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding: FragmentMovieDetailsBinding get() = _binding!!

    private lateinit var companiesAdapter: CompaniesAdapter
    private var movieId: Int = -1
    private val NA = "N/A"
    private var configuration: ConfigurationResponse? = null

    private val viewModel by viewModels<MovieDetailsViewModel> {
        MovieDetailsViewModel.factory(
            MovieDetailsFetchHelperImp(
                requireContext().resources.getString(
                    R.string.apiKey
                ), RetrofitClient.getClient().apis
            )
        )
    }

    override fun initData() {
        lifecycleScope.launch {
            viewModel.dispatchIntent(MovieDetailsViewIntent.Loading(movieId))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieDetailsBinding.inflate(layoutInflater, container, false)

        movieId = arguments?.getInt("movieId") ?: -1
        if (movieId == -1) {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(1000)
                lifecycleScope.launch(Dispatchers.Main) {
                    Snackbar.make(binding.root, "Invalid Arguments try again", Snackbar.LENGTH_LONG)
                        .show()
                    findNavController().popBackStack()
                }
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companiesAdapter = CompaniesAdapter(requireContext())

        val config: String? = arguments?.getString("config") ?: ""
        val gson = Gson()
        if (!config.isNullOrEmpty()) {
            try {
                configuration = gson.fromJson(config, ConfigurationResponse::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                configuration = null
            }
            companiesAdapter.setConfig(configuration)
        }

        lifecycleScope.launch {
            viewModel.state.collect {
                updateMainUiDetails(it)
            }
        }


        binding.backImageView.setOnClickListener {
            findNavController().popBackStack()
        }

        initData()
    }


    private fun updateMainUiDetails(state: MovieDetailsViewState?) {
        state?.let {
            when (it) {
                is MovieDetailsViewState.Loading -> {
                    binding.loadingIndicator.visibility = View.VISIBLE
                    binding.mainContainer.visibility = View.GONE
                }

                is MovieDetailsViewState.DataIsLoaded<*> -> {
                    lifecycleScope.launch(Dispatchers.Main) {
                        (it.data as MovieDetailsResponse).let { details ->
                            with(binding) {
                                titleTextView.text = details.title ?: NA
                                popularityValueTextView.text = details.popularity.toString() ?: NA
                                releaseDateTextView.text = details.release_date ?: NA
                                revenueTextView.text = "${details.revenue.toString() ?: NA} $"
                                runningTimeTextView.text = details.runtime.toString() ?: NA
                                statusTextView.text = details.status ?: NA
                                var genresTemp = ""
                                for (id in details?.genres ?: mutableListOf()) {

                                    if (genresTemp.isEmpty())
                                        genresTemp += id.name
                                    else
                                        genresTemp += " - " + id.name
                                }
                                if (genresTemp.isNullOrEmpty())
                                    genresTemp = NA
                                binding.genresTextView.text = genresTemp
                                var spokenTemp = ""
                                for (lan in details.spoken_languages ?: mutableListOf()) {
                                    if (spokenTemp.isEmpty())
                                        spokenTemp += lan.name
                                    else
                                        spokenTemp += " - ${lan.name}"
                                }
                                binding.spokenLanguagesTextView.text = spokenTemp
                                binding.taglineTextView.text = details.tagline ?: NA
                                binding.companiesRecycler.apply {
                                    adapter = companiesAdapter
                                    layoutManager = LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                }
                                companiesAdapter.setDetails(details.production_companies)
                                if (configuration != null) {
                                    Glide.with(requireContext())
                                        .load(
                                            (configuration?.imagesConfig?.base_url + configuration?.imagesConfig?.backdrop_sizes?.get(
                                                1
                                            ) + details.backdrop_path)
                                        ).error(R.drawable.warning)
                                        .placeholder(CircularProgressDrawable(requireContext()).apply {
                                            strokeWidth = 5f
                                            centerRadius = 30f
                                            start()
                                        })
                                        .into(binding.posterImageView)
                                } else
                                    binding.posterImageView.setImageDrawable(
                                        requireContext().getDrawable(
                                            R.drawable.warning_amber
                                        )
                                    )
                                binding.descriptionTextView.text = details.overview ?: NA
                                var productionCountries = ""
                                for (country in details?.production_countries ?: mutableListOf()) {
                                    if (productionCountries.isEmpty())
                                        productionCountries += country.name
                                    else
                                        productionCountries += " - ${country.name}"
                                }
                                binding.productionCountries.text = productionCountries
                                loadingIndicator.visibility = View.GONE
                                mainContainer.visibility = View.VISIBLE
                            }
                        }

                    }
                }

                is MovieDetailsViewState.Exception -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Warning!!!")
                        .setMessage("Could not load movie details : ${it.callError}")
                        .setPositiveButton("Ok") { dialog, i ->
                            dialog.dismiss()
                            findNavController().popBackStack()
                        }.create().show()
                }
            }
        }
    }


}