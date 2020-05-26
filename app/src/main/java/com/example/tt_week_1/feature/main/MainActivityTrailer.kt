package com.example.tt_week_1.feature.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tt_week_1.R
import com.example.tt_week_1.RepositoryMovie
import com.example.tt_week_1.ext.Trailer_ApiKey
import com.example.tt_week_1.ext.Youtube_API
import com.example.tt_week_1.service.APIMovie
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_trailer.*

class MainActivityTrailer : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private var player: YouTubePlayer? = null
    private var resultSource: String? = null
    private var mCounter: Int = 0
    private val stateCount: String = "counter"
    private var id :Int =0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_trailer)
            id = intent.getIntExtra("id", 0)
        val title = intent.getStringExtra("title")
        val date = intent.getStringExtra("date")
        val voteAverage = intent.getDoubleExtra("voteAverage", 0.0) / 2
        val overview = intent.getStringExtra("overview")
          mCounter=intent.getIntExtra("tgian",0)
        txtTitle.text = title
        txtDate.text = "Release Date :$date"
        rt_star.rating = voteAverage.toFloat()
        txtOverview.text = overview

        player_trailer.initialize(Youtube_API, this)
        if (savedInstanceState != null) {
            mCounter = savedInstanceState.getInt(stateCount)
            player?.play()
        }else {
            requestTrailer(id)

        }

        //set event
        /*requestTrailer(id)*/
    }

    //lưu trạng thái video
    override fun onSaveInstanceState(p0: Bundle) {
        super.onSaveInstanceState(p0)
        player?.currentTimeMillis?.let { p0.putInt(stateCount, it) }

    }

    private fun requestTrailer(id: Int) {
        RepositoryMovie.createService(APIMovie::class.java)
            .getMovieTrailer(id, Trailer_ApiKey)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    //request thành công
                    Log.d("arrmovie", result.toString())
                    // trường hợp API gọi xong trc
                    resultSource = result.youtube.first().source
                    player?.cueVideo(resultSource, mCounter)
                    if(mCounter != 0)
                    player?.play()
                },
                { error ->
                    //request thất bai
                    Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
            )


    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        //trường hợp source gọi xong trc
        player = p1
        if (resultSource != null) {

            player?.cueVideo(resultSource, mCounter)
            if(mCounter != 0)
                player?.play()

        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {

    }
// khi chọn cancel
    override fun onBackPressed() {
        val i = Intent()
        i.putExtra(stateCount,player?.currentTimeMillis)
        i.putExtra("id",id)
        setResult(Activity.RESULT_CANCELED,i)
        super.onBackPressed()
    }
}


