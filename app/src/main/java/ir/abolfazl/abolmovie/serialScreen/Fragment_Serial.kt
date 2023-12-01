package ir.abolfazl.abolmovie.serialScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSerialBinding
import ir.abolfazl.abolmovie.mainScreen.FragmentMain
import ir.abolfazl.abolmovie.model.MainRepository
import ir.abolfazl.abolmovie.model.Movie_Tv
import ir.abolfazl.abolmovie.movieScreen.MovieAdapter
import ir.abolfazl.abolmovie.utils.asyncRequest
import ir.abolfazl.abolmovie.utils.mainActivity
import ir.abolfazl.abolmovie.utils.showToast

class FragmentSerial : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentSerialBinding
    lateinit var serialScreenViewModel: SerialScreenViewModel
    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSerialBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        FragmentMain.ItemCount = 20
        serialScreenViewModel = SerialScreenViewModel(MainRepository())
        compositeDisposable = CompositeDisposable()

        discoverTv()
        compositeDisposable.add(serialScreenViewModel.progressBarSubjectTv.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it) {
                    binding.recyclerShowSerial.visibility = View.INVISIBLE
                    binding.progressSerial.visibility = View.VISIBLE
                } else {
                    binding.recyclerShowSerial.visibility = View.VISIBLE
                    binding.progressSerial.visibility = View.INVISIBLE
                }
            })

        binding.swipeSerial.setOnRefreshListener {
            discoverTv()
            compositeDisposable.add(
                serialScreenViewModel.progressBarSubjectTv.subscribeOn(
                    Schedulers.io()
                ).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    if (it) {
                        binding.recyclerShowSerial.visibility = View.INVISIBLE
                        binding.progressSerial.visibility = View.VISIBLE
                    } else {
                        binding.recyclerShowSerial.visibility = View.VISIBLE
                        binding.progressSerial.visibility = View.INVISIBLE
                    }
                })
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeSerial.isRefreshing = false
            }, 1500)

        }

        mainActivity().binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btn_Home_menu -> {
                    findNavController().navigate(R.id.action_fragmentSerial_to_fragmentMain)
                }
                R.id.btn_Movie_menu -> {
                    findNavController().navigate(R.id.action_fragmentSerial_to_fragmentMovie)
                }
                R.id.btn_TV_menu -> {
                    findNavController().navigate(R.id.action_fragmentSerial_to_searchFragment)
                }
            }
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun discoverTv() {

        serialScreenViewModel
            .discoverTv()
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onSuccess(t: Movie_Tv) {
                    showDataInRecycler(t.results)
                }

                override fun onError(e: Throwable) {
                    requireActivity().showToast("check your internet connection")
                }

            })
    }

    private fun showDataInRecycler(data: List<Movie_Tv.Result>) {

        val tvAdapter = MovieAdapter(ArrayList(data), this)
        binding.recyclerShowSerial.adapter = tvAdapter
        binding.recyclerShowSerial.layoutManager =
            GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        binding.recyclerShowSerial.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    override fun itemSelected(movie: Movie_Tv.Result) {

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("SendData", movie)

        startActivity(intent)
    }
}
