package ir.abolfazl.abolmovie.serialScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.utils.asyncRequest
import javax.inject.Inject

@HiltViewModel
class SerialScreenViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _discoverSerial = MutableLiveData<Movie_Tv>()
    val discoverSerial get() : LiveData<Movie_Tv> = _discoverSerial
    fun discoverTv() {
        mainRepository
            .discoverSerial()
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.v("errorDiscoverSerial",e.message.toString())
                }

                override fun onSuccess(t: Movie_Tv) {
                    _discoverSerial.postValue(t)
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}