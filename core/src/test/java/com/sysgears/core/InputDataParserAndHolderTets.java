package com.sysgears.core;

import com.sysgears.core.input.InputException;
import com.sysgears.core.input.ApacheCliParser;
import com.sysgears.core.input.InputDataHolder;
import com.sysgears.core.input.InputDataParser;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


/**
 * @author Yevgen Goliuk
 */
public class InputDataParserAndHolderTets {
    String commandForExit = "-c exit";
    String correctCommandForSplitFile = "-c split -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp -s 1M";
    String correctCommandForJoinFile = "-c join -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp -s 1K";
    String wrongCommandWithBadCommand = "-c sffsdf -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp -s 1M";
    String wrongCommandWithBadPath = "-c split -p /wrong_file_path/testFile.bmp -s 1M";
    String wrongCommandWithBadSize = "-c split -p /home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp -s 1B";
    InputDataParser apacheCliParser = new ApacheCliParser();

    @DataProvider
    public Object[][] correctInputData() {
        return new Object[][]{
                {commandForExit, "exit"},
                {correctCommandForSplitFile, "split", "/home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp", "1000000"},
                {correctCommandForJoinFile, "join", "/home/yevgen/IdeaProjects/FileFragmetation/core/src/test/resources/file.bmp", "1000"},
        };
    }

    @DataProvider
    public Object[][] wrongInputData() {
        return new Object[][]{
                {wrongCommandWithBadCommand, ""},
                {wrongCommandWithBadPath, ""},
                {wrongCommandWithBadSize, ""}
        };
    }

    @Test(dataProvider = "correctInputData", groups = { "all-tests" })
    public void testCorrectCommand(final String command, final String... expected) {
        final String[] commands = command.split(" ");
        final InputDataHolder inputDataHolder = apacheCliParser.parse(commands);
        for (int index = 0; index<expected.length; index++){
            if (index==0) Assert.assertEquals(inputDataHolder.getCommand(), expected[index]);
            else if (index==1) Assert.assertEquals(inputDataHolder.getFile().getPath(), expected[index]);
            else Assert.assertEquals((Long)inputDataHolder.getSize(), Long.valueOf(expected[index]));
        }
    }

    @Test(dataProvider = "wrongInputData", expectedExceptions = InputException.class, groups = { "all-tests" })
    public void testWrongCommand(final String command, final String... expected) {
        apacheCliParser.parse(command.split(" "));
    }

}
