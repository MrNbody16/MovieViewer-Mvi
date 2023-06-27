package com.example.myapplicationtest2mvi.ui.viewactions

import com.example.myapplicationtest2mvi.model.entities.Go2DetailsHolder

sealed class MovieListActions: ViewAction {

    data class  LoadingAllMovies(val page : Int) : MovieListActions()

    data class LoadPage(val page : Int) : MovieListActions()

    object LoadConfig : MovieListActions()

    object LoadGenres : MovieListActions()

    data class Go2Details(val data : Go2DetailsHolder) : MovieListActions()



}