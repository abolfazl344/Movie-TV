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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.databinding.FragmentMovieBinding
import ir.abolfazl.abolmovie.Adapter.MovieAdapter

@AndroidEntryPoint
class FragmentMovie : Fragment(), MovieAdapter.ItemSelected {
    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieAdapter: MovieAdapter
    private val movieScreenViewModel: MovieScreenViewModel by viewModels({ requireActivity() })
    private var page = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        discoverMovie()
        binding.swipeMovie.setOnRefreshListener {
            if (::movieAdapter.isInitialized) {
                movieAdapter.clearData()
            }
            movieScreenViewModel.discoverMovie(1)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeMovie.isRefreshing = false
            }, 1500)

        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun discoverMovie() {
        movieScreenViewModel.discoverMovie.observe(viewLifecycleOwner) {
            setupRecyclerView(it.results)
        }
    }

    private fun setupRecyclerView(data: List<Movie_Tv.Result>) {
        if (!::movieAdapter.isInitialized) {
            movieAdapter = MovieAdapter(ArrayList(data), this)
            binding.recyclerShowMovie.adapter = movieAdapter
            val layoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
            binding.recyclerShowMovie.layoutManager = layoutManager
            //scrollRecycler(layoutManager)
            //binding.recyclerShowMovie.recycledViewPool.setMaxRecycledViews(0, 0)
        } else {
            movieAdapter.addData(data)
        }
    }

    /*
    private fun scrollRecycler(layoutManager: GridLayoutManager) {

        binding.recyclerShowMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= 20) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        discoverMovie(page)
                        page++
                    }, 1500)
                }
            }

        })

    }

     */

    override fun itemSelected(movie: Movie_Tv.Result) {

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("SendData", movie)

        startActivity(intent)
    }

}
