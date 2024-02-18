package ir.abolfazl.abolmovie.searchScreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.databinding.FragmentSearchBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.Adapter.MovieAdapter
import ir.abolfazl.abolmovie.detailScreen.DetailFragment
import ir.abolfazl.abolmovie.mainScreen.FragmentMainDirections
import ir.abolfazl.abolmovie.serialScreen.FragmentSerialDirections
import ir.abolfazl.abolmovie.utils.Extensions.showToast

@AndroidEntryPoint
class SearchFragment : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: MovieAdapter
    private val searchScreenViewModel: SearchScreenViewModel by viewModels({requireActivity()})
    private var backPressedCount = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.edtSearch.addTextChangedListener { txt ->
            if (txt.toString().isNotEmpty()) {
                searchScreenViewModel.searchMovie(txt.toString())
                searchScreenViewModel.searchMovies.observe(viewLifecycleOwner) {
                    setupRecyclerView(binding.recyclerSearch,it.results)
                }
                if (!binding.recyclerSearch.isVisible) {
                    binding.recyclerSearch.visibility = View.VISIBLE
                }
            } else if (txt.toString().isEmpty()) {
                binding.recyclerSearch.visibility = View.INVISIBLE
            }
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

    private fun setupRecyclerView(recyclerView: RecyclerView, data: List<Movie_Tv.Result>) {
        if (!::searchAdapter.isInitialized) {
            searchAdapter = MovieAdapter(ArrayList(data), this)
            recyclerView.adapter = searchAdapter
            recyclerView.layoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
            recyclerView.recycledViewPool.setMaxRecycledViews(0, 0)
        } else {
            searchAdapter.refreshData(data)
        }
    }

    override fun itemSelected(movie: Movie_Tv.Result) {
        val action = SearchFragmentDirections.toDetailActivity(movie)
        findNavController().navigate(action)
    }

}