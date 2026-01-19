package com.example.module.home.util

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * author : QTwawa
 * date : 2024/7/20 12:03
 */
class BannerTransformer:ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val pageHeight = page.height
        when {
            position < -1 -> {
                page.alpha = 0f
            }
            position <= 1 -> { // [-1,1]
                val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                page.translationX = if (position < 0) {
                    horzMargin - vertMargin / 2
                } else {
                    -horzMargin + vertMargin / 2
                }
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                page.alpha = (MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                        (1 - MIN_SCALE) * (1 - MIN_ALPHA))
            }
            else -> {
                page.alpha = 0f
            }
        }
    }
    companion object {

        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }
}