package com.example.myapplicationtest2mvi.ui.viewmodels


import kotlinx.coroutines.flow.StateFlow

interface IViewModel<STATE , INTENT> {

    val state: StateFlow<STATE?>

    suspend fun dispatchIntent(intent : INTENT)

}