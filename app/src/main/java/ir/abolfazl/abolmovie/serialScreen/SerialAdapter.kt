package ir.abolfazl.abolmovie.serialScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.abolfazl.abolmovie.apiManager.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.model.Serial
import ir.abolfazl.abolmovie.databinding.ItemRecyclerMovieBinding
import ir.abolfazl.abolmovie.fragment.FragmentMain

class SerialAdapter(val data: ArrayList<Serial.Result>, val selectedItem: ItemSelected) :
    RecyclerView.Adapter<SerialAdapter.MainViewHolder>() {
    lateinit var binding: ItemRecyclerMovieBinding

    inner class MainViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        fun bindData(serial: Serial.Result) {
            binding.txtItemMovieTitle.text = serial.originalName
            binding.txtScoreMovie.text = serial.voteAverage.toString()

            Glide
                .with(itemView)
                .load(BASE_URL_IMAGE + serial.posterPath)
                .into(binding.imgItemMovie)

            itemView.setOnClickListener {
                selectedItem.itemSelectedTv(serial)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMovieBinding.inflate(layoutinflater, parent, false)
        return MainViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return FragmentMain.ItemCount
    }

    interface ItemSelected {

        fun itemSelectedTv(serial: Serial.Result)
    }
}