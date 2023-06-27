package com.mr_nbody16.moviewviewer.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class MovieListResponse(

    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val movieResponses: List<MovieResponse> = mutableListOf(),
    @SerializedName("total_pages")
    val total_pages: Int? = null,
    @SerializedName("total_results")
    val total_results: Int? = null

) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}