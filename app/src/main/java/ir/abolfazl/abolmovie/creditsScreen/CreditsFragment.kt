package ir.abolfazl.abolmovie.creditsScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Adapter.MovieAdapter
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentCreditsBinding
import ir.abolfazl.abolmovie.detailScreen.DetailViewModel
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE_CREDITS
import javax.inject.Inject

@AndroidEntryPoint
class CreditsFragment : Fragment(),MovieAdapter.ItemSelected {
    lateinit var binding : FragmentCreditsBinding
    private val args : CreditsFragmentArgs by navArgs()
    private val creditsViewModel : CreditsViewModel by viewModels()
    private val detailViewModel : DetailViewModel by viewModels({requireActivity()})
    private lateinit var movieAdapter : MovieAdapter
    @Inject
    lateinit var glide: RequestManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreditsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val id = args.id
        getPersonDetail(id)
        getPersonCredits(id)

        /*
        binding.imgBackToHome.setOnClickListener {
            findNavController().navigate(R.id.action_creditsFragment_to_detailFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            findNavController().navigate(R.id.action_creditsFragment_to_detailFragment)
        }

         */
    }

    private fun getPersonCredits(id: Int) {
        creditsViewModel.getPersonCredits(id)
        creditsViewModel.credits.observe(viewLifecycleOwner){
            setupRecyclerRecommend(it.cast)
        }
    }

    private fun getPersonDetail(id: Int) {
        creditsViewModel.getPersonDetail(id)
        creditsViewModel.person.observe(viewLifecycleOwner){

            glide
                .load(BASE_URL_IMAGE_CREDITS + it.profilePath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(
                    RequestOptions()
                        .circleCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                )
                .into(binding.imgPoster)

            binding.txtName.text = it.name
            binding.txtInfoCredits.text = it.biography
        }
    }

    private fun setupRecyclerRecommend(credits: List<Movie_Tv.Result>) {
        if(!::movieAdapter.isInitialized){
            movieAdapter = MovieAdapter(ArrayList(credits),this)
            binding.recyclerPerson.adapter = movieAdapter
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerPerson.layoutManager = layoutManager
            binding.recyclerPerson.recycledViewPool.setMaxRecycledViews(0, 0)
        }else{
            movieAdapter.refreshData(credits)
        }

    }

    override fun itemSelected(movie: Movie_Tv.Result) {
        if (movie.releaseDate != null) {
            //getMovieTrailer(dataMovie.id)
            detailViewModel.getRecommendMovie(movie.id)
            detailViewModel.getCreditsMovie(movie.id)
        } else if (movie.firstAirDate != null) {
            //getTvTrailer(dataMovie.id)
            detailViewModel.getRecommendTv(movie.id)
            detailViewModel.getCreditsTv(movie.id)
        }
        detailViewModel.dataMovie = movie

        findNavController().navigate(R.id.action_creditsFragment_to_detailFragment)
    }
}