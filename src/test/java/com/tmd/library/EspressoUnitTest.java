package com.tmd.library;

import android.app.Activity;

import dalvik.annotation.TestTarget;

/**
 * Created by Dovi Samuel on 14/01/2018.
 */
@RunWith (AndroidJUnit4.class)
public class EspressoUnitTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Test
    public void userView(){
        onView(withText("LOGIN")).check(matches(isDisplayed()));
    }

    public void signupTest(){
        onView(withId(R.id.SignUp)).preform(click());
        onView(withId(R.id.Signup_Name)).check(matches(isDisplayed()));
    }

}
