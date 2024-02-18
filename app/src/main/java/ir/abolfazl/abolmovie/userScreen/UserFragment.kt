package ir.abolfazl.abolmovie.userScreen


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentUserBinding
import ir.abolfazl.abolmovie.loginScreen.FragmentLogin
import ir.abolfazl.abolmovie.utils.Extensions.showToast
import javax.inject.Inject

@AndroidEntryPoint
class UserFragment : Fragment() {
    lateinit var binding: FragmentUserBinding
    private var backPressedCount = 0
    @Inject
    lateinit var fireAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val username = fireAuth.currentUser?.displayName
        val email = fireAuth.currentUser?.email

        if (email!!.isNotEmpty() && username!!.isNotEmpty()) {
            binding.txtEmail.text = email
            binding.txtUsername.text = username
        } else {
            binding.txtEmail.text = "Error receiving email"
            binding.txtUsername.text = "Error receiving username"
        }

        binding.btnLogout.setOnClickListener {
            fireAuth.signOut()
            findNavController().navigate(R.id.to_fragmentLogin)
            requireActivity().showToast("SignOut is successfully")
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            if (backPressedCount == 1) {
                requireActivity().finish()
            } else {
                backPressedCount++
                requireActivity().showToast("Press back again to exit")

                Handler(Looper.getMainLooper()).postDelayed({
                    backPressedCount = 0
                }, 2000)
            }
        }

    }

}