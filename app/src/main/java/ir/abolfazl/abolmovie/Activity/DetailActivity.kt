package ir.abolfazl.abolmovie.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.databinding.ActivityDetailBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.utils.Extensions.showToast
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import javax.inject.Inject


@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    @Inject
    lateinit var glide: RequestManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadTrailer()

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
        } else if (dataMovie.firstAirDate != null) {
            binding.txtRelease.text = dataMovie.firstAirDate
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

    private fun loadTrailer() {
        lifecycle.addObserver(binding.playerDetail)

        val listener = object : AbstractYouTubePlayerListener(){
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // using pre-made custom ui
                val defaultPlayerUiController = DefaultPlayerUiController(binding.playerDetail, youTubePlayer)
                binding.playerDetail.setCustomPlayerUi(defaultPlayerUiController.rootView)
                youTubePlayer.cueVideo("6TGg0_xtLoA",0f)
            }
        }
        val option = IFramePlayerOptions.Builder().controls(0).build()
        binding.playerDetail.initialize(listener,option)
        binding.playerDetail.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                showToast("dsdaadadadad")
            }

            override fun onExitFullscreen() {

            }

        })


    }
}