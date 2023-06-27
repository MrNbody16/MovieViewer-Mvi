package com.example.myapplicationtest2mvi.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationtest2mvi.model.apiServices.CallErrors
import com.mr_nbody16_movieViwer_mvi.R
import kotlin.reflect.KClass


fun <T : ViewModel> Fragment.viewModelProvider(
    factory: ViewModelProvider.Factory,
    model: KClass<T>
): T {
    return ViewModelProvider(this, factory).get(model.java)
}

fun Boolean.runIfTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}

fun CallErrors.getMessage(context: Context): String {
    return when (this) {
        is CallErrors.ErrorEmptyData -> context.getString(R.string.error_empty_data)
        is CallErrors.ErrorServer -> context.getString(R.string.error_server_error)
        is CallErrors.ErrorException ->  context.getString(
            R.string.error_exception
        )
        is CallErrors.CallerIsNull -> context.getString(R.string.nullApisRetro)
    }
}