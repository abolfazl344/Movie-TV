package ir.abolfazl.abolmovie.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    lateinit var binding : FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentMain)
        }

        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentSignUp_to_fragmentLogin)
        }
    }

}