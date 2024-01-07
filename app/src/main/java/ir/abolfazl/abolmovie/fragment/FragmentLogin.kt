package ir.abolfazl.abolmovie.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentLoginBinding
import ir.abolfazl.abolmovie.utils.mainActivity

class FragmentLogin : Fragment() {
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainActivity().binding.bottomNavigation.visibility = View.INVISIBLE

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.fragmentMain)
        }

        val data : Pair<String, String>? = requireArguments().getSerializable("email_password") as? Pair<String, String>

        binding.txtSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLogin_to_signUpFragment)
        }
    }

}