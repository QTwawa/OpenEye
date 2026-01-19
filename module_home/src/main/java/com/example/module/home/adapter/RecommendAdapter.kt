package com.example.module.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.module.home.R
import com.example.module.home.bean.Rec

/**
 * description : RecommendAdapter
 * author : QTwawa
 * date : 2024/7/16 15:40
 */
class RecommendAdapter :
    PagingDataAdapter<Rec, RecommendAdapter.RecommendViewHolder>(object :
        DiffUtil.ItemCallback<Rec>() {
        override fun areItemsTheSame(oldItem: Rec, newItem: Rec): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Rec, newItem: Rec): Boolean {
            return oldItem.title == newItem.title
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recommend, parent, false)
        return RecommendViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        val recommendData = getItem(position)
        holder.bind(recommendData!!)
    }
    fun interface OnClickedListener {
        fun onClicked(recommendData : Rec,view: View)
    }

    private var listener: OnClickedListener? = null
    fun setOnClickedListener(listener: OnClickedListener) {
        this.listener = listener
    }

    inner class RecommendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivRecommendCover: ImageView =
            itemView.findViewById(R.id.item_iv_recommend_cover)
        private val tvRecommendTitle: TextView = itemView.findViewById(R.id.item_tv_recommend_title)
        private val ivRecommendIcon: ImageView = itemView.findViewById(R.id.item_iv_recommend_icon)
        private val tvRecommendAuthor: TextView =
            itemView.findViewById(R.id.item_tv_recommend_author)
        private val ivRecommendType: ImageView = itemView.findViewById(R.id.item_iv_recommend_type)
        fun bind(data: Rec) {
            Glide.with(itemView.context)
                .load(data.coverUrl).apply(
                    RequestOptions().fitCenter())
                .into(ivRecommendCover)
            Glide.with(itemView.context)
                .load(data.icon).circleCrop()
                .into(ivRecommendIcon)



            ivRecommendType.setImageResource(R.drawable.ic_photo)
            ivRecommendCover.setOnClickListener {
//                        ARouter.getInstance().build("/video/PhotoGraphActivity")
//                            .withString("picture",data.coverUrl)
//                            .navigation(context.activity?.application?.applicationContext)
            listener?.onClicked(getItem(absoluteAdapterPosition)!!,ivRecommendCover)
                    }
            tvRecommendAuthor.text = data.author
            tvRecommendTitle.text = data.title
        }
    }
}
