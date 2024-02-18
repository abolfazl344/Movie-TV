package ir.abolfazl.abolmovie.signupScreen

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val fireAuth: FirebaseAuth
) : ViewModel() {

    var signupState = false
    var exceptionSignUp = ""

    @SuppressLint("SuspiciousIndentation")
    fun signUpUser(email: String, password: String, username: String) {
        fireAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val profileUser = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                fireAuth.currentUser!!.updateProfile(profileUser)
                signupState = true
            }
            .addOnFailureListener {
                exceptionSignUp = it.message.toString()
            }
    }
}