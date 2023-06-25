package com.example.myapplicationtest2mvi.model.apiServices

sealed class Result<out T> {

    // we use this when our api request is Successful and we have fetched our required data
    // this entity is type safe and can be used with any type of data
    data class Success<out T>(val data : T) : Result<T>()

    // in case of any errors in our online request we will fill an object of this type
    data class Error(val exception : CallErrors) : Result<Nothing>()

    // this loading object will be used when we want to inform our ui that we need a loading dialog or something like this
    object Loading : Result<Nothing>()


}