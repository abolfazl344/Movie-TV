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
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.Adapter.MovieAdapter
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentExploreBinding
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity
import ir.abolfazl.abolmovie.utils.Extensions.showToast

@AndroidEntryPoint
class ExploreFragment : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentExploreBinding
    private var backPressedCount = 0
    private val exploreScreenViewModel: ExploreScreenViewModel by viewModels({ requireActivity() })
    private lateinit var searchAdapter: MovieAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExploreBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainActivity().binding.bottomNavigation.visibility = View.VISIBLE

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
        val action = ExploreFragmentDirections.toDetailActivity(movie)
        findNavController().navigate(action)
    }

}