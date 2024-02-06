package ir.abolfazl.abolmovie.signupScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSignUpBinding
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity
import ir.abolfazl.abolmovie.utils.Extensions.showToast

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private val signUpScreenViewModel: SignUpScreenViewModel by viewModels()
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
        mainActivity().binding.bottomNavigation.visibility = View.INVISIBLE

        binding.btnSignUp.setOnClickListener {
            username = binding.edtUsername.text.toString()
            email = binding.edtEmail.text.toString()
            email.trim()
            password = binding.edtPassword.text.toString()
            confirmPassword = binding.edtConfirmPassword.text.toString()

            if (username.isNotEmpty() && username.length >= 3) {
                if (email.isNotEmpty() && isValidEmail(email)) {
                    if (password.isNotEmpty() && password.length >= 4) {
                        if (confirmPassword.isNotEmpty() && confirmPassword == password) {
                            signUpScreenViewModel.signUpUser(email, password, username)
                            binding.layoutSignIn.visibility = View.VISIBLE
                            if (signUpScreenViewModel.signupState) {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    binding.layoutSignIn.visibility = View.INVISIBLE
                                    findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentMain)
                                }, 1500)
                            } else {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    binding.layoutSignIn.visibility = View.INVISIBLE
                                    requireActivity().showToast("Signup not complete!")
                                },1500)
                            }
                        } else {
                            binding.txtError.text = "ConfirmPassword not equal to password"
                        }
                    } else {
                        binding.txtError.text = "Enter Password or longer than 4 word"
                    }
                } else {
                    binding.txtError.text = "Enter a valid Email"
                }
            } else {
                binding.txtError.text = "Enter a username or longer than 4 word"
            }
        }

        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentLogin)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(emailRegex.toRegex())
    }

}