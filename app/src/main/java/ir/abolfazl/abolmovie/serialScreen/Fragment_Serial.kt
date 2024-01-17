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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSerialBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.model.MovieAdapter
import ir.abolfazl.abolmovie.utils.Extensions.asyncRequest
import ir.abolfazl.abolmovie.utils.Extensions.mainActivity
import ir.abolfazl.abolmovie.utils.Extensions.showToast

@AndroidEntryPoint
class FragmentSerial : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentSerialBinding
    private lateinit var serialAdapter: MovieAdapter
    private lateinit var compositeDisposable: CompositeDisposable
    private var page = 2
    private val serialScreenViewModel: SerialScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSerialBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        compositeDisposable = CompositeDisposable()
        discoverTv(1)
        binding.swipeSerial.setOnRefreshListener {
            serialAdapter.clearData()
            discoverTv(1)
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

                R.id.btn_Profile_menu -> {
                    findNavController().navigate(R.id.action_fragmentSerial_to_userFragment)
                }

                R.id.btn_search_menu -> {
                    findNavController().navigate(R.id.action_fragmentSerial_to_searchFragment)
                }
            }
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun discoverTv(page: Int) {
        serialScreenViewModel
            .discoverTv(page)
            .asyncRequest()
            .subscribe(object : SingleObserver<Movie_Tv> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable) {
                    requireActivity().showToast("check network")
                }

                override fun onSuccess(t: Movie_Tv) {
                    setupRecyclerView(t.results)
                }

            })

    }

    private fun setupRecyclerView(data: List<Movie_Tv.Result>) {
        if (!::serialAdapter.isInitialized) {
            serialAdapter = MovieAdapter(ArrayList(data), this)
            binding.recyclerShowSerial.adapter = serialAdapter
            val layoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
            binding.recyclerShowSerial.layoutManager = layoutManager
            scrollRecycler(layoutManager)
            binding.recyclerShowSerial.recycledViewPool.setMaxRecycledViews(0, 0)
        } else
            serialAdapter.addData(data)
    }

    private fun scrollRecycler(layoutManager: GridLayoutManager) {

        binding.recyclerShowSerial.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= 20
                ) {
                    discoverTv(page)
                    page++
                }
            }
        })

    }

    override fun itemSelected(movie: Movie_Tv.Result) {

        val intent = Intent(requireContext(), DetailActivity::class.java)

        intent.putExtra("SendData", movie)

        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
