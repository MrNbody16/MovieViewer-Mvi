package com.mr_nbody16.moviewviewer.view.adapters

import android.content.Context
import android.provider.SyncStateContract.Constants
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.myapplicationtest2mvi.utils.interfaces.MovieListItemSelector
import com.google.gson.Gson
import com.mr_nbody16.moviewviewer.models.ConfigurationResponse
import com.mr_nbody16.moviewviewer.models.GenresResponse
import com.mr_nbody16.moviewviewer.models.MovieListResponse
import com.mr_nbody16_movieViwer_mvi.R
import com.mr_nbody16_movieViwer_mvi.databinding.MovieListItemModelBinding

class MovieListAdapter(
    private val context: Context,
    private val itemSelector: MovieListItemSelector
) :
    RecyclerView.Adapter<MovieListAdapter.MyViewHolder>() {

    class MyViewHolder(val holderBinding: MovieListItemModelBinding) :
        RecyclerView.ViewHolder(holderBinding.root)


    private var movieList: MovieListResponse? = null
    private var configs: ConfigurationResponse? = null
    private var genreList: HashMap<Int, String>? = null
    private var genresTemp = ""
    private var loadingIndicator: CircularProgressDrawable =
        CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }


    fun setMovieList(movieList: MovieListResponse?) {
        this.movieList = movieList
        Log.i(com.example.myapplicationtest2mvi.utils.Constants.TAG , "MovieList received : $movieList")
        notifyDataSetChanged()
    }

    fun setConfiguration(config: ConfigurationResponse?) {
        this.configs = config
        Log.i(com.example.myapplicationtest2mvi.utils.Constants.TAG , "Config received : $config")
        notifyDataSetChanged()
    }

    fun setGenresList(genreList: HashMap<Int, String>?) {
        this.genreList = genreList
        Log.i(com.example.myapplicationtest2mvi.utils.Constants.TAG , "Genres received : $genreList")
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MovieListItemModelBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = movieList?.movieResponses?.size ?: 0


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(movieList?.movieResponses?.get(position)) {
            holder.holderBinding.also { binding ->

                binding.movieTitle.text = this?.title ?: ""
                binding.releaseDate.text = this?.release_date ?: ""
                binding.movieLanguageTxt.text = this?.original_language ?: ""
                if (configs != null) {
                    val x = (configs?.imagesConfig?.base_url + configs?.imagesConfig?.backdrop_sizes?.get(
                        1
                    ) + this?.backdrop_path)
                    Log.i(com.example.myapplicationtest2mvi.utils.Constants.TAG , "Log Url : $x")
                    Glide.with(context)
                        .load(
                            x
                        )
                        .placeholder(loadingIndicator)
                        .error(R.drawable.warning)
                        .into(binding.moviePosterImg)
                } else {
                    binding.moviePosterImg.setImageDrawable(context.getDrawable(R.drawable.warning))
                }
                if (!genreList.isNullOrEmpty() && !this?.genre_ids.isNullOrEmpty()) {
                    genresTemp = ""
                    for (id in this!!.genre_ids!!) {
                        if (genreList!!.containsKey(id)) {
                            if (genresTemp.isEmpty())
                                genresTemp += genreList!![id]
                            else
                                genresTemp += " - " + genreList!![id]
                        }
                    }
                    binding.genreTxt.text = genresTemp
                }
                binding.itemModelParent.setOnClickListener {
                    itemSelector.onItemSelect(this?.id ?: 0, Gson().toJson(configs ?: ""))
                }
            }
        }
    }


}