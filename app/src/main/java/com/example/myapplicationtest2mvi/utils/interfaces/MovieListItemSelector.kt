package com.example.myapplicationtest2mvi.utils.interfaces

interface MovieListItemSelector {
    fun onItemSelect(movieId : Int , config : String)
}