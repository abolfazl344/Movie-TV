package ir.abolfazl.abolmovie.loginScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val fireAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var loginState = false
    var user = ""
    fun loginUser(email: String, password: String) {

        fireAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { it ->
                loginState = if (it.isSuccessful) {
                    getUsername(email)
                    true
                } else
                    false
            }
    }

    private fun getUsername(email: String) {
        db
            .collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { documents ->
                if (documents.isSuccessful) {
                    for (document in documents.result) {
                        val username = document.getString("username")
                        if (username != null) {
                            user = username
                        }
                    }
                }
            }
    }

}


