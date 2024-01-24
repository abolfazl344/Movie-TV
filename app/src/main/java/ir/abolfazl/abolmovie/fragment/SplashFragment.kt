package ir.abolfazl.abolmovie.fragment


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSplashBinding
import ir.abolfazl.abolmovie.mainScreen.MainScreenViewModel
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {
    @Inject
    lateinit var fireAuth: FirebaseAuth
    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainActivity().binding.bottomNavigation.visibility = View.INVISIBLE
        firstRun()

        Handler(Looper.getMainLooper()).postDelayed({
            if (fireAuth.currentUser == null) {
                findNavController().navigate(R.id.action_splashFragment_to_signUpFragment2)
            }else
            findNavController().navigate(R.id.action_splashFragment_to_fragmentMain3)
        }, 5000)
    }
    private fun firstRun() {
        val prefs = requireActivity().getSharedPreferences(null, AppCompatActivity.MODE_PRIVATE)
        val firstRun = prefs.getBoolean("KEY_FIRST_RUN", true)

        if (firstRun) {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_splashFragment_to_fragmentIntro)
                prefs.edit().putBoolean("KEY_FIRST_RUN", false).apply()
            }, 3000)
        }
    }
}