package com.sysgears.core.input;

import com.sysgears.controller.Controller;
import com.sysgears.controller.StreamController;
import com.sysgears.core.exceptions.InputException;
import com.sysgears.statistic.StatisticService;
import com.sysgears.statistic.StatisticServiceImpl;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.find;

/**
 * @author Yevgen Goliuk
 */
public class CommandTest {

    @DataProvider
    public Object[][] parseCommand() {
        return new Object[][]{
                {"exit", Command.EXIT},
                {"eXiT", Command.EXIT},
                {"split", Command.SPLIT},
                {"join", Command.JOIN}
        };
    }

    @DataProvider
    public Object[][] parseWrongCommand() {
        return new Object[][]{
                {null, null},
                {"x", null}
        };
    }

    @Test(dataProvider = "parseCommand")
    public void testCommandCreatorByCorrectValue(String command, Command expected) {
        final Command actual = Command.getCommandByValue(command);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "parseWrongCommand", expectedExceptions = InputException.class)
    public void testCommandCreatorByWrongValue(String command, Command expected) {
        final Command actual = Command.getCommandByValue(command);
        Assert.assertEquals(actual, expected);
    }

    @Test(expectedExceptions = IOException.class)
    public void testCommandExitApply() throws IOException {
        final InputDataHolder inputDataHolder = EasyMock.createMock(InputDataHolder.class);
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final Controller controller = new StreamController(System.in, System.out);

        Command.EXIT.apply(inputDataHolder, executorService, controller);
        BufferedWriter writer = controller.getWriter();
//        executorService.submit(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }
}
