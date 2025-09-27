package com.example.fittracker.ui.exercise

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fittracker.R
import com.example.fittracker.ui.exercises.detail.RunningActivity
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseRunningTimerActivityTest {

    @Test
    fun finishButton_opensMarkAsDoneDialog_withRunningFields() {
        ActivityScenario.launch(RunningActivity::class.java)

        onView(withId(R.id.btnFinish)).perform(click())

        onView(withText("Mark Run as Done"))
            .check(matches(isDisplayed()))

        onView(withText(containsString("distance")))
            .check(matches(isDisplayed()))
    }
}