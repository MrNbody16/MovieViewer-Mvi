package com.example.myapplicationtest2mvi.model.apiServices.Repo

import com.example.myapplicationtest2mvi.model.apiServices.Apis
import com.example.myapplicationtest2mvi.model.apiServices.CallErrors
import com.example.myapplicationtest2mvi.model.apiServices.MovieListResult
import kotlinx.coroutines.flow.Flow
import com.example.myapplicationtest2mvi.model.apiServices.Result
import com.example.myapplicationtest2mvi.model.apiServices.applyCommonSideEffects
import kotlinx.coroutines.flow.flow

/**
 * these ApiHelper classes acts like a DataSource it starts the online request and returns the result as flow
 * */
class MovieListFetchHelperImp(private val apiKey: String, private val apiServices: Apis?) :
    MovieListFetchHelper {

    override fun getMovieList(pageCounter: Int): Flow<Result> = flow {
        if (apiServices == null)
            emit(MovieListResult.Exception(CallErrors.CallerIsNull))
        else {


            apiServices.getMovieList(apiKey, pageCounter).run {
                if (this.isSuccessful) {
                    if (this.body() == null)
                        emit(MovieListResult.Exception(CallErrors.ErrorEmptyData))
                    else
                        emit(MovieListResult.MovieListSuccess(this.body()!!))
                } else
                    emit(MovieListResult.Exception(CallErrors.ErrorServer))
            }
        }
    }.applyCommonSideEffects(MovieListResult.Loading)


    override fun getConfiguration(): Flow<Result> = flow {
        if (apiServices == null)
            emit(MovieListResult.Exception(CallErrors.CallerIsNull))
        else {
            apiServices.getConfiguration(apiKey).run {
                if (this.isSuccessful) {
                    if (this.body() == null)
                        emit(MovieListResult.Exception(CallErrors.ErrorEmptyData))
                    else
                        emit(MovieListResult.ConfigSuccess(this.body()!!))
                } else
                    emit(MovieListResult.Exception(CallErrors.ErrorServer))
            }
        }
    }.applyCommonSideEffects(MovieListResult.Loading)

    override fun getGenres(): Flow<Result> = flow {
        if (apiServices == null)
            emit(MovieListResult.Exception(CallErrors.CallerIsNull))
        else {
            apiServices.getGenres(apiKey).run {
                if (this.isSuccessful) {
                    if (this.body() == null)
                        emit(MovieListResult.Exception(CallErrors.ErrorEmptyData))
                    else {
                        this.body()!!.genres?.let {
                            val result = hashMapOf<Int, String>()
                            for (genre in this.body()!!.genres!!) {
                                if (genre.id != null && genre.name != null)
                                    result.put(genre.id, genre.name)
                            }
                            emit(MovieListResult.GenresSuccess(result))
                        }
                    }
                }
            }
        }
    }.applyCommonSideEffects(MovieListResult.Loading)


}