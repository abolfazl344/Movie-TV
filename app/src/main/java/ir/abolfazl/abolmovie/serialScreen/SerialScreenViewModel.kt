package ir.abolfazl.abolmovie.serialScreen

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Movie_Tv

class SerialScreenViewModel(private val mainRepository: MainRepository) {

    val progressBarSubjectTv = BehaviorSubject.create<Boolean>()

    fun discoverTv() : Single<Movie_Tv> {
        progressBarSubjectTv.onNext(true)
        return mainRepository.discoverSerial().doFinally { progressBarSubjectTv.onNext(false) }
    }
}