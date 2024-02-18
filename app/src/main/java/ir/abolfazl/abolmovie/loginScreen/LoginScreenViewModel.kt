package ir.abolfazl.abolmovie.loginScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val fireAuth: FirebaseAuth
) : ViewModel() {

    var loginState = false
    var exceptionLogin = ""
    fun loginUser(email: String, password: String) {

        fireAuth
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                loginState = true
            }
            .addOnFailureListener {
                exceptionLogin = it.message!!
            }

    }
}


