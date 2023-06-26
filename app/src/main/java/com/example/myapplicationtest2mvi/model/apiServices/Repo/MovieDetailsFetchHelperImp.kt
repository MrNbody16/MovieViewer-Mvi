package com.example.myapplicationtest2mvi.model.apiServices.Repo

import com.example.myapplicationtest2mvi.model.apiServices.Apis
import com.example.myapplicationtest2mvi.model.apiServices.CallErrors
import com.example.myapplicationtest2mvi.model.apiServices.MovieDetailsResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.myapplicationtest2mvi.model.apiServices.Result
import com.example.myapplicationtest2mvi.model.apiServices.applyCommonSideEffects

/**
 * these ApiHelper classes acts like a DataSource it starts the online request and returns the result as flow
 * */
class MovieDetailsFetchHelperImp(private val apiKey: String, private val apiService: Apis) :
    MovieDetailsFetchHelper {


    override fun getDetails(movieId: Int): Flow<Result> = flow {
        apiService.getMovieDetails(movieId, apiKey).run {
            if (this.isSuccessful) {
                if (this.body() == null)
                    emit(MovieDetailsResult.Exception(CallErrors.ErrorEmptyData))
                else
                    emit(MovieDetailsResult.MovieDetailsSuccess(this.body()!!))
            } else
                emit(MovieDetailsResult.Exception(CallErrors.ErrorServer))
        }
    }.applyCommonSideEffects(MovieDetailsResult.Loading)

    override fun getMoviesCompanyDetails(companyId: Int): Flow<Result> = flow {
        apiService.getCompanyInformation(companyId, apiKey).run {
            if (this.isSuccessful) {
                if (this.body() == null)
                    emit(MovieDetailsResult.Exception(CallErrors.ErrorEmptyData))
                else
                    emit(MovieDetailsResult.MovieCompanyDetails(this.body()!!))
            } else
                emit(MovieDetailsResult.Exception(CallErrors.ErrorServer))
        }
    }.applyCommonSideEffects(MovieDetailsResult.Loading)
}