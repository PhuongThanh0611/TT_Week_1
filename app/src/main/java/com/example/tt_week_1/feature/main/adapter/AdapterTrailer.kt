package com.example.tt_week_1.feature.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tt_week_1.R
import com.example.tt_week_1.data.Youtube
import com.example.tt_week_1.ext.Youtube_API
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_main_trailer.view.*

class AdapterTrailer(private var listTrailer: List<Youtube>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_main_trailer, parent, false)
        return ViewHolderTrailer(view)
    }

    override fun getItemCount(): Int {
       return listTrailer?.size?:0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolderTrailer) .bind(listTrailer[position])
    }
    class ViewHolderTrailer(val view: View) : RecyclerView.ViewHolder(view){
        fun bind(item:Youtube){
            itemView.txtTitle.text=item.name
            itemView.player_trailer.initialize(Youtube_API, object :
                YouTubePlayer.OnInitializedListener{
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer?,
                    p2: Boolean
                ) {
                    if (p1 != null) {
                        p1.cueVideo(item.source)
                    }
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                    if (p1 != null) {
                        if(p1.isUserRecoverableError){
                        }
                    }
                }

            })
        }
    }

}