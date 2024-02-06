package ir.abolfazl.abolmovie.model.api

import io.reactivex.Single
import ir.abolfazl.abolmovie.model.Local.Credits
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.model.Local.Trailer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page:Int = 1) : Single<Movie_Tv>

    @GET("movie/top_rated")
    fun getTopMovie(@Query("page") page:Int = 1) : Single<Movie_Tv>

    @GET("movie/now_playing")
    fun getNowPlayingMovie(@Query("page") page:Int = 1) : Single<Movie_Tv>

    @GET("discover/movie")
    fun discoverMovie(@Query("page") page : Int = 1) : Single<Movie_Tv>

    @GET("discover/tv")
    fun discoverSerial(@Query("page") page : Int = 1) : Single<Movie_Tv>

    @GET("movie/{movie_id}/videos")
    fun getMovieTrailer(@Path("movie_id") movie_id : Int) : Single<Trailer>

    @GET("tv/{series_id}/videos")
    fun getTvTrailer(@Path("series_id") series_id : Int) : Single<Trailer>

    @GET("search/movie")
    fun searchMovie(@Query("query") query:String) : Single<Movie_Tv>

    @GET("tv/top_rated")
    fun getTopRatedTv(@Query("page") page:Int = 1) : Single<Movie_Tv>

    @GET("tv/popular")
    fun getPopularTv(@Query("page") page:Int = 1) : Single<Movie_Tv>

    @GET("movie/{movie_id}/credits")
    fun getCreditsMovie(@Path("movie_id") movie_id : Int) : Single<Credits>

    @GET("tv/{series_id}/credits")
    fun getCreditsTv(@Path("series_id") series_id : Int) : Single<Credits>
}