package ir.abolfazl.abolmovie.mainScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import javax.inject.Inject


@HiltViewModel
class MainScreenViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _popularMovies = MutableLiveData<Movie_Tv>()
    val popularMovie get() : LiveData<Movie_Tv> = _popularMovies

    private val _topMovies = MutableLiveData<Movie_Tv>()
    val topMovie get() : LiveData<Movie_Tv> = _topMovies

    private val _nowMovies = MutableLiveData<Movie_Tv>()
    val nowMovie get() : LiveData<Movie_Tv> = _nowMovies

    fun getPopularMovie() {
        mainRepository.getPopularMovie()
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: Movie_Tv) {
                    _popularMovies.postValue(t)
                }

            })
    }

    fun getTopMovie() {
        mainRepository.getTopMovie()
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: Movie_Tv) {
                    _topMovies.postValue(t)
                }

            })
    }

    fun getNowPlaying() {

        mainRepository.getNowPlaying()
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.v("testLog", e.message.toString())
                }

                override fun onSuccess(t: Movie_Tv) {
                    _nowMovies.postValue(t)
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
