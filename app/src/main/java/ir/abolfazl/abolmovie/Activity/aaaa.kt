package com.pierfrancescosoffritti.androidyoutubeplayer.core.sampleapp.utils

import android.app.Activity
import android.view.View

class FullscreenHelper(private val context: Activity, vararg views: View) {
    private val views: Array<View>


    init {
        this.views = views as Array<View>
    }

    /**
     * call this method to enter full screen
     */
    fun enterFullscreen() {
        val decorView = context.window.decorView
        hideSystemUi(decorView)
        for (view in views) {
            view.visibility = View.GONE
            view.invalidate()
        }
    }

    /**
     * call this method to exit full screen
     */
    fun exitFullscreen() {
        val decorView = context.window.decorView
        showSystemUi(decorView)
        for (view in views) {
            view.visibility = View.VISIBLE
            view.invalidate()
        }
    }

    private fun hideSystemUi(mDecorView: View) {
        mDecorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun showSystemUi(mDecorView: View) {
        mDecorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }
}