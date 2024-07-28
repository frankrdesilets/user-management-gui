package tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * This test suite runs all tests on the application.
 */
@Suite
@SelectClasses({ TestFileIO.class })
public class RunAllTests {

}
