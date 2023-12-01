package ir.abolfazl.abolmovie.mainScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.movieScreen.MovieAdapter
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentMainBinding
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Movie_Tv
import ir.abolfazl.abolmovie.utils.asyncRequest
import ir.abolfazl.abolmovie.utils.mainActivity
import java.util.*

class FragmentMain : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentMainBinding
    lateinit var mainScreenViewModel: MainScreenViewModel
    lateinit var compositeDisposable: CompositeDisposable

    companion object {
        var ItemCount = 10
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainScreenViewModel = MainScreenViewModel(MainRepository())
        compositeDisposable = CompositeDisposable()

        firstRun()
        mainActivity().binding.bottomNavigation.visibility = View.VISIBLE

            initUi()
            progressBar()

        binding.refreshLayoutMain.setOnRefreshListener {
            initUi()
            progressBar()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.refreshLayoutMain.isRefreshing = false
            }, 1500)
        }

        mainActivity().binding.bottomNavigation.setOnItemSelectedListener{

            when(it.itemId){
                R.id.btn_TV_menu -> {
                    findNavController().navigate(R.id.action_fragmentMain_to_fragmentSerial)
                }
                R.id.btn_Movie_menu -> {
                    findNavController().navigate(R.id.action_fragmentMain_to_fragmentMovie)
                }
                R.id.btn_search_menu -> {
                    findNavController().navigate(R.id.action_fragmentMain_to_searchFragment)
                }
            }
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun progressBar() {

        compositeDisposable.add(mainScreenViewModel.progressBarSubjectPop.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            if(it){
                binding.recyclerNewMovie.visibility = View.INVISIBLE
                binding.progressBarNewmovie.visibility = View.VISIBLE

            }else{
                binding.recyclerNewMovie.visibility = View.VISIBLE
                binding.progressBarNewmovie.visibility = View.INVISIBLE
            }
        })
        compositeDisposable.add(mainScreenViewModel.progressBarSubjectTop.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            if(it){
                binding.recyclerToprated.visibility = View.INVISIBLE
                binding.progressBarTopMovie.visibility = View.VISIBLE

            }else{
                binding.recyclerToprated.visibility = View.VISIBLE
                binding.progressBarTopMovie.visibility = View.INVISIBLE
            }
        })
        compositeDisposable.add(mainScreenViewModel.progressBarSubjectNow.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{
            if(it){
                binding.recyclerNowPlaying.visibility = View.INVISIBLE
                binding.progressBarNowPlaying.visibility = View.VISIBLE

            }else{
                binding.recyclerNowPlaying.visibility = View.VISIBLE
                binding.progressBarNowPlaying.visibility = View.INVISIBLE
            }
        })

    }

    private fun initUi() {
        getNowPlayingMovie()
        getPopularMovie()
        getTopMovie()
    }

    private fun firstRun() {
        val prefs = requireActivity().getSharedPreferences(null, AppCompatActivity.MODE_PRIVATE)
        val firstRun = prefs.getBoolean("KEY_FIRST_RUN", true)

        if (firstRun) {
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentIntro2)
            prefs.edit().putBoolean("KEY_FIRST_RUN", false).apply()
        }
    }

    private fun getTopMovie() {
        mainScreenViewModel
            .getTopMovie()
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)

                }

                override fun onSuccess(t: Movie_Tv) {
                    showDataInRecyclerTopRated(t.results)
                }

                override fun onError(e: Throwable) {
                }

            })
    }

    private fun getPopularMovie() {
        mainScreenViewModel
            .getPopularMovie()
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                   compositeDisposable.add(d)
                }

                override fun onSuccess(t: Movie_Tv) {
                    showDataInRecyclerPopularMovie(t.results)
                }

                override fun onError(e: Throwable) {

                }

            })
    }

    private fun getNowPlayingMovie() {
        mainScreenViewModel
            .getNowPlaying()
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: Movie_Tv) {
                    showDataInRecyclerNowPlaying(t.results)
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(requireContext(), "check your connection", Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

    private fun showDataSearched(data: List<Movie_Tv.Result>) {

        val mainAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerNowPlaying.adapter = mainAdapter
        binding.recyclerNowPlaying.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerNowPlaying.recycledViewPool.setMaxRecycledViews(0, 0)

    }

    override fun itemSelected(movie: Movie_Tv.Result) {

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("SendData", movie)

        startActivity(intent)
    }

    private fun showDataInRecyclerNowPlaying(data: List<Movie_Tv.Result>) {

        val mainAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerNowPlaying.adapter = mainAdapter
        binding.recyclerNowPlaying.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerNowPlaying.recycledViewPool.setMaxRecycledViews(0, 0)

    }

    private fun showDataInRecyclerTopRated(data: List<Movie_Tv.Result>) {
        val mainAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerToprated.adapter = mainAdapter
        binding.recyclerToprated.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerToprated.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    private fun showDataInRecyclerPopularMovie(data: List<Movie_Tv.Result>) {

        val mainAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerNewMovie.adapter = mainAdapter
        binding.recyclerNewMovie.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerNewMovie.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}




