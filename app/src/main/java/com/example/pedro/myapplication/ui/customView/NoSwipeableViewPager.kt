package com.example.pedro.myapplication.ui.customView

import android.content.Context
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import java.lang.reflect.AccessibleObject.setAccessible
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoSwipeableViewPager(context: Context, attrs: AttributeSet? = null): ViewPager(context, attrs) {

    init {
        setMyScroller()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Never allow swiping to switch between pages
        return false
    }

    //down one is added for smooth scrolling

    private fun setMyScroller() {
        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, MyScroller(getContext()))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class MyScroller(context: Context) : Scroller(context, DecelerateInterpolator()) {

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/)
        }
    }
}