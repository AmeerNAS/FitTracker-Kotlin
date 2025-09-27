package com.example.fittracker.fragments

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith


@UninstallModules(AppModule::class)
@RunWith(AndroidJUnit4::class)
class DashboardFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun showsGrayBodyWhenNoWorkouts() {
        launchFragmentInContainer<DashboardFragment>()
        onView(withId(R.id.body_container)).check(matches(isDisplayed()))
        onView(withId(R.id.bestMuscleGroups)).check(matches(withText(containsString("-"))))
    }

    @Test
    fun updatesColorsAfterWorkoutLogs() {
        // Prepopulate DB with some workout
        // Then launch fragment and verify color change
    }
}