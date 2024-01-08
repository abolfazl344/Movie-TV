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
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Activity.DetailActivity
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentSerialBinding
import ir.abolfazl.abolmovie.mainScreen.FragmentMain
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.movieScreen.MovieAdapter
import ir.abolfazl.abolmovie.utils.mainActivity

@AndroidEntryPoint
class FragmentSerial : Fragment(), MovieAdapter.ItemSelected {
    lateinit var binding: FragmentSerialBinding
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
        FragmentMain.ItemCount = 20

        discoverTv()

        binding.swipeSerial.setOnRefreshListener {
            discoverTv()
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

                R.id.btn_Profile_menu ->{
                    findNavController().navigate(R.id.action_fragmentSerial_to_userFragment)
                }
            }
            true
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            requireActivity().finish()
        }
    }

    private fun discoverTv() {
        serialScreenViewModel.discoverTv()
        serialScreenViewModel.discoverSerial.observe(viewLifecycleOwner){
            showDataInRecycler(it.results)
        }
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
