package com.example.module.home.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lib_net.RetrofitClient
import com.example.module.home.bean.DrData
import com.example.module.home.service.DailyRvService

/**
 * author : QTwawa
 * date : 2024/7/25 15:08
 */
class DailySource(): PagingSource<Int, DrData>() {
    override fun getRefreshKey(state: PagingState<Int, DrData>): Int? =null
    private var nextParams = ""

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DrData> {
        return try {
            val page = params.key ?: 1
            val homeData = if (nextParams == "")
                RetrofitClient.getService(DailyRvService::class.java).getDailyRvData("", "","")
            else RetrofitClient.getService(DailyRvService::class.java)
                .getDailyRvData(
                    nextParams.split("&")[0].split("=")[1],
                    nextParams.split("&")[1].split("=")[1],
                    nextParams.split("&")[2].split("=")[1],
                )
            nextParams = homeData.nextPageUrl.substring(
                52,
                homeData.nextPageUrl.length
            )
            val realData = mutableListOf<DrData>()
            for (data in homeData.itemList) {
                if (data.type == "videoSmallCard") {
                    data.data.run {
                        realData.add(
                            DrData(
                                author,
                                category,
                                consumption,
                                cover,
                                date,
                                description,
                                duration,
                                id,
                                playUrl,
                                title,
                            )
                        )
                    }
                }
            }
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (nextParams != "") page + 1 else null
            LoadResult.Page(realData, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}