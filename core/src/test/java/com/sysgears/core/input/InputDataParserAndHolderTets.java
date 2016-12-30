package com.sysgears.core.input;

import com.sysgears.core.exceptions.InputException;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import static org.easymock.EasyMock.expect;

/**
 * @author Yevgen Goliuk
 */
public class InputDataParserAndHolderTets {
    String commandForExit = "-c exit";
    String correctCommandForSplitFile = "-c split -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp -s 1M";
    String wrongCommandWithBadPath = "-c split -p /wrong_file_path/file.bmp -s 1M";
    String wrongCommandWithBadSize = "-c split -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp -s 1B";
    InputDataParser apacheCliParser = new ApacheCliParser();

    @DataProvider
    public Object[][] correctInputData() {
        return new Object[][]{
                {commandForExit, "exit"},
                {correctCommandForSplitFile, "split", "/home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp", "1000000"},
        };
    }

    @DataProvider
    public Object[][] wrongInputData() {
        return new Object[][]{
                {wrongCommandWithBadPath, ""},
                {wrongCommandWithBadSize, ""}
        };
    }

    @Test(dataProvider = "correctInputData")
    public void testCorrectCommand(final String command, final String... expected) {
        final String[] commands = command.split(" ");
        final InputDataHolder inputDataHolder = apacheCliParser.parse(commands);
        final String actual = inputDataHolder.getCommand();
        for (int index = 0; index<expected.length; index++){
            if (index==0) Assert.assertEquals(inputDataHolder.getCommand(), expected[index]);
            else if (index==1) Assert.assertEquals(inputDataHolder.getFile().getPath(), expected[index]);
            else Assert.assertEquals((Long)inputDataHolder.getSize(), Long.valueOf(expected[index]));
        }
    }

    @Test(dataProvider = "wrongInputData", expectedExceptions = InputException.class)
    public void testWrongCommand(final String command, final String... expected) {
        apacheCliParser.parse(command.split(" "));
    }

//    InputDataHolder inputExit;
//    InputDataHolder inputSplit;
//
//    @BeforeTest
//    public void exitMock() {
//        inputExit = EasyMock.createMock(InputDataHolder.class);
//        expect(inputExit.getCommand()).andReturn("exit");
//        EasyMock.replay(inputExit);
//    }
//
//    @BeforeTest
//    public void splitMock() {
//        inputSplit = EasyMock.createMock(InputDataHolder.class);
//        expect(inputSplit.getCommand()).andReturn("split");
//        expect(inputSplit.getFile()).andReturn(new File("/home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp"));
//        expect(inputSplit.getSize()).andReturn(1000000L);
//        EasyMock.replay(inputSplit);
//    }
//
//    @Test
//    public void testParse() throws Exception {
//        Assert.assertEquals(inputExit.getCommand(), apacheCliParser.parse(commandForExit.split(" ")).getCommand());
//        Assert.assertEquals(inputSplit.getCommand(), apacheCliParser.parse(correctCommandForSplitFile.split(" ")).getCommand());
//        Assert.assertEquals(inputSplit.getFile(), apacheCliParser.parse(correctCommandForSplitFile.split(" ")).getFile());
//        Assert.assertEquals(inputSplit.getSize(), apacheCliParser.parse(correctCommandForSplitFile.split(" ")).getSize());
//    }
//    @Test(expectedExceptions = InputException.class)
//    public void testException() {
//        apacheCliParser.parse(wrongCommandWithBadPath.split(" "));
//    }
}
