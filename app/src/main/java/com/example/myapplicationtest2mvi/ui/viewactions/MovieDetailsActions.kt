package com.example.myapplicationtest2mvi.ui.viewactions

sealed class MovieDetailsActions : ViewAction {
    
   data class LoadMovieDetails(val movieId: Int) : MovieDetailsActions()

}