package ir.abolfazl.abolmovie.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ir.abolfazl.abolmovie.MainActivity.MainActivity

fun Fragment.mainActivity(): MainActivity {
    return requireActivity() as MainActivity
}
fun Context.showToast(text : String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun <T> Single<T>.asyncRequest() : Single<T>{

    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}
