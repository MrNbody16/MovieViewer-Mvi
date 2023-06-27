package com.example.myapplicationtest2mvi.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplicationtest2mvi.ui.viewactions.ViewAction
import com.example.myapplicationtest2mvi.ui.viewintents.ViewIntents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.myapplicationtest2mvi.ui.viewstate.ViewState

abstract class BaseViewModel<INTENT : ViewIntents , ACTION : ViewAction , STATE : ViewState>() : ViewModel(), IViewModel<STATE , INTENT> {

    protected val _mState = MutableStateFlow<STATE?>(null)
    override val state: StateFlow<STATE?>
        get() {
            return _mState
        }

    fun runOnBack(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { block() }
    }


    abstract fun intentToAction(intent: INTENT): ACTION

    abstract suspend fun handleAction(action: ACTION)

    override suspend fun dispatchIntent(intent: INTENT) {
        handleAction(intentToAction(intent))
    }
}