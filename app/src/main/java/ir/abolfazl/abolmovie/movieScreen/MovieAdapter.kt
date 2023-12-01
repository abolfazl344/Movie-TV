package ir.abolfazl.abolmovie.movieScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.abolfazl.abolmovie.apiManager.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.model.Movie_Tv
import ir.abolfazl.abolmovie.databinding.ItemRecyclerMovieBinding
import ir.abolfazl.abolmovie.mainScreen.FragmentMain

class MovieAdapter(val data: ArrayList<Movie_Tv.Result>, val selectedItem: ItemSelected) : RecyclerView.Adapter<MovieAdapter.MainViewHolder>() {
    lateinit var binding : ItemRecyclerMovieBinding

    inner class MainViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){

        fun bindData(movie: Movie_Tv.Result){
            if(movie.title != null){
                binding.txtItemMovieTitle.text = movie.title
            }else if(movie.name != null){
                binding.txtItemMovieTitle.text = movie.name
            }

            binding.txtScoreMovie.text = movie.voteAverage.toString()

            Glide
                .with(itemView)
                .load(BASE_URL_IMAGE + movie.posterPath)
                .into(binding.imgItemMovie)

            itemView.setOnClickListener {
                selectedItem.itemSelected(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMovieBinding.inflate(layoutinflater,parent,false)
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