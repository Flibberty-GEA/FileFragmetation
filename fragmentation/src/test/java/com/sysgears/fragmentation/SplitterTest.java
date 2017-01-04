package com.sysgears.fragmentation;

/**
 * @author yevgen
 */
public class SplitterTest {

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
