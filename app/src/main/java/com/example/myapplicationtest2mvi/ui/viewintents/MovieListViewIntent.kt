package com.example.myapplicationtest2mvi.ui.viewintents

sealed class MovieListViewIntent : ViewIntents {

    data class LoadAllData(val page : Int) : MovieListViewIntent()

    data class LoadPage(val page : Int) : MovieListViewIntent()

    object LoadConfig : MovieListViewIntent()

    object LoadGenres : MovieListViewIntent()

    data class Go2DetailsIntent(val movieId : Int) : MovieListViewIntent()

}