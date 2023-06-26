package com.example.myapplicationtest2mvi.ui.viewstate

import com.example.myapplicationtest2mvi.model.apiServices.CallErrors

sealed class MovieDetailsViewState : ViewState {

    object Loading : MovieDetailsViewState()

    data class DataIsLoaded<out T>(val data : T) : MovieDetailsViewState()

    data class Exception(val callError : CallErrors) : MovieDetailsViewState()

}