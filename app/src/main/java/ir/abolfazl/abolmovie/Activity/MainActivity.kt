package ir.abolfazl.abolmovie.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.ActivityMainBinding
import ir.abolfazl.abolmovie.mainScreen.MainScreenViewModel
import ir.abolfazl.abolmovie.movieScreen.MovieScreenViewModel
import ir.abolfazl.abolmovie.serialScreen.SerialScreenViewModel
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    private val serialScreenViewModel: SerialScreenViewModel by viewModels()
    private val movieScreenViewModel: MovieScreenViewModel by viewModels()
    private var pressMovie = 0
    private var pressTv = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainData()

        binding.bottomNavigation.selectedItemId = R.id.btn_Home_menu

        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btn_TV_menu -> {
                    if(pressTv == 0){
                        serialScreenViewModel.discoverTv(1)
                        pressTv++
                    }
                    findNavController(R.id.fragmentContainerView).navigate(R.id.to_fragmentTv)
                }

                R.id.btn_Movie_menu -> {
                    if(pressMovie == 0){
                        movieScreenViewModel.discoverMovie(1)
                        pressMovie++
                    }
                    findNavController(R.id.fragmentContainerView).navigate(R.id.to_fragmentMovie)
                }

                R.id.btn_search_menu -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.to_fragmentExplore)
                }

                R.id.btn_Profile_menu -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.to_fragmentUser)
                }

                R.id.btn_Home_menu -> {
                    findNavController(R.id.fragmentContainerView).navigate(R.id.to_fragmentMain)
                }

            }
            true
        }

        binding.bottomNavigation.setOnItemReselectedListener {}

    }

    fun mainData() {
        mainScreenViewModel.getPopularMovie(1)
        mainScreenViewModel.getTopMovie(1)
        mainScreenViewModel.getNowPlaying(1)
        mainScreenViewModel.getTopTv(1)
        mainScreenViewModel.getPopularTv(1)
    }

}