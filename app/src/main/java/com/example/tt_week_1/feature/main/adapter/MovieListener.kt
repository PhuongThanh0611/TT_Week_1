package com.example.tt_week_1.feature.main.adapter

import com.example.tt_week_1.data.Result

interface MovieListener {
    fun onClickItem(item: Result)
    fun onLoadMore()
}