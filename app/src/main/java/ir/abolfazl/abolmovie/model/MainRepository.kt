package ir.abolfazl.abolmovie.model

import io.reactivex.Single
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.model.api.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {

    fun getPopularMovie() : Single<Movie_Tv>{

        return apiService.getPopularMovie()
    }
    fun getTopMovie() : Single<Movie_Tv>{

        return apiService.getTopMovie()
    }
    fun getNowPlaying() : Single<Movie_Tv>{

        return apiService.getNowPlayingMovie()
    }

    fun discoverMovie(page : Int) : Single<Movie_Tv>{

        return apiService.discoverMovie(page)
    }

    fun discoverSerial(page: Int) : Single<Movie_Tv>{

        return apiService.discoverSerial(page)
    }

    fun searchMovie(title : String) : Single<Movie_Tv>{

        return apiService.searchMovie(title)
    }

}