package ir.abolfazl.abolmovie.movieScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.databinding.FragmentMovieBinding
import ir.abolfazl.abolmovie.mainScreen.FragmentMain
import ir.abolfazl.abolmovie.utils.mainActivity

@AndroidEntryPoint
class FragmentMovie : Fragment(), MovieAdapter.ItemSelected {
    private lateinit var binding: FragmentMovieBinding
    private val movieScreenViewModel: MovieScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        FragmentMain.ItemCount = 20

        discoverMovie()

        binding.swipeMovie.setOnRefreshListener {
            discoverMovie()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeMovie.isRefreshing = false
            }, 1500)

        }

        mainActivity().binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btn_Home_menu -> {
                    findNavController().navigate(R.id.action_fragmentMovie_to_fragmentMain)
                }

                R.id.btn_TV_menu -> {
                    findNavController().navigate(R.id.action_fragmentMovie_to_fragmentSerial)
                }

                R.id.btn_search_menu -> {
                    findNavController().navigate(R.id.action_fragmentMovie_to_searchFragment)
                }
            }
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun discoverMovie() {
        movieScreenViewModel.discoverMovie()
        movieScreenViewModel.discoverMovie.observe(viewLifecycleOwner){
            showDataInRecycler(it.results)
        }
    }

    private fun showDataInRecycler(data: List<Movie_Tv.Result>) {

        val movieAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerShowMovie.adapter = movieAdapter
        binding.recyclerShowMovie.layoutManager =
            GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        binding.recyclerShowMovie.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    override fun itemSelected(movie: Movie_Tv.Result) {

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("SendData", movie)

        startActivity(intent)
    }

}
