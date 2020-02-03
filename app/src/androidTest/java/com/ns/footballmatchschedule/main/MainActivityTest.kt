package com.ns.footballmatchschedule.main

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import com.ns.footballmatchschedule.MainActivity
import com.ns.footballmatchschedule.R
import com.ns.footballmatchschedule.util.MainIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.support.test.InstrumentationRegistry
import com.ns.footballmatchschedule.db.database
import com.ns.footballmatchschedule.model.Favorite
import org.jetbrains.anko.db.delete

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(MainIdlingResource)
        InstrumentationRegistry.getTargetContext().database.use {
            delete(
                Favorite.TABLE_FAVORITE
            )
        }
    }

    @After
    fun done() {
        IdlingRegistry.getInstance().unregister(MainIdlingResource)
    }

    @Test
    fun testAppBehaviour() {
        //check menu last match
        onView(withId(R.id.bottom_navigation)).check(matches(isDisplayed()))

        onView(withText("Last Matches")).check(matches(isDisplayed()))

        onView(withId(R.id.list_match)).check(matches(isDisplayed()))

        //scroll to item on position 10
        onView(withId(R.id.list_match)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10)
        )

        //click item on position 10
        onView(withId(R.id.list_match)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click())
        )

        //check detail activity
        onView(withText("Match Detail")).check(matches(isDisplayed()))

        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))

        //click favorite
        onView(withId(R.id.add_to_favorite)).perform(click())

        //check favorite
        onView(withText("Added to favorite")).check(matches(isDisplayed()))

        //click back button
        Espresso.pressBack()

        //click menu favorite
        onView(withId(R.id.menu_favorites)).perform(click())

        //check menu favorite
        onView(withText("Favorites")).check(matches(isDisplayed()))

        onView(withId(R.id.list_match)).check(matches(isDisplayed()))

        //click item on position 0
        onView(withId(R.id.list_match)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        //check detail activity
        onView(withText("Match Detail")).check(matches(isDisplayed()))

        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))

        //click favorite
        onView(withId(R.id.add_to_favorite)).perform(click())

        //check favorite
        onView(withText("Removed from favorite")).check(matches(isDisplayed()))

        //click back button
        Espresso.pressBack()

        //click menu next match
        onView(withId(R.id.menu_next_matches)).perform(click())

        //check menu next match
        onView(withText("Next Matches")).check(matches(isDisplayed()))

        onView(withId(R.id.list_match)).check(matches(isDisplayed()))

        //scroll to item on position 12
        onView(withId(R.id.list_match)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(12)
        )

        //click item on position 12
        onView(withId(R.id.list_match)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(12, click())
        )

        //check detail activity
        onView(withText("Match Detail")).check(matches(isDisplayed()))

        onView(withId(R.id.add_to_favorite)).check(matches(isDisplayed()))

        //click favorite
        onView(withId(R.id.add_to_favorite)).perform(click())

        //check favorite
        onView(withText("Added to favorite")).check(matches(isDisplayed()))

        //end
    }
}