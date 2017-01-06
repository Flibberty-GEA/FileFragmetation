package com.sysgears.controller;

import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.easymock.EasyMock.expect;


/**
 * @author yevgen
 */
public class ControllerTest {
    Controller controller;

    @BeforeTest(groups = {"all-tests"})
    public void dataa() {
        controller = EasyMock.createMock(StreamController.class);
        expect(controller.isOpen()).andReturn(true);
        EasyMock.replay(controller);
    }


    @Test(groups = {"all-tests"})
    public void testUnit(){
        Assert.assertTrue(controller.isOpen());
    }

//    @Test(groups = {"all-tests"})
//    public void testExceptionInSendMessage(){
//        Controller controller = new StreamController(System.in, System.out);
//        Assert.assertTrue(controller.isOpen());
////        controller.closeController();
////        Assert.assertFalse(controller.isOpen());
//    }
}
