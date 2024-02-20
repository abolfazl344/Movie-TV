package ir.abolfazl.abolmovie.serialScreen

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import ir.abolfazl.abolmovie.databinding.FragmentSerialBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.Adapter.MovieAdapter
import ir.abolfazl.abolmovie.utils.Extensions.showToast

@AndroidEntryPoint
class FragmentSerial : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentSerialBinding
    private lateinit var serialAdapter: MovieAdapter
    private val serialScreenViewModel: SerialScreenViewModel by viewModels({ requireActivity() })
    private var page = 2
    private var backPressedCount = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSerialBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        discoverTv()
        binding.swipeSerial.setOnRefreshListener {
            if (::serialAdapter.isInitialized) {
                serialAdapter.clearData()
            }
            serialScreenViewModel.discoverTv(1)
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeSerial.isRefreshing = false
            }, 1500)

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

    private fun discoverTv() {
        serialScreenViewModel.discoverTv.observe(viewLifecycleOwner) {
            Log.v("testData", it.toString())
            setupRecyclerView(it.results)
        }

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
                    serialScreenViewModel.discoverTv(page)
                    page++
                }
            }
        })

    }

    override fun itemSelected(movie: Movie_Tv.Result) {
        val action = FragmentSerialDirections.toDetailActivity(movie)
        findNavController().navigate(action)
    }

}
