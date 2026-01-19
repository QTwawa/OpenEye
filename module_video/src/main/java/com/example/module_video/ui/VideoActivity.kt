package com.example.module_video.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_video.LikeAnimation
import com.example.module_video.R
import com.example.module_video.adapter.OthersAdapter
import com.example.module_video.bean.VideoDataHelper
import com.example.module_video.databinding.ActivityVideoBinding
import com.example.module_video.viewmodel.OthersViewModel
import xyz.doikki.videocontroller.StandardVideoController


@Route(path="/video/VideoActivity")
class VideoActivity : AppCompatActivity() {
    private val mBinding: ActivityVideoBinding by lazy {
        ActivityVideoBinding.inflate(layoutInflater)
    }
    private val mOthersViewModel: OthersViewModel by lazy {
        OthersViewModel()
    }
    private val mAdapter: OthersAdapter by lazy {
        OthersAdapter()
    }
    private val dbHelper = VideoDataHelper(this)
    private lateinit var url:String
    private var isStared=false
    private var isLiked=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        ARouter.getInstance().inject(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.white, null)
        initView()
        initEvent()
    }


    @SuppressLint("SetTextI18n")
    private fun initView() {
        mBinding.apply {
            itemTvTitle.text=intent.getStringExtra("title")
            tvAuthor.text=intent.getStringExtra("author")
            tvDescription.text=intent.getStringExtra("description")
            tvLikes.text=intent.getIntExtra("likes",0).toString()
            tvTag.text="#"+intent.getStringExtra("tag")
            tvStar.text=intent.getIntExtra("star",0).toString()
            toolbar.title=intent.getStringExtra("title")
            url= intent.getStringExtra("url").toString()
        }
        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        mBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val videoFeeds = dbHelper.getVideoFeed(url)
        isLiked=videoFeeds.liked
        isStared=videoFeeds.collected
        if(isLiked){
            mBinding.ivLikes.setImageResource(R.drawable.liked)
            mBinding.tvLikes.text= (mBinding.tvLikes.text.toString().toInt()+1).toString()
        }
        if(isStared){
            mBinding.ivStar.setImageResource(R.drawable.stared)
            mBinding.tvStar.text= (mBinding.tvStar.text.toString().toInt()+1).toString()
        }
        mOthersViewModel.getOthersData(intent.getIntExtra("id",0))
        mOthersViewModel.othersData.observe(this@VideoActivity){
            val list = it.filter {element->
                element.type == "videoSmallCard"
            }
            mAdapter.submitList(list)
        }
        mBinding.rvOthers.apply {
            adapter = mAdapter
            layoutManager= LinearLayoutManager(this@VideoActivity)
        }
        playVideo(url)
    }


    @SuppressLint("SetTextI18n")
    private fun initEvent() {
        mBinding.ivLikes.setOnClickListener {
           if(!isLiked){
               mBinding.ivLikes.setImageResource(R.drawable.liked)
               mBinding.tvLikes.text= (mBinding.tvLikes.text.toString().toInt()+1).toString()
               LikeAnimation.animateLike(mBinding.ivLikes)
               updateVideoFeed(url,isLiked,isStared)
               isLiked=true
           }else{
               mBinding.ivLikes.setImageResource(R.drawable.like)
               mBinding.tvLikes.text= (mBinding.tvLikes.text.toString().toInt()-1).toString()
               LikeAnimation.animateLike(mBinding.ivLikes)
               updateVideoFeed(url,isLiked,isStared)
               isLiked=false
           }
        }
        mBinding.ivStar.setOnClickListener {
            if(!isStared){
                mBinding.ivStar.setImageResource(R.drawable.stared)
                mBinding.tvStar.text= (mBinding.tvStar.text.toString().toInt()+1).toString()
                LikeAnimation.animateLike(mBinding.ivStar)
                updateVideoFeed(url,isLiked,isStared)
                isStared=true
            }else{
                mBinding.ivStar.setImageResource(R.drawable.star)
                mBinding.tvStar.text= (mBinding.tvStar.text.toString().toInt()-1).toString()
                LikeAnimation.animateLike(mBinding.ivStar)
                updateVideoFeed(url,isLiked,isStared)
                isStared=false
            }
        }
        //视频分享
        mBinding.ivShare.setOnClickListener {
           val shareIntent = Intent().apply {
               action = Intent.ACTION_SEND
               type = "text/plain"
               putExtra(Intent.EXTRA_TEXT, url)
           }
           startActivity(Intent.createChooser(shareIntent, "分享视频到"))
        }
    }

    private fun updateVideoFeed(url: String, isLiked: Boolean, isStared: Boolean) {
        dbHelper.insertVideoFeed(url, isLiked, isStared)
    }

    private fun playVideo(url: String) {
        val controller = StandardVideoController(this)
        controller.addDefaultControlComponent(mBinding.itemTvTitle.text.toString(), false)
        mBinding.video.setVideoController(controller) //设置控制器
        mBinding.video.setUrl(url) //设置视频地址
        mBinding.video.start() //开始播放，不调用则不自动播放
    }

    override fun onPause() {
        super.onPause()
        mBinding.video.pause()
    }

    override fun onResume() {
        super.onResume()
        mBinding.video.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding.video.release()
    }
    override fun onBackPressed() {
        if (!mBinding.video.onBackPressed()) {
            super.onBackPressed()
        }
    }


    override fun finish() {
        val record=getSharedPreferences(intent.getIntExtra("id",0).toString(), MODE_PRIVATE).edit()
        record.apply {
            putBoolean("isLiked",isLiked)
            putBoolean("isStared",isStared)
            apply()
        }
        super.finish()
    }
}
