package ir.abolfazl.abolmovie.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.movieScreen.MovieAdapter
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.model.Movie
import ir.abolfazl.abolmovie.databinding.FragmentMovieBinding
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.movieScreen.MovieScreenViewModel
import ir.abolfazl.abolmovie.utils.asyncRequest
import ir.abolfazl.abolmovie.utils.mainActivity
import ir.abolfazl.abolmovie.utils.showToast

class FragmentMovie : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentMovieBinding
    lateinit var movieScreenViewModel: MovieScreenViewModel
    lateinit var compositeDisposable: CompositeDisposable

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
        movieScreenViewModel = MovieScreenViewModel(MainRepository())
        compositeDisposable = CompositeDisposable()

        discoverMovie()

        binding.swipeMovie.setOnRefreshListener {
            discoverMovie()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeMovie.isRefreshing = false
            }, 1500)

        }

        mainActivity().binding.bottomNavigation.setOnItemSelectedListener {

            when(it.itemId){
                R.id.btn_Home_menu ->{
                    findNavController().navigate(R.id.action_fragmentMovie_to_fragmentMain)
                }
                R.id.btn_TV_menu ->{
                    findNavController().navigate(R.id.action_fragmentMovie_to_fragmentSerial)
                }

            }
            true
        }
    }

    private fun discoverMovie(){
        movieScreenViewModel
            .discoverMovie()
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie>{
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: Movie) {
                    showDataInRecycler(t.results)
                }

                override fun onError(e: Throwable) {
                    requireActivity().showToast("check your internet connection")
                }

            })

        compositeDisposable.add(movieScreenViewModel.progressBarSubjectMovie.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            if(it){
                binding.progressMovie.visibility = View.VISIBLE
                binding.recyclerShowMovie.visibility = View.INVISIBLE
            }else{
                binding.progressMovie.visibility = View.INVISIBLE
                binding.recyclerShowMovie.visibility = View.VISIBLE
            }
        })
    }

    private fun showDataInRecycler(data: List<Movie.Result>) {

        val movieAdapter = MovieAdapter(ArrayList(data),this)
        binding.recyclerShowMovie.adapter = movieAdapter
        binding.recyclerShowMovie.layoutManager = GridLayoutManager(requireContext(),3,LinearLayoutManager.VERTICAL,false)
        binding.recyclerShowMovie.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    override fun itemSelected(movie: Movie.Result) {

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("dataMovie", movie)

        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}