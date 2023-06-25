package com.example.myapplicationtest2mvi.model.apiServices.apiHelpers

import com.example.myapplicationtest2mvi.model.apiServices.Apis
import com.example.myapplicationtest2mvi.model.apiServices.CallErrors
import com.mr_nbody16.moviewviewer.models.CompanyResponse
import com.mr_nbody16.moviewviewer.models.MovieDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.myapplicationtest2mvi.model.apiServices.Result
import com.example.myapplicationtest2mvi.model.apiServices.applyCommonSideEffects

class MovieDetailsHelperImp(private val apiKey: String, private val apiService: Apis) :
    MovieDetailsHelper {


    override fun getDetails(movieId: Int): Flow<Result<MovieDetailsResponse>> = flow {
        apiService.getMovieDetails(movieId, apiKey).run {
            if (this.isSuccessful) {
                if (this.body() == null)
                    emit(Result.Error(CallErrors.ErrorEmptyData))
                else
                    emit(Result.Success(this.body()!!))
            } else
                emit(Result.Error(CallErrors.ErrorServer))
        }
    }.applyCommonSideEffects()

    override fun getMoviesCompanyDetails(companyId: Int): Flow<Result<CompanyResponse>> = flow {
        apiService.getCompanyInformation(companyId, apiKey).run {
            if (this.isSuccessful) {
                if (this.body() == null)
                    emit(Result.Error(CallErrors.ErrorEmptyData))
                else
                    emit(Result.Success(this.body()!!))
            } else
                emit(Result.Error(CallErrors.ErrorServer))
        }
    }.applyCommonSideEffects()
}