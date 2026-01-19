package com.example.module_video.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.module_video.R

/**
 * author : QTwawa
 * date : 2024/7/24 15:21
 */
class PhotoAdapter(list: List<String>): RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {
    private val photoData=list
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoAdapter.PhotoViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_photo,parent,false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoAdapter.PhotoViewHolder, position: Int) {
        val data=photoData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
      return photoData.size
    }
    fun interface OnClickedListener {
        fun onClicked(view: View)
    }

    private var listener: OnClickedListener? = null
    fun setOnClickedListener(listener: OnClickedListener) {
        this.listener = listener
    }

    inner class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_picture)
        private val page:TextView=itemView.findViewById(R.id.tv_page)

        @SuppressLint("SetTextI18n")
        fun bind(data: String){
            Glide.with(itemView.context).load(data).apply(RequestOptions().fitCenter()).into(imageView)
            page.text= (absoluteAdapterPosition+1).toString() + "/" + photoData.size.toString()
            imageView.setOnClickListener {
                listener?.onClicked(imageView)
            }
        }
    }
}