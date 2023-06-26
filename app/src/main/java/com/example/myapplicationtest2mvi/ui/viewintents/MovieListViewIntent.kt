package com.example.myapplicationtest2mvi.ui.viewintents

sealed class MovieListViewIntent : ViewIntents {

    data class Loading(val page : Int) : MovieListViewIntent()

    data class Go2DetailsIntent(val movieId : Int) : MovieListViewIntent()

}