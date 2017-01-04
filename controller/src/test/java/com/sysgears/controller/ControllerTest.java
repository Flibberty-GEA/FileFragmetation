package com.sysgears.controller;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;



/**
 * @author yevgen
 */
public class ControllerTest {
//public static final Logger log = LogManager.getLogger(Foo.class);

//    Controller controller;

//    @BeforeTest(groups = { "all-tests" })
//    public void beforTest() {
//        controller = new StreamController(System.in, System.out);
////        controller.closeController();
//    }

//    @Test(groups = { "all-tests" })
//    public void testCloseException(){
//
//    }

//    @Test(expectedExceptions = ControllerException.class, groups = { "all-tests" })
//    public void testExceptionInGetMessage() {
//        controller.closeController();
//        controller.getMessage();
//    }

//    @Test(groups = { "all-tests" }, expectedExceptions = ControllerException.class)
    @Test(groups = { "all-tests" })
    public void testExceptionInSendMessage(){
//        controller.sendMessage("HELLO");
        Controller controller = new StreamController(System.in, System.out);
//        controller.closeController();
        Assert.assertTrue(controller.isOpen());

//        controller.sendMessage("HELLO");
//        controller.sendMessage("");
    }
}
