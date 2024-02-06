package ir.abolfazl.abolmovie.Activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.ActivityMainBinding
import ir.abolfazl.abolmovie.mainScreen.FragmentMain
import ir.abolfazl.abolmovie.mainScreen.MainScreenViewModel
import ir.abolfazl.abolmovie.movieScreen.FragmentMovie
import ir.abolfazl.abolmovie.movieScreen.MovieScreenViewModel
import ir.abolfazl.abolmovie.searchScreen.SearchFragment
import ir.abolfazl.abolmovie.serialScreen.FragmentSerial
import ir.abolfazl.abolmovie.serialScreen.SerialScreenViewModel
import ir.abolfazl.abolmovie.userScreen.UserFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    private val serialScreenViewModel: SerialScreenViewModel by viewModels()
    private val movieScreenViewModel : MovieScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainData()
        serialScreenViewModel.discoverTv(1)
        movieScreenViewModel.discoverMovie(1)

        binding.bottomNavigation.selectedItemId = R.id.btn_Home_menu

        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btn_TV_menu -> {
                    replaceFragment(FragmentSerial())
                }

                R.id.btn_Movie_menu -> {
                    replaceFragment(FragmentMovie())
                }

                R.id.btn_search_menu -> {
                    replaceFragment(SearchFragment())
                }

                R.id.btn_Profile_menu -> {
                    replaceFragment(UserFragment())
                }

                R.id.btn_Home_menu -> {
                    replaceFragment(FragmentMain())
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

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.commit()

    }
}