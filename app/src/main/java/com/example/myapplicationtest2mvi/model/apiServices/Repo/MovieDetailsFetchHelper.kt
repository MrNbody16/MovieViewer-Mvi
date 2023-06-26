package com.example.myapplicationtest2mvi.model.apiServices.Repo

import kotlinx.coroutines.flow.Flow
import com.example.myapplicationtest2mvi.model.apiServices.Result


interface MovieDetailsFetchHelper {


    fun getDetails(movieId: Int): Flow<Result>

    fun getMoviesCompanyDetails(companyId: Int): Flow<Result>


}