package com.sysgears.controller;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;

import java.io.IOException;


/**
 * @author yevgen
 */
public class ControllerTest {

    @Test(groups = {"all-tests"})
    public void testExceptionInSendMessage(){
        Controller controller = new StreamController(System.in, System.out);
        Assert.assertTrue(controller.isOpen());
//        controller.closeController();
//        Assert.assertFalse(controller.isOpen());
    }
}
