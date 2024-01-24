package ir.abolfazl.abolmovie.mainScreen

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
import ir.abolfazl.abolmovie.utils.Extensions.asyncRequest
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _popularMovies = MutableLiveData<Movie_Tv>()
    val popularMovie get() : LiveData<Movie_Tv> = _popularMovies

    private val _topMovies = MutableLiveData<Movie_Tv>()
    val topMovie get() : LiveData<Movie_Tv> = _topMovies

    private val _nowMovies = MutableLiveData<Movie_Tv>()
    val nowMovie get() : LiveData<Movie_Tv> = _nowMovies

    private val _topTv = MutableLiveData<Movie_Tv>()
    val topTv get() : LiveData<Movie_Tv> = _topTv

    private val _popularTv = MutableLiveData<Movie_Tv>()
    val popularTv get() : LiveData<Movie_Tv> = _popularTv

    fun getPopularMovie(page: Int) {
        mainRepository.getPopularMovie(page)
            .asyncRequest()
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

    fun getTopMovie(page: Int) {
        mainRepository.getTopMovie(page)
            .asyncRequest()
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

    fun getNowPlaying(page: Int) {

        mainRepository.getNowPlaying(page)
            .asyncRequest()
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

    fun getTopTv(page: Int) {
        mainRepository
            .getTopRatedTv(page)
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {

                }

                override fun onSuccess(t: Movie_Tv) {
                    _topTv.postValue(t)
                }

            })
    }

    fun getPopularTv(page: Int) {
        mainRepository
            .getPopularTv(page)
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {

                }

                override fun onSuccess(t: Movie_Tv) {
                    _popularTv.postValue(t)
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
