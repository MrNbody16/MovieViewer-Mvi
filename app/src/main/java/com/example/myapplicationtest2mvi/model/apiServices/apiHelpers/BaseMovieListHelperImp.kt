package com.example.myapplicationtest2mvi.model.apiServices.apiHelpers

import com.example.myapplicationtest2mvi.model.apiServices.Apis
import com.example.myapplicationtest2mvi.model.apiServices.CallErrors
import com.mr_nbody16.moviewviewer.models.ConfigurationResponse
import com.mr_nbody16.moviewviewer.models.MovieListResponse
import kotlinx.coroutines.flow.Flow
import com.example.myapplicationtest2mvi.model.apiServices.Result
import com.example.myapplicationtest2mvi.model.apiServices.applyCommonSideEffects
import kotlinx.coroutines.flow.flow

class BaseMovieListHelperImp(private val apiKey : String , private val apiServices : Apis) : BaseMovieListHelper  {

    override fun getMovieList(pageCounter: Int): Flow<Result<MovieListResponse>> = flow {
        apiServices.getMovieList(apiKey , pageCounter).run {
            if(this.isSuccessful) {
                if(this.body() == null)
                    emit(Result.Error(CallErrors.ErrorEmptyData))
                else
                    emit(Result.Success(this.body()!!))
            } else
                emit(Result.Error(CallErrors.ErrorServer))
        }
    }.applyCommonSideEffects()

    override fun getConfiguration(): Flow<Result<ConfigurationResponse>> = flow {
        apiServices.getConfiguration(apiKey).run {
            if(this.isSuccessful) {
                if(this.body() == null)
                    emit(Result.Error(CallErrors.ErrorEmptyData))
                else
                    emit(Result.Success(this.body()!!))
            } else
                emit(Result.Error(CallErrors.ErrorServer))
        }
    }.applyCommonSideEffects()

    override fun getGenres(): Flow<Result<HashMap<Int, String>>>  = flow {
        apiServices.getGenres(apiKey).run {
            if(this.isSuccessful){
                if(this.body() == null)
                    emit(Result.Error(CallErrors.ErrorEmptyData))
                else {
                    this.body()!!.genres?.let {
                        val result = hashMapOf<Int,String>()
                        for(genre in this.body()!!.genres!!) {
                            if(genre.id != null && genre.name != null)
                                result.put(genre.id , genre.name)
                        }
                        emit(Result.Success(result))
                    }
                }
            }
        }
    }.applyCommonSideEffects()


}