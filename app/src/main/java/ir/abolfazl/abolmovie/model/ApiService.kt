package ir.abolfazl.abolmovie.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page:Int = 1) : Single<Movie>

    @GET("movie/top_rated")
    fun getTopMovie(@Query("page") page:Int = 1) : Single<Movie>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page:Int = 1) : Single<Movie>

    @GET("search/movie")
    fun searchMovie(@Query("query") query:String) : Single<Movie.Result>

    @GET("discover/movie")
    fun discoverMovie(@Query("page") page : Int = 1) : Single<Movie>

    @GET("discover/tv")
    fun discoverSerial(@Query("page") page : Int = 1) : Single<Serial>
}