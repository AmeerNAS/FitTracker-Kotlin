package com.example.fittracker

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
//! 3ten is a Backport of android date package for datetime data

class FitTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize ThreeTenABP
        AndroidThreeTen.init(this)
    }
}