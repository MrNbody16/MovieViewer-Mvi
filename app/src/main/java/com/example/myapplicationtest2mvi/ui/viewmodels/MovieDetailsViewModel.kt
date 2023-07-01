package com.example.myapplicationtest2mvi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationtest2mvi.model.apiServices.MovieDetailsResult
import com.example.myapplicationtest2mvi.model.apiServices.Repo.MovieDetailsFetchHelperImp
import com.example.myapplicationtest2mvi.ui.viewactions.MovieDetailsActions
import com.example.myapplicationtest2mvi.ui.viewintents.MovieDetailsViewIntent
import com.example.myapplicationtest2mvi.ui.viewstate.MovieDetailsViewState

class MovieDetailsViewModel(private val movieDetailsHelper: MovieDetailsFetchHelperImp) :
    BaseViewModel<MovieDetailsViewIntent, MovieDetailsActions, MovieDetailsViewState>() {

    companion object {
        fun factory(movieDetailsHelper: MovieDetailsFetchHelperImp) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MovieDetailsViewModel(movieDetailsHelper) as T
                }
            }
    }

    override fun intentToAction(intent: MovieDetailsViewIntent): MovieDetailsActions {
        return when (intent) {
            is MovieDetailsViewIntent.Loading ->
                MovieDetailsActions.LoadMovieDetails(intent.movieId)
        }
    }

    override suspend fun handleAction(action: MovieDetailsActions) {
        runOnBack {
            when (action) {
                is MovieDetailsActions.LoadMovieDetails -> {
                    movieDetailsHelper.getDetails(action.movieId).collect {
                        _mState.emit((it as MovieDetailsResult).reduce())
                    }
                }
            }
        }
    }


}