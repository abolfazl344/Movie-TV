package ir.abolfazl.abolmovie.model

import io.reactivex.Single
import ir.abolfazl.abolmovie.model.Local.Credits
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.model.Local.Trailer
import ir.abolfazl.abolmovie.model.api.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiService) {

    fun getPopularMovie(page: Int) : Single<Movie_Tv>{

        return apiService.getPopularMovie(page)
    }
    fun getTopMovie(page: Int) : Single<Movie_Tv>{

        return apiService.getTopMovie(page)
    }
    fun getNowPlaying(page: Int) : Single<Movie_Tv>{

        return apiService.getNowPlayingMovie(page)
    }

    fun getTopRatedTv(page: Int) : Single<Movie_Tv>{

        return apiService.getTopRatedTv(page)
    }

    fun getPopularTv(page: Int) : Single<Movie_Tv>{

        return apiService.getPopularTv(page)
    }
    fun discoverMovie(page : Int) : Single<Movie_Tv>{

        return apiService.discoverMovie(page)
    }

    fun discoverTv(page: Int) : Single<Movie_Tv>{

        return apiService.discoverSerial(page)
    }

    fun getMovieTrailer(movieID : Int) : Single<Trailer>{

        return apiService.getMovieTrailer(movieID)
    }

    fun getTvTrailer(serialID : Int) : Single<Trailer>{

        return apiService.getTvTrailer(serialID)
    }

    fun searchMovie(title : String) : Single<Movie_Tv>{

        return apiService.searchMovie(title)
    }

    fun getCreditsMovie(movieID: Int) : Single<Credits>{

        return apiService.getCreditsMovie(movieID)
    }

    fun getCreditsTv(seriesID: Int) : Single<Credits>{

        return apiService.getCreditsTv(seriesID)
    }
}