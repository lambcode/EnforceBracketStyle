package net.lambcode.enforceBracketStyle;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(JUnitParamsRunner.class)
public class CustomEnterActionHandlerTest {

    private CustomEnterActionHandler customEnterActionHandler;

    @Before
    public void setup()
    {
        customEnterActionHandler = new CustomEnterActionHandler();
    }

    @Test
    @Parameters(method = "getTrueCases")
    @TestCaseName("testing true for sequence [{0}] at position {1}")
    public void shouldReturnTrue(String sequence, int location) {
        boolean result = customEnterActionHandler.shouldFormatBracket(sequence, location);
        assertTrue(result);
    }

    @Test
    @Parameters(method = "getFalseCases")
    @TestCaseName("testing false for sequence [{0}] at position {1}")
    public void shouldReturnFalse(String sequence, int location) {
        boolean result = customEnterActionHandler.shouldFormatBracket(sequence, location);
        assertFalse(result);
    }

    private Object[][] getTrueCases() {
        return new Object[][]{
                new Object[]{"{", 1},
                new Object[]{"{ ", 1},
                new Object[]{"{}", 1},
                new Object[]{"{} \n", 1},
                new Object[]{"{\n} \n   {\n\n", 9},
                new Object[]{"{\n} \n   { \n\n", 9}
        };
    }

    private Object[][] getFalseCases() {
        return new Object[][]{
                new Object[]{"", 0},
                new Object[]{"{", 0},
                new Object[]{"{} \n", 2},
                new Object[]{"{\n} ", 2},
                new Object[]{"\n   a {\n} ", 7}
        };
    }
}
