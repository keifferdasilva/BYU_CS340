package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;

public class FollowServiceTest {

    private FollowingRequest request;
    private DataPage<FollowRelation> expectedResponse;
    private DynamoFollowDAO mockFollowDAO;
    private FollowService followServiceSpy;

    @BeforeEach
    public void setup() {
        AuthToken authToken = new AuthToken();

        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        // Setup a request object to use in the tests
        request = new FollowingRequest(authToken, currentUser.getAlias(), 3, null);

        // Setup a mock FollowDAO that will return known responses
        expectedResponse = new DataPage<>();
        //TODO:Fix this test
//        expectedResponse.setValues(Arrays.asList(resultUser1, resultUser2, resultUser3));
//        expectedResponse = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        mockFollowDAO = Mockito.mock(DynamoFollowDAO.class);
        Mockito.when(mockFollowDAO.getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias())).thenReturn(expectedResponse);

        followServiceSpy = Mockito.spy(FollowService.class);
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                followServiceSpy.followDAO = mockFollowDAO;
                return null;
            }
        }).when(followServiceSpy).getFollowingDAO();
    }

    /**
     * Verify that the {@link FollowService#getFollowees(FollowingRequest)}
     * method returns the same result as the {@link DynamoFollowDAO} class.
     */
    @Test
    public void testGetFollowees_validRequest_correctResponse() {
        FollowingResponse response = followServiceSpy.getFollowees(request);
        Assertions.assertEquals(expectedResponse, response);
    }
}
