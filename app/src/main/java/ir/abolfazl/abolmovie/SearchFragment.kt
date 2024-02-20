package ir.abolfazl.abolmovie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ir.abolfazl.abolmovie.Adapter.MovieAdapter
import ir.abolfazl.abolmovie.databinding.FragmentSearchBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.searchScreen.ExploreScreenViewModel
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity

class SearchFragment : Fragment(),MovieAdapter.ItemSelected {
    lateinit var binding : FragmentSearchBinding
    private val exploreScreenViewModel: ExploreScreenViewModel by viewModels({requireActivity()})
    private lateinit var searchAdapter : MovieAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainActivity().binding.bottomNavigation.visibility = View.INVISIBLE

        binding.edtSearch.addTextChangedListener { txt ->
            if (txt.toString().isNotEmpty()) {
                exploreScreenViewModel.searchMulti(txt.toString())
                exploreScreenViewModel.searchMovies.observe(viewLifecycleOwner) {
                    setupRecyclerView(it.results)
                }
                if (!binding.recyclerSearch.isVisible) {
                    binding.recyclerSearch.visibility = View.VISIBLE
                }
            } else if (txt.toString().isEmpty()) {
                binding.recyclerSearch.visibility = View.INVISIBLE
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.to_fragmentExplore)
        }
    }

    private fun setupRecyclerView(data: List<Movie_Tv.Result>) {
        if (!::searchAdapter.isInitialized) {
            searchAdapter = MovieAdapter(ArrayList(data), this)
            binding.recyclerSearch.adapter = searchAdapter
            binding.recyclerSearch.layoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
            binding.recyclerSearch.recycledViewPool.setMaxRecycledViews(0, 0)
        } else {
            searchAdapter.refreshData(data)
        }
    }

    override fun itemSelected(movie: Movie_Tv.Result) {

    }
}