package com.example.myapplicationtest2mvi.ui.viewactions

sealed class MovieListActions: ViewAction {

    data class  LoadingAllMovies(val page : Int) : MovieListActions()

    data class Go2Details(val data : Int) : MovieListActions()



}