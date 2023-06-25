package com.example.myapplicationtest2mvi.model.apiServices



object ApiServicesUtils {


    const val MAX_RETIRES = 3L
    // a static value that we'll use it to calculate api request retry delay
    private const val INITIAL_BACKOFF = 2000L


    fun getBackOffDelay(attempt : Long) = INITIAL_BACKOFF * (attempt + 1)



}