package com.example.myapplicationtest2mvi.ui.viewintents

sealed class MovieDetailsViewIntent : ViewIntents {

    data class Loading(val movieId : Int) : MovieDetailsViewIntent()



}