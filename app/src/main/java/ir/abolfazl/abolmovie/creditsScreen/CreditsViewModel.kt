package ir.abolfazl.abolmovie.creditsScreen


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.model.Local.Person
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.utils.Extensions.asyncRequest
import javax.inject.Inject

@HiltViewModel
class CreditsViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _person = MutableLiveData<Person>()
    val person get() : LiveData<Person> = _person

    private val _credits = MutableLiveData<Movie_Tv>()
    val credits get() : LiveData<Movie_Tv> = _credits

    fun getPersonDetail(personID: Int) {
        mainRepository
            .getPerson(personID)
            .asyncRequest()
            .subscribe(object : SingleObserver<Person> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    Log.v("errorTrailer", e.message!!)
                }

                override fun onSuccess(t: Person) {
                    _person.postValue(t)
                }

            })
    }

    fun getPersonCredits(personID: Int){
        mainRepository
            .getPersonCredits(personID)
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv>{
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {

                }

                override fun onSuccess(t: Movie_Tv) {
                    _credits.postValue(t)
                }

            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}