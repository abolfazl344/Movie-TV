package ir.abolfazl.abolmovie.movieScreen

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Movie

class MovieScreenViewModel(private val mainRepository: MainRepository) {

    val progressBarSubjectMovie = BehaviorSubject.create<Boolean>()

    fun discoverMovie(): Single<Movie> {
        progressBarSubjectMovie.onNext(true)
        return mainRepository.discoverMovie().doFinally { progressBarSubjectMovie.onNext(false) }
    }
}