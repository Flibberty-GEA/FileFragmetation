package com.sysgears.core;

import com.sysgears.core.input.InputException;
import com.sysgears.core.input.ApacheCliParser;
import com.sysgears.core.input.InputDataHolder;
import com.sysgears.core.input.InputDataParser;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;


/**
 * @author Yevgen Goliuk
 */
public class InputDataParserAndHolderTetsIT {
    FileUtil fileUtil = new FileUtil();
    File file = fileUtil.createFile("parseAndHolderTest.txt", 35700034);

    String commandForExit = "-c exit";
    String correctCommandForSplitFile = "-c split -p "+file.getPath()+" -s 1M";
    String correctCommandForJoinFile = "-c join -p "+file.getPath()+" -s 1K";
    String wrongCommandWithBadCommand = "-c lol -p "+file.getPath()+" -s 1M";
    String wrongCommandWithBadPath = "-c split -p /wrong_file_path/testFile.bmp -s 1M";
    String wrongCommandWithBadSize = "-c split -p "+file.getPath()+" -s 1B";
    InputDataParser apacheCliParser = new ApacheCliParser();

    @DataProvider
    public Object[][] correctInputData() {
        return new Object[][]{
                {commandForExit, "exit"},
                {correctCommandForSplitFile, "split", file.getPath(), "1000000"},
                {correctCommandForJoinFile, "join", file.getPath(), "1000"},
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

    @AfterTest(groups = { "all-tests" })
    public void deleteFiles(){
        file.delete();
    }
}
