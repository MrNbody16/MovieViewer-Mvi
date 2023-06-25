package com.example.myapplicationtest2mvi.model.apiServices.apiHelpers

import com.mr_nbody16.moviewviewer.models.ConfigurationResponse
import com.mr_nbody16.moviewviewer.models.MovieListResponse
import kotlinx.coroutines.flow.Flow
import com.example.myapplicationtest2mvi.model.apiServices.Result

interface BaseMovieListHelper {




    fun getMovieList(pageCounter : Int ) : Flow<Result<MovieListResponse>>

    fun getConfiguration() : Flow<Result<ConfigurationResponse>>

    fun getGenres() : Flow<Result<HashMap<Int,String>>>



}