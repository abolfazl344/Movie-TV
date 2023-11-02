package ir.abolfazl.abolmovie.serialScreen

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Serial

class SerialScreenViewModel(val mainRepository: MainRepository) {

    val progressBarSubjectTv = BehaviorSubject.create<Boolean>()

    fun discoverTv() : Single<Serial> {
        progressBarSubjectTv.onNext(true)
        return mainRepository.discoverSerial().doFinally { progressBarSubjectTv.onNext(false) }
    }
}