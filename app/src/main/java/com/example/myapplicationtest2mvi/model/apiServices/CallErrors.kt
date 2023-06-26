package com.example.myapplicationtest2mvi.model.apiServices

sealed class CallErrors {

    object ErrorEmptyData : CallErrors()
    object ErrorServer : CallErrors()
    data class ErrorException(val throwable : Throwable) : CallErrors()

    object CallerIsNull : CallErrors()

}