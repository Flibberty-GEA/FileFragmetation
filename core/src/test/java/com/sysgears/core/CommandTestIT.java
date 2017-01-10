package com.sysgears.core;

import com.sysgears.controller.Controller;
import com.sysgears.controller.StreamController;
import com.sysgears.core.input.Command;
import com.sysgears.core.input.InputDataHolder;
import com.sysgears.core.input.InputException;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.easymock.EasyMock.*;


/**
 * Integration tests for Command.
 *
 * @author Yevgen Goliuk
 */
public class CommandTestIT {
    private final String STATISTIC_INFO_PATTERN = "Total progress: (100|[1-9]?[0-9]{1})%, " +
            "pool-\\d+-thread-[12]: (100|[1-9]?[0-9]{1})%, " +
            "pool-\\d+-thread-[12]: (100|[1-9]?[0-9]{1})%, " +
            "time remaining: \\d+\\.\\d+s\\.";
    final FileCreator fileCreator = new FileCreator();
    File file;
    String prefix = "_part_";

    @DataProvider
    public Object[][] correctCommand() {
        return new Object[][]{
                {"exit", Command.EXIT},
                {"eXiT", Command.EXIT},
                {"split", Command.SPLIT},
                {"join", Command.JOIN}
        };
    }

    @DataProvider
    public Object[][] wrongCommand() {
        return new Object[][]{
                {null, null},
                {"x", null}
        };
    }

    @Test(dataProvider = "correctCommand", groups = { "all-tests" })
    public void testCommandCreatorByCorrectValue(String command, Command expected) {
        final Command actual = Command.getCommandByValue(command);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "wrongCommand", expectedExceptions = InputException.class, groups = { "all-tests" })
    public void testCommandCreatorByWrongValue(String command, Command expected) {
        final Command actual = Command.getCommandByValue(command);
        Assert.assertEquals(actual, expected);
    }


    @Test(groups = { "all-tests" })
    public void testCommandSplitApply() throws IOException {
        final InputDataHolder inputDataHolder = EasyMock.createMock(InputDataHolder.class);
        file = fileCreator.createFile("commandTest.txt", 35700034);

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

    @AfterTest(groups = { "all-tests" })
    public void deleteFiles(){
        File deleteFile;
        int count = (int) (file.length()/1000000L);
        for (int index = 0; index <=count; index++ ){
            deleteFile = new File(file.getPath()+prefix+index);
            deleteFile.delete();
        }
        file.delete();
    }
}
