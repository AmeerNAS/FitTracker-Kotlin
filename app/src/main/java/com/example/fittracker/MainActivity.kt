package com.example.fittracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fittracker.data.local.db.AppDatabase
import com.example.fittracker.databinding.ActivityMainBinding
import com.example.fittracker.ui.util.applySystemBarPadding

/**
 * MainActivity
 *
 * The primary Activity hosting the app's navigation and UI container.
 * single entry point for navigation and screen display
 * within the app, leveraging Jetpack Navigation for fragment management.
 *
 * Responsibilities:
 * - Sets up the main view binding and content view.
 * - Configures window insets to allow content to draw behind system bars.
 * - Initializes the Navigation Component with NavHostFragment and NavController.
 * - Connects the bottom navigation view with the NavController for: Home, Dashboard, and Exercises.
 * - Applies system bar padding to ensure proper layout adjustments with system UI.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.navHostFragmentActivityMain.applySystemBarPadding()
        // Correct way: get NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // Applys padding
        binding.navHostFragmentActivityMain.applySystemBarPadding()




        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_exercises
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }
}