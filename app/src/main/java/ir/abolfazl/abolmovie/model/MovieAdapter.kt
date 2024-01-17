package ir.abolfazl.abolmovie.model

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.model.Local.Movie_Tv
import ir.abolfazl.abolmovie.databinding.ItemRecyclerMovieBinding

class MovieAdapter(private val data: ArrayList<Movie_Tv.Result>, val selectedItem: ItemSelected) :
    RecyclerView.Adapter<MovieAdapter.MainViewHolder>() {
    lateinit var binding: ItemRecyclerMovieBinding

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(movie: Movie_Tv.Result) {

            Glide
                .with(itemView.context)
                .load(BASE_URL_IMAGE + movie.posterPath)
                .apply(
                    RequestOptions()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                )
                .into(binding.imgItemMovie)

            itemView.setOnClickListener {
                selectedItem.itemSelected(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMovieBinding.inflate(layoutInflater, parent, false)
        return MainViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(newData: List<Movie_Tv.Result>) {
        val startPosition = data.size
        data.addAll(newData)
        notifyItemRangeInserted(startPosition,newData.size)
    }

    fun refreshData(newData: List<Movie_Tv.Result>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
    fun clearData(){
        data.clear()
        notifyDataSetChanged()
    }
    interface ItemSelected {

        fun itemSelected(movie: Movie_Tv.Result)
    }

}