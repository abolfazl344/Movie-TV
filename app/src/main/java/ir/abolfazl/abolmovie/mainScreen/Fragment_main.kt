package ir.abolfazl.abolmovie.mainScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.movieScreen.MovieAdapter
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentMainBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.api.ApiService
import ir.abolfazl.abolmovie.utils.MainViewModelFactory
import ir.abolfazl.abolmovie.utils.mainActivity
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentMain : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentMainBinding
    private val mainScreenViewModel: MainScreenViewModel by viewModels()
    companion object {
        var ItemCount = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        firstRun()

        initUi()

        binding.refreshLayoutMain.setOnRefreshListener {
            initUi()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.refreshLayoutMain.isRefreshing = false
            }, 1500)
        }

        mainActivity().binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btn_TV_menu -> {
                    findNavController().navigate(R.id.action_fragmentMain_to_fragmentSerial)
                }

                R.id.btn_Movie_menu -> {
                    findNavController().navigate(R.id.action_fragmentMain_to_fragmentMovie)
                }

                R.id.btn_search_menu -> {
                    findNavController().navigate(R.id.action_fragmentMain_to_searchFragment)
                }
            }
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun initUi() {
        getNowPlayingMovie()
        getPopularMovie()
        getTopMovie()
    }

    private fun firstRun() {
        val prefs = requireActivity().getSharedPreferences(null, AppCompatActivity.MODE_PRIVATE)
        val firstRun = prefs.getBoolean("KEY_FIRST_RUN", true)

        if (firstRun) {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentIntro2)
            prefs.edit().putBoolean("KEY_FIRST_RUN", false).apply()
        }
    }

    private fun getPopularMovie() {
        mainScreenViewModel.getPopularMovie()
        mainScreenViewModel.popularMovie.observe(viewLifecycleOwner) {
            showDataInRecyclerPopularMovie(it.results)
        }

    }

    private fun getTopMovie() {
        mainScreenViewModel.getTopMovie()
        mainScreenViewModel.topMovie.observe(viewLifecycleOwner) {
            showDataInRecyclerTopRated(it.results)
        }

    }

    private fun getNowPlayingMovie() {
        mainScreenViewModel.getNowPlaying()
        mainScreenViewModel.nowMovie.observe(viewLifecycleOwner) {
            showDataInRecyclerNowPlaying(it.results)
        }

    }

    override fun itemSelected(movie: Movie_Tv.Result) {

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("SendData", movie)

        startActivity(intent)
    }

    private fun showDataInRecyclerNowPlaying(data: List<Movie_Tv.Result>) {

        val mainAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerNowPlaying.adapter = mainAdapter
        binding.recyclerNowPlaying.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerNowPlaying.recycledViewPool.setMaxRecycledViews(0, 0)

    }

    private fun showDataInRecyclerTopRated(data: List<Movie_Tv.Result>) {
        val mainAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerToprated.adapter = mainAdapter
        binding.recyclerToprated.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerToprated.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    private fun showDataInRecyclerPopularMovie(data: List<Movie_Tv.Result>) {

        val mainAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerNewMovie.adapter = mainAdapter
        binding.recyclerNewMovie.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerNewMovie.recycledViewPool.setMaxRecycledViews(0, 0)
    }

}




