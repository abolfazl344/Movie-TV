package ir.abolfazl.abolmovie.searchScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.utils.asyncRequest
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _searchMovies = MutableLiveData<Movie_Tv>()
    val searchMovies get() : LiveData<Movie_Tv> = _searchMovies

    fun searchMovie(title: String) {

        mainRepository
            .searchMovie(title)
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: Movie_Tv) {
                    _searchMovies.postValue(t)
                }

                override fun onError(e: Throwable) {
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}