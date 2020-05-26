
package com.example.tt_week_1.feature.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tt_week_1.R
import com.example.tt_week_1.data.Result
import com.example.tt_week_1.ext.Base_Img
import kotlinx.android.synthetic.main.item_poster.view.*
import kotlinx.android.synthetic.main.item_video.view.*

class AdapterMovie (private val listMovies: ArrayList<Result>, private var listener: MovieListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //private val itemClickListener: ItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder {
        return if(viewType ==0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poster, parent, false)
            ViewHolderPoster(    view      )
        } else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.framelayout_item_video, parent, false)
            ViewHolderVideo(    view      )
        }
    }

    override fun getItemCount() = listMovies.count()

    fun AddList(  listMovies: List<Result>){
        this.listMovies.apply {
            addAll(listMovies)
            notifyDataSetChanged()
        }
    }
    fun removeData(){
        listMovies.clear()
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(position==listMovies.size-5) listener.onLoadMore()
        // tra ve view layout
        if(getItemViewType(position)== 0){
            (holder as ViewHolderPoster).bind(listMovies[position],listener)

        }else{
            (holder as ViewHolderVideo).bind(listMovies[position],listener)

        }
    }

    override fun getItemViewType(position: Int): Int {
        //check xem la video hay poster
        val count = listMovies[position].voteAverage
        return if(count >5 ){
            1 // tra ve 1 neu la video
        }else{
            0
        } // mặc định ko có star sẽ là poster
    }

    class ViewHolderPoster(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(android: Result,itemClickListener: MovieListener) {
            // itemView.img_poster = android.
            Glide.with(view.context)
                .load(Base_Img+android.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(7)))
                .error(R.drawable.ic_launcher_background)
                .into(itemView.img_poster)
            itemView.txt_title.text = android.title
            itemView.txt_overview.text = android.overview
            // sự kiện click item
            itemView.setOnClickListener{
                itemClickListener.onClickItem(android)
            }
        }
    }
    class ViewHolderVideo(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(android: Result,itemClickListener: MovieListener) {
            // itemView.img_poster = android.
            Glide.with(view.context)
                .load(Base_Img+android.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(7)))
                .error(R.drawable.ic_launcher_background)
                .into(itemView.img_trailer)
            // bắt sự kiện click item
            itemView.setOnClickListener{
                itemClickListener.onClickItem(android)
            }
        }

    }

}
