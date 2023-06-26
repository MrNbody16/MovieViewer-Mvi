package com.example.myapplicationtest2mvi.ui.viewstate

import com.example.myapplicationtest2mvi.model.apiServices.CallErrors

sealed class MovieListViewState : ViewState {

    object Loading : MovieListViewState()
    data class DataISLoaded<out T>(val data: T) : MovieListViewState()
    data class Exception(val callError: CallErrors) : MovieListViewState()

}