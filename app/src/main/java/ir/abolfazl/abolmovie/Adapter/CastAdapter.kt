package ir.abolfazl.abolmovie.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import ir.abolfazl.abolmovie.databinding.ItemRecyclerCreditsBinding
import ir.abolfazl.abolmovie.model.Local.Credits
import ir.abolfazl.abolmovie.utils.BASE_URL_IMAGE_CREDITS

class CastAdapter(private val data: ArrayList<Credits.Cast>, val selectedItem: ItemSelected) :
    RecyclerView.Adapter<CastAdapter.MainViewHolder>() {
    lateinit var binding: ItemRecyclerCreditsBinding

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(credits: Credits.Cast) {
               Glide
                   .with(itemView.context)
                   .load(BASE_URL_IMAGE_CREDITS + credits.profilePath)
                   .transition(DrawableTransitionOptions.withCrossFade())
                   .apply(
                       RequestOptions()
                           .circleCrop()
                           .skipMemoryCache(true)
                           .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                   )
                   .into(binding.imageView)

               binding.txtNameActor.text = credits.name
               binding.txtNameCharacter.text = credits.character

            itemView.setOnClickListener {
                selectedItem.itemSelectedCast(credits)
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

    interface ItemSelected{
        fun itemSelectedCast(credits: Credits.Cast)
    }

}