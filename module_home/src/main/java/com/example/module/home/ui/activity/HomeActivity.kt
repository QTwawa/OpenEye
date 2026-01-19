package com.example.module.home.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.module.home.databinding.ActivityHomeBinding
import com.example.module.home.ui.fragment.HomeFragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.model_hot.ui.HotFragment
import com.example.module.community.CommunityFragment
import com.example.module.found.ui.fragment.FoundFragment
import com.example.module.home.R
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * @author: QT
 * @date: 2024/7/16
 * description:主框架
 */

@Route(path = "/home/HomeActivity")
class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(android.R.color.transparent, null)
        ARouter.getInstance().inject(this)
        initView()
    }

    @SuppressLint("CommitTransaction")
    private fun initView() {
        val communityFragment = CommunityFragment()
        val foundFragment = FoundFragment()
        val homeFragment = HomeFragment()
        val hotFragment = HotFragment()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fg_home_view, homeFragment).commit()
        supportFragmentManager.beginTransaction().apply {
            add(R.id.fg_home_view, foundFragment)
            add(R.id.fg_home_view, communityFragment)
            add(R.id.fg_home_view, hotFragment)
            hide(foundFragment)
            hide(communityFragment)
            hide(hotFragment)
        }.commit()
        binding.btnHomeNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> {
                    supportFragmentManager.beginTransaction().apply {
                        hide(foundFragment)
                        hide(communityFragment)
                        hide(hotFragment)
                        show(homeFragment)
                    }.commit()
                }
                R.id.item_found -> {
                    supportFragmentManager.beginTransaction().apply {
                        hide(homeFragment)
                        hide(communityFragment)
                        hide(hotFragment)
                        show(foundFragment)
                    }.commit()
                }

                R.id.item_community -> {
                    supportFragmentManager.beginTransaction().apply {
                        hide(foundFragment)
                        hide(homeFragment)
                        hide(hotFragment)
                        show(communityFragment)
                    }.commit()
                }
                R.id.item_mine -> {
                    supportFragmentManager.beginTransaction().apply {
                        hide(foundFragment)
                        hide(communityFragment)
                        hide(homeFragment)
                        show(hotFragment)
                    }.commit()
                }
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.btnHomeNavigation.setOnItemSelectedListener(null)
        _binding=null
    }
}
