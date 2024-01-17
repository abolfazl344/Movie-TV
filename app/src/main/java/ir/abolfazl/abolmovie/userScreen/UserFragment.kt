package ir.abolfazl.abolmovie.userScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentUserBinding
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity
import ir.abolfazl.abolmovie.utils.Extensions.showToast
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : Fragment() {
    lateinit var binding : FragmentUserBinding
    @Inject
    lateinit var fireAuth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val username = fireAuth.currentUser?.displayName
        val email = fireAuth.currentUser?.email

        mainActivity().binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btn_Home_menu -> {
                    findNavController().navigate(R.id.action_userFragment_to_fragmentMain)
                }
                R.id.btn_Movie_menu -> {
                    findNavController().navigate(R.id.action_userFragment_to_fragmentMovie)
                }
                R.id.btn_TV_menu -> {
                    findNavController().navigate(R.id.action_userFragment_to_fragmentSerial)
                }

                R.id.btn_search_menu ->{
                    findNavController().navigate(R.id.action_userFragment_to_searchFragment)
                }
            }
            true
        }

        if(email!!.isNotEmpty() && username!!.isNotEmpty()){
            binding.txtEmail.text = email
            binding.txtUsername.text = username
        }else
            requireActivity().showToast("error")

        binding.btnLogout.setOnClickListener {
            fireAuth.signOut()
            findNavController().navigate(R.id.action_userFragment_to_fragmentLogin)
            requireActivity().showToast("SignOut is successfully")
        }
    }

}