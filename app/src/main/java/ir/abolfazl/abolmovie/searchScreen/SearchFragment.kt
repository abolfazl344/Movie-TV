package ir.abolfazl.abolmovie.searchScreen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.databinding.FragmentSearchBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.Adapter.MovieAdapter

@AndroidEntryPoint
class SearchFragment : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: MovieAdapter
    private val searchScreenViewModel: SearchScreenViewModel by viewModels({requireActivity()})

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
            requireActivity().finish()
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
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("SendData", movie)
        startActivity(intent)
    }

}