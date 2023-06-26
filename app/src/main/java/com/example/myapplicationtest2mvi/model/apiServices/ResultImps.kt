package com.example.myapplicationtest2mvi.model.apiServices

import com.example.myapplicationtest2mvi.ui.viewstate.MovieDetailsViewState
import com.example.myapplicationtest2mvi.ui.viewstate.MovieListViewState
import com.mr_nbody16.moviewviewer.models.CompanyResponse
import com.mr_nbody16.moviewviewer.models.ConfigurationResponse
import com.mr_nbody16.moviewviewer.models.GenresResponse
import com.mr_nbody16.moviewviewer.models.MovieDetailsResponse
import com.mr_nbody16.moviewviewer.models.MovieListResponse
import java.util.WeakHashMap


sealed class MovieListResult : Result {

    object Loading : MovieListResult()

    data class Exception(val callError: CallErrors) : MovieListResult()

    data class MovieListSuccess(val data: MovieListResponse) : MovieListResult()

    data class ConfigSuccess(val data: ConfigurationResponse) : MovieListResult()

    data class GenresSuccess(val data: HashMap<Int, String>) : MovieListResult()

    fun reduce(): MovieListViewState {
        return when (this) {
            is Exception -> MovieListViewState.Exception(callError)
            is Loading -> MovieListViewState.Loading
            is MovieListSuccess -> MovieListViewState.DataISLoaded(data)
            is GenresSuccess -> MovieListViewState.DataISLoaded(data)
            is ConfigSuccess -> MovieListViewState.DataISLoaded(data)
        }
    }

}


sealed class MovieDetailsResult : Result {

    object Loading : MovieDetailsResult()

    data class Exception(val callError: CallErrors) : MovieDetailsResult()

    data class MovieDetailsSuccess(val data: MovieDetailsResponse) : MovieDetailsResult()

    data class MovieCompanyDetails(val data: CompanyResponse) : MovieDetailsResult()

    fun reduce(): MovieDetailsViewState {
        return when (this) {
            is Exception -> MovieDetailsViewState.Exception(callError)
            is Loading -> MovieDetailsViewState.Loading
            is MovieDetailsSuccess -> MovieDetailsViewState.DataIsLoaded(data)
            is MovieCompanyDetails -> MovieDetailsViewState.DataIsLoaded(data)
        }
    }

}
