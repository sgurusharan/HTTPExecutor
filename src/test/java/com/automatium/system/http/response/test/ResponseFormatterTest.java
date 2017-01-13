package com.automatium.system.http.response.test;

import com.automatium.system.CommandOutput;
import com.automatium.system.http.response.JsonResponseFormatter;
import com.automatium.system.http.response.OutputResponseFormatter;
import com.automatium.system.http.response.XMLResponseFormatter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Gurusharan on 11-12-2016.
 */
@RunWith(Parameterized.class)
public class ResponseFormatterTest {

    OutputResponseFormatter formatter;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> formattersToTest() {
        return Arrays.asList(
                new Object[][]{
                        { JsonResponseFormatter.class },
                        { XMLResponseFormatter.class }
                }
        );
    }

    public ResponseFormatterTest(Class formatterToTest) {
        try {
            formatter = (OutputResponseFormatter) formatterToTest.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFormatter() {
        CommandOutput testOutput = new CommandOutput("Test Output", "Test Error", 125);

        String response = formatter.getResponseFromOutput(testOutput);
        CommandOutput actualOutput = formatter.getOutputFromResponse(response);

        Assert.assertEquals(testOutput.getExitCode(), actualOutput.getExitCode());
        Assert.assertEquals(testOutput.getOut(), actualOutput.getOut());
        Assert.assertEquals(testOutput.getErr(), actualOutput.getErr());
    }
}
