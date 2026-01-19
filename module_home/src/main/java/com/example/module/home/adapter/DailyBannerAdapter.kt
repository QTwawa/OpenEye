package com.example.module.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.module.home.R
import com.example.module.home.bean.DvItem

/**
 * description : banner的adapter
 * author : QTwawa
 * date : 2024/7/17 16:08
 */
class DailyBannerAdapter :
    ListAdapter<DvItem, DailyBannerAdapter.DailyBannerViewHolder>(object :
        DiffUtil.ItemCallback<DvItem>() {
        override fun areItemsTheSame(oldItem: DvItem, newItem: DvItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DvItem, newItem: DvItem): Boolean {
            return oldItem.data == newItem.data
        }
    }) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyBannerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_daily_banner, parent, false)
        return DailyBannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyBannerViewHolder, position: Int) {
//        if(currentList.size!=0){
//            val data = getItem(position % currentList.size)
//            holder.bind(data)
//        }
        val data = getItem(position % currentList.size)
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }


inner class DailyBannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val ivDailyCover: ImageView = itemView.findViewById(R.id.item_iv_home_daily_banner)
    private val tvDailyTitle: TextView = itemView.findViewById(R.id.item_tv_home_daily_banner)
    fun bind(data: DvItem) {
        Glide.with(itemView.context)
            .load(data.data.cover.detail.replace("http", "https"))
            .into(ivDailyCover)
        tvDailyTitle.text = data.data.title
    }
  }
}