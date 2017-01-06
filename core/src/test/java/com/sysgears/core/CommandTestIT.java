package com.sysgears.core;

import com.sysgears.controller.Controller;
import com.sysgears.controller.StreamController;
import com.sysgears.core.input.Command;
import com.sysgears.core.input.InputDataHolder;
import com.sysgears.core.input.InputException;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.easymock.EasyMock.*;


/**
 * @author Yevgen Goliuk
 */
public class CommandTestIT {
    private final String STATISTIC_INFO_PATTERN = "Total progress: (100|[1-9]?[0-9]{1})%, " +
            "pool-\\d+-thread-[12]: (100|[1-9]?[0-9]{1})%, " +
            "pool-\\d+-thread-[12]: (100|[1-9]?[0-9]{1})%, " +
            "time remaining: \\d+\\.\\d+s\\.";

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

    @Test(dataProvider = "parseCommand", groups = { "all-tests" })
    public void testCommandCreatorByCorrectValue(String command, Command expected) {
        final Command actual = Command.getCommandByValue(command);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "parseWrongCommand", expectedExceptions = InputException.class, groups = { "all-tests" })
    public void testCommandCreatorByWrongValue(String command, Command expected) {
        final Command actual = Command.getCommandByValue(command);
        Assert.assertEquals(actual, expected);
    }


    @Test(/*expectedExceptions = IOException.class, */groups = { "all-tests" })
    public void testCommandSplitApply() throws IOException {
        final InputDataHolder inputDataHolder = EasyMock.createMock(InputDataHolder.class);
        File file = new File("/home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp");

        expect(inputDataHolder.getCommand()).andReturn("split").anyTimes();
        expect(inputDataHolder.getSize()).andReturn(1000000L).anyTimes();
        expect(inputDataHolder.getFile()).andReturn(file).anyTimes();
        EasyMock.replay(inputDataHolder);

        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        final Controller controller = EasyMock.createMock(StreamController.class);
        controller.sendMessage(matches(STATISTIC_INFO_PATTERN));
        expectLastCall().anyTimes();
        EasyMock.replay(controller);

        Command.SPLIT.apply(inputDataHolder, executorService, controller);
        EasyMock.verify(inputDataHolder);
        EasyMock.verify(controller);
    }


//    @Test(expectedExceptions = IOException.class)
//    public void testCommandExitApply() throws IOException {
//        final InputDataHolder inputDataHolder = EasyMock.createMock(InputDataHolder.class);
//        final ExecutorService executorService = Executors.newFixedThreadPool(2);
//        final Controller controller = new StreamController(System.in, System.out);
//
//        Command.EXIT.apply(inputDataHolder, executorService, controller);
//        BufferedWriter writer = controller.getWriter();
//    }
}
