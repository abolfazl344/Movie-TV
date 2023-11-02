package ir.abolfazl.abolmovie.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import ir.abolfazl.abolmovie.apiManager.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.model.Movie
import ir.abolfazl.abolmovie.model.Serial
import ir.abolfazl.abolmovie.databinding.ActivityDetailBinding
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataMovie = intent.getParcelableExtra<Movie.Result>("dataMovie")
        val dataSerial = intent.getParcelableExtra<Serial.Result>("dataSerial")

        if (dataMovie != null) {

            Glide
                .with(this)
                .load(BASE_URL_IMAGE + dataMovie.posterPath)
                .transform(RoundedCornersTransformation(32, 8))
                .into(binding.imgPoster)

            Glide
                .with(this)
                .load(BASE_URL_IMAGE + dataMovie.backdropPath)
                .into(binding.imgBackImage)

            binding.txtRelease.text = dataMovie.releaseDate
            binding.txtScore.text = dataMovie.voteAverage.toString()
            binding.txtInfoMovie.text = dataMovie.overview
            binding.txtTitle.text = dataMovie.title
            binding.txtTime.text = dataMovie.originalLanguage

            binding.imgBackToHome.setOnClickListener {
                onBackPressed()
            }
        }

        if (dataSerial != null) {

            Glide
                .with(this)
                .load(BASE_URL_IMAGE + dataSerial.posterPath)
                .transform(RoundedCornersTransformation(32, 8))
                .into(binding.imgPoster)

            Glide
                .with(this)
                .load(BASE_URL_IMAGE + dataSerial.backdropPath)
                .into(binding.imgBackImage)

            binding.txtRelease.text = dataSerial.firstAirDate
            binding.txtScore.text = dataSerial.voteAverage.toString()
            binding.txtInfoMovie.text = dataSerial.overview
            binding.txtTitle.text = dataSerial.name
            binding.txtTime.text = dataSerial.originalLanguage

            binding.imgBackToHome.setOnClickListener {
                onBackPressed()
            }
        }
    }
}