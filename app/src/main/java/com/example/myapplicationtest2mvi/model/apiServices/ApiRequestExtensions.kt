package com.example.myapplicationtest2mvi.model.apiServices

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

/**
 * This method watches and retries api requests .
 * when procedure fails it checks attempts count and the cause of failure
 * then it might retry or just update state with an error
* */
fun <T:Any> Flow<Result<T>>.applyCommonSideEffects() =
    retryWhen { cause , attempt ->
        when {
            (cause is IOException && attempt < ApiServicesUtils.MAX_RETIRES) -> {
                delay(ApiServicesUtils.getBackOffDelay(attempt))
                true
            }
            else -> {
                false
            }
        }
    }.onStart {
        emit(Result.Loading)
    }.catch {
        emit(Result.Error(CallErrors.ErrorException(it)))
    }
