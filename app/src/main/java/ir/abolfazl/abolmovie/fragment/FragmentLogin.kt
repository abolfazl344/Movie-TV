package ir.abolfazl.abolmovie.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentLoginBinding
import ir.abolfazl.abolmovie.utils.mainActivity
import ir.abolfazl.abolmovie.utils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class FragmentLogin : Fragment() {
    lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var fireAuth: FirebaseAuth

    @Inject
    lateinit var db: FirebaseFirestore
    private var email = ""
    private var password = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainActivity().binding.bottomNavigation.visibility = View.INVISIBLE

        binding.btnLogin.setOnClickListener {
            email = binding.edtEmailLogIn.text.toString()
            password = binding.edtPasswordLogIn.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                binding.txtError.visibility = View.VISIBLE
                binding.txtError.text = "Enter Email and Password"
            }
        }

        binding.txtSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLogin_to_signUpFragment)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loginUser(email: String, password: String) {

        fireAuth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {

                } else {
                    binding.txtError.visibility = View.VISIBLE
                    binding.txtError.text = "Email or Password is incorrect"
                }
            }
            .addOnCanceledListener {

            }
    }

}