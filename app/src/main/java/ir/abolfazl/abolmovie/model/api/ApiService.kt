package ir.abolfazl.abolmovie.model.api

import androidx.lifecycle.LiveData
import io.reactivex.Single
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page:Int = 1) : Single<Movie_Tv>

    @GET("movie/top_rated")
    fun getTopMovie(@Query("page") page:Int = 1) : Single<Movie_Tv>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page:Int = 1) : Single<Movie_Tv>

    @GET("search/movie")
    fun searchMovie(@Query("query") query:String) : Single<Movie_Tv>

    @GET("discover/movie")
    fun discoverMovie(@Query("page") page : Int = 1) : Single<Movie_Tv>

    @GET("discover/tv")
    fun discoverSerial(@Query("page") page : Int = 1) : Single<Movie_Tv>
}