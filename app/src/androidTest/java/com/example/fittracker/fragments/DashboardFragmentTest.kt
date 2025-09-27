package com.example.fittracker.ui.dashboard

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fittracker.R
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches

@RunWith(AndroidJUnit4::class)
class DashboardFragmentTest {

    @Test
    fun dashboard_showsGrayWhenNoWorkouts() {
        launchFragmentInContainer<DashboardFragment>()

        // Body graph container exists
        onView(withId(R.id.body_container)).check(matches(isDisplayed()))

        // Best muscle groups text is shown with "-"
        onView(withId(R.id.best_muscle_groups))
            .check(matches(withText(containsString("-"))))
    }
}