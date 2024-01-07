package ir.abolfazl.abolmovie.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.databinding.ActivityDetailBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
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

        val dataMovie = intent.getParcelableExtra<Movie_Tv.Result>("SendData")

        glide
            .load(BASE_URL_IMAGE + dataMovie!!.posterPath)
            .transform(RoundedCornersTransformation(32, 8))
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
}