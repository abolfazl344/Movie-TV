package ir.abolfazl.abolmovie.searchScreen

import io.reactivex.Single
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Movie_Tv

class SearchScreenViewModel(private val mainRepository: MainRepository) {

    fun searchMovie(title : String) : Single<Movie_Tv>{

        return mainRepository.searchMovie(title)
    }
}