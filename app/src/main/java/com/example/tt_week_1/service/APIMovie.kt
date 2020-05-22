package com.example.tt_week_1.service

import com.example.tt_week_1.data.ResultApi
import com.example.tt_week_1.data.Trailer
import com.example.tt_week_1.ext.ConstExt.Companion.Trailer_ApiKey
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface APIMovie {
    @GET("now_playing")
    fun getMovie(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Observable<ResultApi>
    @GET("{id}/trailers")
    fun getMovieTrailer(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String= Trailer_ApiKey
    ):Observable<Trailer>

}