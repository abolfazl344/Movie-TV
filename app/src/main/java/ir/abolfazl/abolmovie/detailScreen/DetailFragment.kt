package ir.abolfazl.abolmovie.detailScreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Adapter.CastAdapter
import ir.abolfazl.abolmovie.Adapter.CrewAdapter
import ir.abolfazl.abolmovie.Adapter.MovieAdapter
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.FragmentDetailBinding
import ir.abolfazl.abolmovie.model.Local.Credits
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE_CREDITS
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment(), MovieAdapter.ItemSelected, CastAdapter.ItemSelected, CrewAdapter.ItemSelected {
    lateinit var binding: FragmentDetailBinding
    @Inject
    lateinit var glide: RequestManager
    private lateinit var castAdapter: CastAdapter
    private lateinit var crewAdapter: CrewAdapter
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var dataCredits: Credits
    private val detailViewModel: DetailViewModel by viewModels({requireActivity()})
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(detailViewModel.dataMovie.profilePath != null){
            val action = DetailFragmentDirections.actionDetailFragmentToCreditsFragment(detailViewModel.dataMovie.id)
            findNavController().navigate(action)
        }else{
            loadDataDetail(detailViewModel.dataMovie)
        }

        binding.txtCrew.setOnClickListener {
            binding.txtCrew.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.txtCast.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            if (dataCredits.crew != null) {
                setupRecyclerCrew(dataCredits.crew!!)
            }
        }

        binding.txtCast.setOnClickListener {
            binding.txtCrew.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            binding.txtCast.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            if (dataCredits.cast != null) {
                setupRecyclerCast(dataCredits.cast!!)
            }
        }

        binding.imgBackToHome.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadDataDetail(dataMovie: Movie_Tv.Result) {
        getRecommend()
        getCredits()

        glide
            .load(BASE_URL_IMAGE_CREDITS + dataMovie.posterPath)
            .transform(RoundedCornersTransformation(32, 8))
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.imgPoster)

        glide
            .load(BASE_URL_IMAGE_CREDITS + dataMovie.backdropPath)
            .into(binding.imgBackImage)

        if (dataMovie.title != null) {
            binding.txtTitle.text = dataMovie.title
        } else if (dataMovie.name != null) {
            binding.txtTitle.text = dataMovie.name
        }

        //binding.txtScore.text = dataMovie.voteAverage.toString()
        binding.txtInfoMovie.text = dataMovie.overview
        binding.txtTime.text = dataMovie.originalLanguage
    }

    private fun getRecommend() {
        detailViewModel.recommend.observe(viewLifecycleOwner) {
            if (it.results.isNotEmpty()) {
                setupRecyclerRecommend(it.results)
            } else {
                binding.txtRecommended.visibility = View.GONE
                binding.recyclerRecommend.visibility = View.GONE
            }

        }
    }

    /*
    private fun getMovieTrailer() {
        detailViewModel.trailer.observe(this) {
            if (it.results!!.isNotEmpty()) {
                loadTrailer(it.results[0].key)
            } else {
                binding.txtTrailerError.visibility = View.VISIBLE
            }
        }
    }

    private fun getTvTrailer() {
        detailViewModel.trailer.observe(this) {
            if (it.results!!.isNotEmpty()) {
                loadTrailer(it.results[0].key)
            } else {
                binding.txtTrailerError.visibility = View.VISIBLE
            }

        }
    }

     */

    private fun getCredits() {
        detailViewModel.credits.observe(viewLifecycleOwner) {
            if (it.cast!!.isNotEmpty()) {
                dataCredits = it
                setupRecyclerCast(it.cast)
            }
        }
    }

    private fun setupRecyclerCast(cast: List<Credits.Cast>) {
        castAdapter = CastAdapter(ArrayList(cast), this)
        binding.recyclerCast.adapter = castAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCast.layoutManager = layoutManager
        binding.recyclerCast.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    private fun setupRecyclerCrew(crew: List<Credits.Crew>) {
        crewAdapter = CrewAdapter(ArrayList(crew), this)
        binding.recyclerCast.adapter = crewAdapter
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCast.layoutManager = layoutManager
        binding.recyclerCast.recycledViewPool.setMaxRecycledViews(0, 0)
    }

    private fun setupRecyclerRecommend(recommend: List<Movie_Tv.Result>) {
        if (!::movieAdapter.isInitialized) {
            movieAdapter = MovieAdapter(ArrayList(recommend), this)
            binding.recyclerRecommend.adapter = movieAdapter
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerRecommend.layoutManager = layoutManager
            binding.recyclerRecommend.recycledViewPool.setMaxRecycledViews(0, 0)
        } else {
            movieAdapter.refreshData(recommend)
            binding.recyclerRecommend.scrollToPosition(0)
        }

    }

    /*
    private fun loadTrailer(videoID: String) {
        val listener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoID, 0f)
            }
        }
        val option = IFramePlayerOptions.Builder().controls(1).fullscreen(1).build()
        binding.playerDetail.initialize(listener, option)
        binding.playerDetail.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {

            }

            override fun onExitFullscreen() {

            }

        })
        lifecycle.addObserver(binding.playerDetail)
    }

     */

    override fun itemSelected(credits: Credits.Crew) {
        val action = DetailFragmentDirections.actionDetailFragmentToCreditsFragment(credits.id!!)
        findNavController().navigate(action)
    }

    override fun itemSelectedCast(credits: Credits.Cast) {
        val action = DetailFragmentDirections.actionDetailFragmentToCreditsFragment(credits.id!!)
        findNavController().navigate(action)
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
        
        loadDataDetail(movie)
        binding.nestedScrollView.smoothScrollTo(0, 0)
    }
}