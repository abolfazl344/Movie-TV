package ir.abolfazl.abolmovie.serialScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.utils.Extensions.asyncRequest
import javax.inject.Inject

@HiltViewModel
class SerialScreenViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _discoverTv = MutableLiveData<Movie_Tv>()
    val discoverTv get() : LiveData<Movie_Tv> = _discoverTv
    fun discoverTv(page: Int) {
        mainRepository
            .discoverTv(page)
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {

                }

                override fun onSuccess(t: Movie_Tv) {
                    _discoverTv.postValue(t)
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}