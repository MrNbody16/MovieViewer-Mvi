package com.example.myapplicationtest2mvi.model.apiServices.apiHelpers

import com.mr_nbody16.moviewviewer.models.CompanyResponse
import com.mr_nbody16.moviewviewer.models.MovieDetailsResponse
import kotlinx.coroutines.flow.Flow
import com.example.myapplicationtest2mvi.model.apiServices.Result


interface MovieDetailsHelper {


    fun getDetails(movieId: Int): Flow<Result<MovieDetailsResponse>>

    fun getMoviesCompanyDetails(companyId: Int): Flow<Result<CompanyResponse>>


}