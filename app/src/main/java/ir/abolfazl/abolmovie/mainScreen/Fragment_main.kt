package ir.abolfazl.abolmovie.mainScreen

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.Adapter.MovieAdapter
import ir.abolfazl.abolmovie.databinding.FragmentMainBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FragmentMain : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentMainBinding
    private val mainScreenViewModel: MainScreenViewModel by viewModels({ requireActivity() })
    private lateinit var mainAdapter: MovieAdapter

    @Inject
    lateinit var fireAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainActivity().binding.bottomNavigation.visibility = View.VISIBLE
        initUi()
        binding.refreshLayoutMain.setOnRefreshListener {
            mainActivity().mainData()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.refreshLayoutMain.isRefreshing = false
            }, 1500)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }

    }

    private fun initUi() {
        getNowPlayingMovie()
        getPopularMovie()
        getTopMovie()
        getTopTv()
        getPopularTv()
    }

    private fun getPopularMovie() {
        mainScreenViewModel.popularMovie.observe(viewLifecycleOwner) {
            setDataToRecycler(binding.recyclerPopularMovie, it.results)
        }

    }

    private fun getTopMovie() {
        mainScreenViewModel.topMovie.observe(viewLifecycleOwner) {
            setDataToRecycler(binding.recyclerToprated, it.results)
        }

    }

    private fun getNowPlayingMovie() {
        mainScreenViewModel.nowMovie.observe(viewLifecycleOwner) {
            setDataToRecycler(binding.recyclerNowPlaying, it.results)
        }

    }

    private fun getTopTv() {
        mainScreenViewModel.topTv.observe(viewLifecycleOwner) {
            setDataToRecycler(binding.recyclerTopRatedTv, it.results)
        }
    }

    private fun getPopularTv() {
        mainScreenViewModel.popularTv.observe(viewLifecycleOwner) {
            setDataToRecycler(binding.recyclerPopularTv, it.results)
        }
    }

    private fun setDataToRecycler(recyclerView: RecyclerView, data: List<Movie_Tv.Result>) {
        mainAdapter = MovieAdapter(ArrayList(data), this)
        recyclerView.adapter = mainAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    override fun itemSelected(movie: Movie_Tv.Result) {

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("SendData", movie)

        startActivity(intent)
    }
}




