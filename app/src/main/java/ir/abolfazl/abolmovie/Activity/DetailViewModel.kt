package ir.abolfazl.abolmovie.Activity


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.model.Local.Trailer
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.utils.Extensions.asyncRequest
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _trailer  = MutableLiveData<Trailer>()
    val trailer get() : LiveData<Trailer> = _trailer
    fun getMovieTrailer(movieID : Int){
        mainRepository
            .getMovieTrailer(movieID)
            .asyncRequest()
            .subscribe(object : SingleObserver<Trailer>{
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.v("errorTrailer",e.message!!)
                }

                override fun onSuccess(t: Trailer) {
                    _trailer.postValue(t)
                }

            })
    }

    fun getTvTrailer(serialID : Int){
        mainRepository
            .getTvTrailer(serialID)
            .asyncRequest()
            .subscribe(object : SingleObserver<Trailer>{
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.v("errorTrailer",e.message!!)
                }

                override fun onSuccess(t: Trailer) {
                    _trailer.postValue(t)
                }

            })
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}