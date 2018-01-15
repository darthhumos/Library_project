package com.tmd.library;

import org.junit.Test;
import org.mockito.Mockito;

import dalvik.annotation.TestTarget;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

/**
 * Created by Dovi Samuel on 14/01/2018.
 */

public class MockitoUnitTest {

    Message msgObj;

    public void  createMsg(){
        msgObj = mock(Message.class);
        Mockito.when(msgObj.getMessage()).thenReturn("Temp message");
    }

    @Test
    public void test_mockito(){
        msgObj = new Message();
        assertSame("Temp message",msgObj.getMessage());
    }


}
