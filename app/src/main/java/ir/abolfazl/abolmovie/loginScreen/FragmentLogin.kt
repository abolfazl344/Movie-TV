package ir.abolfazl.abolmovie.loginScreen

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
import ir.abolfazl.abolmovie.databinding.FragmentLoginBinding
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity

@AndroidEntryPoint
class FragmentLogin : Fragment() {
    lateinit var binding: FragmentLoginBinding
    private val loginScreenViewModel: LoginScreenViewModel by viewModels()
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
            loginUser(email, password)
        }

        binding.txtSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLogin_to_signUpFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            loginScreenViewModel.loginUser(email, password)
            binding.layoutSignIn.visibility = View.VISIBLE
            if (loginScreenViewModel.loginState) {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.layoutSignIn.visibility = View.INVISIBLE
                    findNavController().navigate(R.id.action_fragmentLogin_to_fragmentMain)
                }, 1500)
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.layoutSignIn.visibility = View.INVISIBLE
                    binding.txtError.visibility = View.VISIBLE
                    binding.txtError.text = "Email or Password is incorrect"
                }, 1500)
            }

        } else {
            binding.layoutSignIn.visibility = View.INVISIBLE
            binding.txtError.visibility = View.VISIBLE
            binding.txtError.text = "Enter Email and Password"
        }
    }
}