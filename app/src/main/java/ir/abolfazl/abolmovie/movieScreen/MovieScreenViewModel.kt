package ir.abolfazl.abolmovie.movieScreen


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
class MovieScreenViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _discoverMovie = MutableLiveData<Movie_Tv>()
    val discoverMovie get() : LiveData<Movie_Tv> = _discoverMovie
    fun discoverMovie(page: Int) {
        mainRepository
            .discoverMovie(page)
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {

                }

                override fun onSuccess(t: Movie_Tv) {
                    _discoverMovie.postValue(t)
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}