package com.example.fittracker.ui.util

import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Applies padding to a View so its content avoids system bars
 * (status bar at the top, navigation bar at the bottom).
 */
fun View.applySystemBarPadding(applyTop: Boolean = true, applyBottom: Boolean = true) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

        //unchanged
        val left = v.paddingLeft
        val right = v.paddingRight

        // Manual Padding
        val actionBarHeight = if (applyTop) {
            val tv = TypedValue()
            if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            } else 0
        } else 0

        val top = if (applyTop) actionBarHeight else v.paddingTop
        // I could not figure out a way to avoid hard code
        val bottom = if (applyBottom) systemBars.bottom + 120 else v.paddingBottom

        //Log.d("InsetsDebug", "Applying insets: top=${top}, bottom=${bottom}")
        v.setPadding(left, top, right, bottom)
        insets
    }
    ViewCompat.requestApplyInsets(this)

}