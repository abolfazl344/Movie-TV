package ir.abolfazl.abolmovie.movieScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.databinding.ItemRecyclerMovieBinding
import ir.abolfazl.abolmovie.mainScreen.FragmentMain
import javax.inject.Inject

@AndroidEntryPoint
class MovieAdapter(private val data: ArrayList<Movie_Tv.Result>, val selectedItem: ItemSelected) : RecyclerView.Adapter<MovieAdapter.MainViewHolder>() {
    lateinit var binding : ItemRecyclerMovieBinding
    @Inject
    lateinit var glide : RequestManager
    inner class MainViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun bindData(movie: Movie_Tv.Result){

            glide
                .load(BASE_URL_IMAGE + movie.posterPath)
                .into(binding.imgItemMovie)

            itemView.setOnClickListener {
                selectedItem.itemSelected(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMovieBinding.inflate(layoutInflater,parent,false)
        return MainViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return FragmentMain.ItemCount
    }

    interface ItemSelected{

        fun itemSelected(movie: Movie_Tv.Result)
    }

}