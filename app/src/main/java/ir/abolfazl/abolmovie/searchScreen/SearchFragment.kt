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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSearchBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.model.api.ApiService
import ir.abolfazl.abolmovie.movieScreen.MovieAdapter
import ir.abolfazl.abolmovie.utils.mainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentSearchBinding
    @Inject
    lateinit var apiService: ApiService
    private val searchScreenViewModel: SearchScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mainActivity().binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btn_Home_menu -> {
                    findNavController().navigate(R.id.action_searchFragment_to_fragmentMain)
                }
                R.id.btn_TV_menu -> {
                    findNavController().navigate(R.id.action_searchFragment_to_fragmentSerial)
                }
                R.id.btn_Movie_menu -> {
                    findNavController().navigate(R.id.action_searchFragment_to_fragmentMovie)
                }

            }
            true
        }


        binding.edtSearch.addTextChangedListener { txt ->
            if(txt.toString().isNotEmpty()){
                searchScreenViewModel.searchMovie(txt.toString())
                searchScreenViewModel.searchMovies.observe(viewLifecycleOwner){
                    showDataInRecycler(it.results)
                }
                if(!binding.recyclerSearch.isVisible){
                    binding.recyclerSearch.visibility = View.VISIBLE
                }
            }else if(txt.toString().isEmpty()){
                binding.recyclerSearch.visibility = View.INVISIBLE
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }

    }


    private fun showDataInRecycler(data: List<Movie_Tv.Result>) {

        val adapter: MovieAdapter

        if (data.isNotEmpty()) {
            binding.txtNoSearchFound.visibility = View.INVISIBLE
            adapter = MovieAdapter(ArrayList(data), this)
            binding.recyclerSearch.adapter = adapter
            binding.recyclerSearch.layoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
            binding.recyclerSearch.recycledViewPool.setMaxRecycledViews(0, 0)

        }else{
            binding.txtNoSearchFound.visibility = View.VISIBLE
            binding.recyclerSearch.visibility = View.INVISIBLE
        }

    }

    override fun itemSelected(movie: Movie_Tv.Result) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("SendData", movie)
        startActivity(intent)
    }

}