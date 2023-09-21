package mocking;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

public class ClientTest {

    Service mockService;
    Client spyClient;

    /**
     * This function is run before each test. We can use it to set up our mock objects with Mockito.
     */
    @BeforeEach
    public void setup() {
        mockService = Mockito.mock(Service.class);

        Client client = new Client();
        spyClient = Mockito.spy(client);

        Mockito.doReturn(mockService).when(spyClient).getNewService();
    }

    /**
     * This is supposed to test whether the code in the Client is correct. The Client code is correct,
     *     but the Service has a bug that causes this test to fail. Using Mockito we can test the Client
     *     without also testing the Service
     */
    @Test
    public void testConvertValue() {
        String expected = "70";
        Mockito.doReturn(2).when(mockService).getDecimalDigitCount(35);
        String actual = spyClient.convertValue(35);
        Assertions.assertEquals(expected, actual);
    }

    /**
     * This test has a problem similar to the last, but additionally we have no assurance
     *     that the Client's code is correct, since the function has no return value for us to
     *     check. However, we can use Mockito to check the parameters passed to the Service
     *     in order to see whether the Client has correct code.
     */
    @Test
    public void testCreateFormattedStringsWithAnswer() {


        Mockito.doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Object[] args = invocationOnMock.getArguments();
                Object firstParameter = args[0];
                Assertions.assertNotNull(firstParameter);
                return null;
            }
        }).when(mockService).processList(Mockito.anyList());
        String input = "Have a nice day";
        spyClient.createFormattedStrings(input);

        Mockito.verify(mockService).processList(Mockito.anyList());
    }

}
