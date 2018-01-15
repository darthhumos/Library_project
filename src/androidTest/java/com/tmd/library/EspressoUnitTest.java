package com.tmd.library;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dalvik.annotation.TestTarget;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Dovi Samuel on 14/01/2018.
 */
@RunWith(AndroidJUnit4.class)
public class EspressoUnitTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);


    @Test
    public void userView(){
        Espresso.onView(ViewMatchers.withText("Login")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void signupTest(){
        Espresso.onView(ViewMatchers.withId(R.id.SignUp)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.Signup_Name)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }



}
