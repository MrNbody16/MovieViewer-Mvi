package com.example.myapplicationtest2mvi.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.example.myapplicationtest2mvi.model.apiServices.MovieListResult
import com.example.myapplicationtest2mvi.model.apiServices.Repo.MovieListFetchHelperImp
import com.example.myapplicationtest2mvi.ui.viewactions.MovieListActions
import com.example.myapplicationtest2mvi.ui.viewintents.MovieListViewIntent
import com.example.myapplicationtest2mvi.ui.viewstate.MovieListViewState
import com.example.myapplicationtest2mvi.ui.viewstate.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MovieListViewModel(private val movieListHelper: MovieListFetchHelperImp) :
    BaseViewModel<MovieListViewIntent, MovieListActions, MovieListViewState>() {


    private val _mConfigState = MutableStateFlow<ViewState>(MovieListViewState.Loading)
    val configState: StateFlow<ViewState>
        get() {
            return _mConfigState
        }

    private val _mGenresState = MutableStateFlow<ViewState>(MovieListViewState.Loading)
    val genresState: StateFlow<ViewState>
        get() {
            return _mGenresState
        }

    override fun intentToAction(intent: MovieListViewIntent): MovieListActions {
        return when (intent) {
            is MovieListViewIntent.Loading -> MovieListActions.LoadingAllMovies(intent.page)
            is MovieListViewIntent.Go2DetailsIntent -> MovieListActions.Go2Details(intent.movieId)
        }
    }

    override suspend fun handleAction(action: MovieListActions) {
        runOnBack {
            when (action) {
                is MovieListActions.LoadingAllMovies -> {

                    viewModelScope.launch(Dispatchers.IO) {
                        movieListHelper.getConfiguration().collect {
                            _mConfigState.emit((it as MovieListResult).reduce())
                        }
                    }
                    viewModelScope.launch(Dispatchers.IO) {}
                    movieListHelper.getGenres().collect {
                        _mGenresState.emit((it as MovieListResult).reduce())
                    }
                    viewModelScope.launch(Dispatchers.IO) {
                        movieListHelper.getMovieList(action.page).collect {
                            mState.emit((it as MovieListResult).reduce())
                        }
                    }
                }
                is MovieListActions.Go2Details ->
                    mState.emit(MovieListViewState.DataISLoaded(action.data))
            }
        }
    }




}