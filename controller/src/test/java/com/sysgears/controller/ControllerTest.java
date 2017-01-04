package com.sysgears.controller;

import org.testng.Assert;
import org.testng.annotations.Test;



/**
 * @author yevgen
 */
public class ControllerTest {

    @Test(groups = { "all-tests" })
    public void testExceptionInSendMessage(){
        Controller controller = new StreamController(System.in, System.out);
        Assert.assertTrue(controller.isOpen());
    }
}
