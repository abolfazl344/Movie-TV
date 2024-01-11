package ir.abolfazl.abolmovie.signupScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.abolfazl.abolmovie.model.Local.UserInfo
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val fireAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    var signupState = false
    fun signUpUser(email: String, password: String, username: String) {
        fireAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    addUserToDatabase(email, password, username)
                }
            }
    }

    private fun addUserToDatabase(email: String, password: String, username: String) {
        val userData = UserInfo(email, password, username)
        db
            .collection("users")
            .document(email)
            .set(userData)
            .addOnCompleteListener {
                signupState = it.isSuccessful
            }
    }
}