package ir.abolfazl.abolmovie.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSignUpBinding
import ir.abolfazl.abolmovie.model.Local.UserInfo
import ir.abolfazl.abolmovie.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    @Inject
    lateinit var fireAuth : FirebaseAuth
    @Inject
    lateinit var db : FirebaseFirestore
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
    private var username = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSignUp.setOnClickListener {
            username = binding.edtUsername.text.toString()
            email = binding.edtEmail.text.toString()
            email.trim()
            password = binding.edtPassword.text.toString()
            confirmPassword = binding.edtConfirmPassword.text.toString()

            if(username.isNotEmpty() && username.length >= 3) {
                if (email.isNotEmpty() && isValidEmail(email)) {
                    if (password.isNotEmpty() && password.length >= 4) {
                        if (confirmPassword.isNotEmpty() && confirmPassword == password) {
                            signUpUser(email,password)
                        } else {
                            binding.txtError.text = "ConfirmPassword not equal to password"
                        }
                    } else {
                        binding.txtError.text = "Enter Password or longer than 4 word"
                    }
                } else {
                    binding.txtError.text = "Enter a valid Email"
                }
            }else{
                binding.txtError.text = "Enter a username or longer than 4 word"
            }
        }

        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentLogin)
        }
    }

    private fun signUpUser(email: String, password: String) {
        fireAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    addUserToDatabase(email,password,username)
                }else{
                    requireActivity().showToast("Sign up not completed")
                }

            }
    }

    private fun addUserToDatabase(email: String, password: String, username: String) {
        val userData = UserInfo(email,password,username)
        db
            .collection("users")
            .document(email)
            .set(userData)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val action = SignUpFragmentDirections.actionFragmentSignUpToFragmentMain(username)
                    findNavController().navigate(action)
                    requireActivity().showToast("data added to database")
                }

            }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(emailRegex.toRegex())
    }

}