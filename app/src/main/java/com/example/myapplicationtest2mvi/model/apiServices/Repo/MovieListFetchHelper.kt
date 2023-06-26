package com.example.myapplicationtest2mvi.model.apiServices.Repo

import kotlinx.coroutines.flow.Flow
import com.example.myapplicationtest2mvi.model.apiServices.Result

interface MovieListFetchHelper {




    fun getMovieList(pageCounter : Int ) : Flow<Result>

    fun getConfiguration() : Flow<Result>

    fun getGenres() : Flow<Result>



}