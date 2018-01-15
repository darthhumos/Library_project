package com.tmd.library;

/**
 * Created by Dovi Samuel on 15/01/2018.
 */



import org.junit.Before;
import org.junit.Test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import  android.support.test.uiautomator.UiDevice;
import  android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import  android.support.test.uiautomator.Until;

import static org.junit.Assert.assertEquals;

public class UIAutomatorUnitTest {
    private UiDevice mDevice;
    @Test
    public void checkButoonExists(){
        UiObject signUpButton = new UiObject(new UiSelector().textContains("tomer"));
       assertEquals(false,signUpButton.exists());

    }


    @Test
    public void clickTest(){
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        UiObject LoginButton = mDevice.findObject(new UiSelector()
                .text("Login"));
        try {
            if(LoginButton.exists() && LoginButton.isEnabled())
            {
                try {
                    LoginButton.click();
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
                assertEquals(true,LoginButton.exists());
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

    }
}


