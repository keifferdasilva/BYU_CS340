package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.FeedRelation;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;
import edu.byu.cs.tweeter.server.dao.relation.StoryRelation;
import edu.byu.cs.tweeter.server.sqs.SQSStatusClient;

public class StatusService {

    StoryDAO storyDAO;
    FeedDAO feedDAO;
    AuthTokenDAO authTokenDAO;
    UserDAO userDAO;
    FollowsDAO followDAO;

    public StatusService(DAOFactory factory){
        storyDAO = factory.getStoryDAO();
        feedDAO = factory.getFeedDAO();
        authTokenDAO = factory.getAuthTokenDAO();
        userDAO = factory.getUserDAO();
        followDAO = factory.getFollowsDAO();
    }

    public PostStatusResponse postStatus(PostStatusRequest request){
        if(request.getStatus() == null){
            throw new RuntimeException("[Bad Request] Request is missing a status");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authentication token");
        }

        if(authTokenDAO.verifyAuthToken(request.getAuthToken())){
            Status status = request.getStatus();
            storyDAO.postStatus(status);
            SQSStatusClient.postStatusMessage(status);
            return new PostStatusResponse();
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public FeedResponse getFeed(FeedRequest request) {
        if(request.getUserAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            //Pair<List<Status>, Boolean> data = FakeData.getInstance().getPageOfStatus(request.getLastStatus(), request.getLimit());
            DataPage<FeedRelation> result = feedDAO.getFeed(request.getUserAlias(), request.getLimit(), request.getLastStatus());
            List<Status> feed = new ArrayList<>();
            for(FeedRelation relation : result.getValues()){
                User user = userDAO.getUser(relation.getPosterAlias());
                Status status = new Status(relation.getPost(), user, relation.getTimestamp(), relation.getUrls(), relation.getMentions());
                feed.add(status);
            }
            return new FeedResponse(feed, result.isHasMorePages());
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public StoryResponse getStory(StoryRequest request) {
        if(request.getUserAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            DataPage<StoryRelation> data = storyDAO.getStory(request.getUserAlias(), request.getLimit(), request.getLastStatus());

            List<Status> story = new ArrayList<>();
            for(StoryRelation relation : data.getValues()){
                User user = userDAO.getUser(relation.getAlias());
                Status status = new Status(relation.getPost(), user, relation.getTimestamp(), relation.getUrls(), relation.getMentions());
                story.add(status);
            }
            return new StoryResponse(story, data.isHasMorePages());
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public void postToFeed(List<FollowRelation> followers, Status status) {
        feedDAO.postToFeed(followers, status);
    }
}
