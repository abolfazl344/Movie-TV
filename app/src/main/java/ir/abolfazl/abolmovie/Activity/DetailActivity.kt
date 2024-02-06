package ir.abolfazl.abolmovie.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.Adapter.CastAdapter
import ir.abolfazl.abolmovie.Adapter.CrewAdapter
import ir.abolfazl.abolmovie.databinding.ActivityDetailBinding
import ir.abolfazl.abolmovie.model.Local.Credits
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.utils.Extensions.showToast
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), CastAdapter.ItemSelected, CrewAdapter.ItemSelected {
    lateinit var binding: ActivityDetailBinding
    private lateinit var castAdapter: CastAdapter
    private lateinit var crewAdapter: CrewAdapter

    @Inject
    lateinit var glide: RequestManager
    private val detailViewModel: DetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataMovie = intent.getParcelableExtra<Movie_Tv.Result>("SendData")

        glide
            .load(BASE_URL_IMAGE + dataMovie!!.posterPath)
            .transform(RoundedCornersTransformation(32, 8))
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.imgPoster)

        glide
            .load(BASE_URL_IMAGE + dataMovie.backdropPath)
            .into(binding.imgBackImage)

        if (dataMovie.releaseDate != null) {
            binding.txtRelease.text = dataMovie.releaseDate
            getMovieTrailer(dataMovie.id)
            getCreditsMovie(dataMovie.id)
        } else if (dataMovie.firstAirDate != null) {
            binding.txtRelease.text = dataMovie.firstAirDate
            getTvTrailer(dataMovie.id)
            getCreditsTv(dataMovie.id)
        }
        if (dataMovie.title != null) {
            binding.txtTitle.text = dataMovie.title
        } else if (dataMovie.name != null) {
            binding.txtTitle.text = dataMovie.name
        }

        binding.txtScore.text = dataMovie.voteAverage.toString()
        binding.txtInfoMovie.text = dataMovie.overview
        binding.txtTime.text = dataMovie.originalLanguage

        binding.imgBackToHome.setOnClickListener {
            onBackPressed()

        }
    }

    private fun getMovieTrailer(movieID: Int) {
        detailViewModel.getMovieTrailer(movieID)
        detailViewModel.trailer.observe(this) {
            if (it.results!!.isNotEmpty()) {
                loadTrailer(it.results[0].key)
            } else {
                binding.txtTrailerError.visibility = View.VISIBLE
            }
        }
    }

    private fun getTvTrailer(serialID: Int) {
        detailViewModel.getTvTrailer(serialID)
        detailViewModel.trailer.observe(this) {
            if (it.results!!.isNotEmpty()) {
                loadTrailer(it.results[0].key)
            } else {
                binding.txtTrailerError.visibility = View.VISIBLE
            }

        }
    }

    private fun getCreditsMovie(movieID: Int) {
        detailViewModel.getCreditsMovie(movieID)
        detailViewModel.credits.observe(this) {
            if (it.cast!!.isNotEmpty()) {
                setupRecyclerCast(it.cast)
            }
            if (it.crew!!.isNotEmpty()) {
                setupRecyclerCrew(it.crew)
            }
        }
    }

    private fun getCreditsTv(serialID: Int) {
        detailViewModel.getCreditsTv(serialID)
        detailViewModel.credits.observe(this) {
            if (it.cast!!.isNotEmpty()) {
                setupRecyclerCast(it.cast)
            }
            if (it.crew!!.isNotEmpty()) {
                setupRecyclerCrew(it.crew)
            }
        }
    }

    private fun setupRecyclerCast(cast: List<Credits.Cast>) {
        if (!::castAdapter.isInitialized) {
            castAdapter = CastAdapter(ArrayList(cast), this)
            binding.recyclerCast.adapter = castAdapter
            val layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerCast.layoutManager = layoutManager
            binding.recyclerCast.recycledViewPool.setMaxRecycledViews(0, 0)
        } else {
            castAdapter.refreshData(cast)
        }
    }

    private fun setupRecyclerCrew(crew: List<Credits.Crew>) {
        if (!::crewAdapter.isInitialized) {
            crewAdapter = CrewAdapter(ArrayList(crew), this)
            binding.recyclerCrew.adapter = crewAdapter
            val layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerCrew.layoutManager = layoutManager
            binding.recyclerCrew.recycledViewPool.setMaxRecycledViews(0, 0)
        } else {
            crewAdapter.refreshData(crew)
        }
    }

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

    override fun itemSelected(credits: Credits.Cast) {
        showToast("cast")
    }

    override fun itemSelected(credits: Credits.Crew) {
        showToast("crew")
    }
}