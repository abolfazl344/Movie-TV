package ir.abolfazl.abolmovie.detailScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.R
import ir.abolfazl.abolmovie.databinding.ActivityDetailBinding
import ir.abolfazl.abolmovie.model.Local.Movie_Tv

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding
    private val args: DetailActivityArgs by navArgs()
    private lateinit var dataMovie: Movie_Tv.Result
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataMovie = args.dataMovie

        detailViewModel.dataMovie = dataMovie

        if (dataMovie.releaseDate != null) {
            //getMovieTrailer(dataMovie.id)
            detailViewModel.getRecommendMovie(dataMovie.id)
            detailViewModel.getCreditsMovie(dataMovie.id)
        } else if (dataMovie.firstAirDate != null) {
            //getTvTrailer(dataMovie.id)
            detailViewModel.getRecommendTv(dataMovie.id)
            detailViewModel.getCreditsTv(dataMovie.id)
        }

    }

}