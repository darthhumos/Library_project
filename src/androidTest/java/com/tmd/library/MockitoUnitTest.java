package com.tmd.library;

import org.junit.Test;

import dalvik.annotation.TestTarget;

import static junit.framework.Assert.assertSame;
import static org.mockito.*;

/**
 * Created by Dovi Samuel on 14/01/2018.
 */

public class MockitoUnitTest {

    Message msgObj;

    public void  createMsg(){
        msgObj = mock(Message.class);
        when(msgObj.getMessage().thenReturn("Temp message"));
    }

    @Test
    public void test_mockito(){
        assertSame("Temp message",msgObj.getMessage());
    }


}
