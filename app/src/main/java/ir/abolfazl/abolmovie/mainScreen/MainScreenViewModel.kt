package ir.abolfazl.abolmovie.mainScreen

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Movie

class MainScreenViewModel(private val mainRepository: MainRepository) {

    val progressBarSubjectPop = BehaviorSubject.create<Boolean>()
    val progressBarSubjectTop = BehaviorSubject.create<Boolean>()
    val progressBarSubjectNow = BehaviorSubject.create<Boolean>()

    fun getPopularMovie(): Single<Movie> {
        progressBarSubjectPop.onNext(true)
        return mainRepository.getPopularMovie().doFinally { progressBarSubjectPop.onNext(false) }
    }

    fun getTopMovie(): Single<Movie> {
        progressBarSubjectTop.onNext(true)
        return mainRepository.getTopMovie().doFinally { progressBarSubjectTop.onNext(false) }
    }

    fun getNowPlaying(): Single<Movie> {
        progressBarSubjectNow.onNext(true)
        return mainRepository.getNowPlaying().doFinally { progressBarSubjectNow.onNext(false) }
    }
}