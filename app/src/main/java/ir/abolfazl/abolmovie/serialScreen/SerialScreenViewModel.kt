package ir.abolfazl.abolmovie.serialScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import javax.inject.Inject

@HiltViewModel
class SerialScreenViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {
    fun discoverTv(page: Int): Single<Movie_Tv> {
        return mainRepository.discoverSerial(page)
    }
}