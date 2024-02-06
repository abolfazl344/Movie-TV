package ir.abolfazl.abolmovie.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ir.abolfazl.abolmovie.databinding.ItemRecyclerCreditsBinding
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE
import ir.abolfazl.abolmovie.model.Local.Credits

class CrewAdapter(private val data: ArrayList<Credits.Crew>, val selectedItem: ItemSelected) :
    RecyclerView.Adapter<CrewAdapter.MainViewHolder>() {
    lateinit var binding: ItemRecyclerCreditsBinding

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(credits: Credits.Crew) {

                Glide
                    .with(itemView.context)
                    .load(BASE_URL_IMAGE + credits.profilePath)
                    .apply(
                        RequestOptions()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    )
                    .into(binding.circleImageView)

            binding.txtNameActor.text = credits.name
            binding.txtNameCharacter.text = credits.knownForDepartment

            itemView.setOnClickListener {
                selectedItem.itemSelected(credits)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerCreditsBinding.inflate(layoutInflater, parent, false)
        return MainViewHolder(binding.root)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun refreshData(newData: List<Credits.Crew>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    interface ItemSelected {
        fun itemSelected(credits: Credits.Crew)
    }

}