package ir.abolfazl.abolmovie.searchScreen

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSearchBinding
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Movie_Tv
import ir.abolfazl.abolmovie.movieScreen.MovieAdapter
import ir.abolfazl.abolmovie.utils.asyncRequest
import ir.abolfazl.abolmovie.utils.mainActivity
import ir.abolfazl.abolmovie.utils.showToast

class SearchFragment : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentSearchBinding
    lateinit var searchScreenViewModel: SearchScreenViewModel
    lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        searchScreenViewModel = SearchScreenViewModel(MainRepository())

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

        binding.edtSearch.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                searchMovie(binding.edtSearch.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }


        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }

    }

    private fun searchMovie(title: String) {
        searchScreenViewModel.searchMovie(title).asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onSuccess(t: Movie_Tv) {
                    showDataInRecycler(t.results)
                }

                override fun onError(e: Throwable) {

                }

            })
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}