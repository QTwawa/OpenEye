package com.example.module.home.ui.fragment

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module.home.ViewModel.DailyViewModel
import com.example.module.home.adapter.DailyRvAdapter
import com.example.module.home.bean.DrData
import com.example.module.home.databinding.FgHomeDailyBinding
import com.example.module.home.helper.BannerHelper
import com.example.module_video.ui.VideoActivity
import kotlinx.coroutines.launch

/**
 * description : RecommendViewModel
 * author : QTwawa
 * date : 2024/7/17 15:19
 */

class DailyFragment : Fragment() {
    private var _binding: FgHomeDailyBinding? = null
    private val binding get() = _binding!!
    private val mDailyViewModel: DailyViewModel by lazy {
        ViewModelProvider(this)[DailyViewModel::class.java]
    }
    private val mRvAdapter: DailyRvAdapter by lazy {
        DailyRvAdapter()
    }
    private val bannerHelper by lazy {
        BannerHelper()
    }
    private var mUrl: String = "" //Rv下一个Url

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FgHomeDailyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvView()
        initBanner()
        onScroll()
    }

    private fun initBanner() {
        mDailyViewModel.dailyVpData.observe(viewLifecycleOwner) {
            val list = it
            mRvAdapter.onInitBanner { adapter ->
                adapter.submitBannerList(list)
                bannerHelper.initBanner(adapter)
                bannerHelper.startRun()
            }
        }
    }

    private fun initRvView() {
//        mDailyViewModel.dailyRvData.observe(viewLifecycleOwner) {
//            val list = it?.filter { element ->
//                element.type == "videoSmallCard"
//            }
//            mRvAdapter.submitList(list)
//        }
        lifecycleScope.launch {
            mDailyViewModel.dailyData.collect{
                mRvAdapter.submitData(it)
            }
        }
        mDailyViewModel.url.observe(viewLifecycleOwner) {
            mUrl = it
        }
        binding.rvHomeDaily.apply {
            adapter = mRvAdapter
            layoutManager = LinearLayoutManager(context)
        }
        mRvAdapter.setOnClickedListener{ dailyData:DrData,view:View->
            val intent=Intent(context,VideoActivity::class.java)
            intent.apply {
                putExtra("title",dailyData.title)
                putExtra("author",dailyData.author.name)
                putExtra("description",dailyData.description)
                putExtra("likes",dailyData.consumption.collectionCount)
                putExtra("tag",dailyData.category)
                putExtra("share",dailyData.consumption.shareCount)
                putExtra("star",dailyData.consumption.realCollectionCount)
                putExtra("url",dailyData.playUrl)
                putExtra("id",dailyData.id)
            }
            startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(requireActivity(),view,"sharedImage").toBundle())
        }
        //刷新再次请求数据
        binding.srlHomeDaily.setOnRefreshListener {
            binding.srlHomeDaily.postDelayed({
                mDailyViewModel.getDailyVpData()
//                mDailyViewModel.getDailyRvData()
                mRvAdapter.retry()
                binding.srlHomeDaily.isRefreshing = false
            },2000)
        }
        //没有网的时候就报
        mDailyViewModel.isConnect.observe(viewLifecycleOwner) {
            if (!it) {
               Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onScroll() {
       // 滑到下面加载更多
//        mBinding.rvHomeDaily.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (!recyclerView.canScrollVertically(1)) {
//                    mDailyViewModel.getNextDailyRvData(mUrl)
//                }
//            }
//        })
        //判断是否阻拦横向滑动
        val recyclerViewTouchListener = View.OnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE||event.action == MotionEvent.ACTION_DOWN) {
                val childView = binding.rvHomeDaily.findChildViewUnder(event.x, event.y)
                if (childView != null) {
                    val position = binding.rvHomeDaily.getChildAdapterPosition(childView)
                    binding.rvHomeDaily.isScrollEnabled = position > 1
                }
            }
            false
        }
        binding.rvHomeDaily.setOnTouchListener(recyclerViewTouchListener)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        bannerHelper.destroy()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}