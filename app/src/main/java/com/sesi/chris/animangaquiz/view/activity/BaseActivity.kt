package com.sesi.chris.animangaquiz.view.activity

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.sesi.chris.animangaquiz.R

open class BaseActivity : AppCompatActivity() {

    fun setStatusBarGradiant(activity: Activity) {
        val window = activity.window
        val background =
            ContextCompat.getDrawable(activity.applicationContext, R.drawable.black_gradient)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor =
            ContextCompat.getColor(activity.applicationContext, android.R.color.transparent)
        window.navigationBarColor =
            ContextCompat.getColor(activity.applicationContext, android.R.color.transparent)
        window.setBackgroundDrawable(background)
        changeStatusBarContrastStyle(window)
    }

    private fun changeStatusBarContrastStyle(window: Window) {
        val decorView = window.decorView
        val windowInsetController = WindowCompat.getInsetsController(window, decorView)
        windowInsetController.isAppearanceLightStatusBars = true
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        val configuration = Configuration(newBase.resources.configuration)
        configuration.fontScale = 1.0f
        applyOverrideConfiguration(configuration)
    }
}