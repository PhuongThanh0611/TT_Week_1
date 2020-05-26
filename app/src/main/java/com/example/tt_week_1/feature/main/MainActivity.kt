package com.example.tt_week_1.feature.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt_week_1.R
import com.example.tt_week_1.RepositoryMovie
import com.example.tt_week_1.data.Result
import com.example.tt_week_1.feature.main.adapter.AdapterMovie
import com.example.tt_week_1.feature.main.adapter.MovieListener
import com.example.tt_week_1.service.APIMovie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MovieListener {

   /* private val mAdapter: AdapterMovie by lazy {
        AdapterMovie(arrayListOf(),this)
    }*/
   private lateinit var mAdapter: AdapterMovie
    private var mCount: Int = 0
    private var mapList = HashMap<Int, Int>()
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        setEvents()
        requestMovie(1)
    }

    private fun setEvents() {
        swipeContainer.setOnRefreshListener {
            requestMovie(1)
            swipeContainer.isRefreshing = false
            mAdapter.removeData()
        }
    }

    // khởi tao recyclerview
    private fun initRecyclerView() {
        mAdapter = AdapterMovie(arrayListOf(),this)
        rvMovie.adapter =mAdapter
        rvMovie.setHasFixedSize(true)

    }

    private fun requestMovie(page: Int) {
        //io(): Tạo ra và trả về 1 Scheduler với mục đích xử lý các công việc không mang nặng tính chất tính toán
        RepositoryMovie.createService(APIMovie::class.java)
            .getMovie("a07e22bc18f5cb106bfe4cc1f83ad8ed", page)
            .observeOn(AndroidSchedulers.mainThread())//nhận vào tham số là 1 Scheduler sẽ làm cho các Operator hay Subscriber được gọi đằng sau nó chạy trên thread được cung cấp bởi Scheduler đó.
            .subscribeOn(Schedulers.io())//nhận vào tham số là 1 Scheduler, sẽ quyết định việc xử lý các phần tính toán để tạo nên 1 Observable trên thread cung cấp bởi Scheduler đó.
            // 1 Scheduler sẽ định nghĩa ra thread để chạy 1 khối lượng công việc.
            .subscribe(//
                //cú pháp của rxjava trong kotlin
                { result ->
                    //request thành công
                    Log.d("arrmovie", result.toString())
                    mAdapter.AddList(result.results)
                    /*handleSuccessMovie(result)*/
                },
                { error ->
                    //request thất bai
                    handlerErrorMovie(error)
                }
            )
    }

    //Xử lí dữ liệu khi request thành công
/*
    private fun handleSuccessMovie(result: ResultApi) {
        mAdapter = AdapterMovie(result.results, this)
        rvMovie.adapter = mAdapter
        mAdapter?.notifyDataSetChanged()
    }
*/

    //Xử lí dữ lieu request thất bại
    private fun handlerErrorMovie(error: Throwable) {
        Log.e(
            com.example.tt_week_1.ext.TAG,
            "handlerErrorAndroidVersion: ${error.localizedMessage}"
        )
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }

    override fun onClickItem(item: Result) {

        val intent = Intent(this, MainActivityTrailer::class.java)
        intent.putExtra("id", item.id)
        intent.putExtra("title", item.title)
        intent.putExtra("date", item.releaseDate)
        intent.putExtra("voteAverage", item.voteAverage)
        intent.putExtra("overview", item.overview)

        if (mapList.containsKey(item.id))
            intent.putExtra("tgian", mapList[item.id])
        startActivityForResult(intent, 999)
    }

    //load thêm trang sau khi danh sách phim sắp hết
    override fun onLoadMore() {
        currentPage++
        requestMovie(currentPage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED && requestCode == 999) {
            if (data != null) {
                mCount = data.getIntExtra("counter", 0)
                val idTv = data.getIntExtra("id", 0)
                mapList[idTv] = mCount
                Log.d("bbb", "$mCount $idTv")
            }
        }
    }
}
