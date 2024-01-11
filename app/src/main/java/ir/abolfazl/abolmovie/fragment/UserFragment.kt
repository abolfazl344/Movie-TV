package ir.abolfazl.abolmovie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Activity.MainActivity
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentUserBinding
import ir.abolfazl.abolmovie.utils.mainActivity
import ir.abolfazl.abolmovie.utils.showToast
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
        binding.btnLogout.setOnClickListener {
            fireAuth.signOut()
            findNavController().navigate(R.id.action_userFragment_to_fragmentLogin)
            requireActivity().showToast("SignOut is successfully")
        }
    }

}