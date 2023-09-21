package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.DynamoFollowDAO;
import edu.byu.cs.tweeter.server.dao.FollowsDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    AuthTokenDAO authTokenDAO;
    FollowsDAO followDAO;

    UserDAO userDAO;
    DAOFactory factory;

    public FollowService(DAOFactory factory){
        this.factory = factory;
        authTokenDAO = factory.getAuthTokenDAO();
        followDAO = factory.getFollowsDAO();
        userDAO = factory.getUserDAO();
    }

    public void getFollowingDAO(){
        followDAO = factory.getFollowsDAO();
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if(request.getFollower() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        else if(request.getFollowee() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }
        else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing authentication token");
        }
        //boolean isFollower = new Random().nextInt() > 0;
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            return new IsFollowerResponse(followDAO.isFollower(request.getFollower(), request.getFollowee()));
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
        //return new IsFollowerResponse(isFollower);
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link DynamoFollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        //return getFollowingDAO().getFollowees(request);
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            DataPage<FollowRelation> result = followDAO.getFollowees(request.getFollowerAlias(), request.getLimit(), request.getLastFolloweeAlias());
            List<User> followees = new ArrayList<>();
            for(FollowRelation relation : result.getValues()){
                System.out.println(relation.getFollowee_handle());
                User user = userDAO.getUser(relation.getFollowee_handle());
                followees.add(user);
                System.out.println(user);
            }
            return new FollowingResponse(followees, result.isHasMorePages());
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public FollowersResponse getFollowers(FollowersRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        //return getFollowingDAO().getFollowers(request);
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            DataPage<FollowRelation> result = followDAO.getFollowers(request.getFolloweeAlias(), request.getLimit(), request.getLastFollowerAlias());
            List<User> followers = new ArrayList<>();
            for(FollowRelation relation : result.getValues()){
                User user = userDAO.getUser(relation.getFollower_handle());
                followers.add(user);
            }
            return new FollowersResponse(followers, result.isHasMorePages());
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }


    public FollowResponse follow(FollowRequest request) {
        if(request.getFollowerAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        else if(request.getFolloweeAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }
        else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing authentication token");
        }
        //return new FollowResponse();
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            String result = followDAO.follow(request.getFollowerAlias(), request.getFolloweeAlias());
            FollowResponse response;
            if(result == null){
                response = new FollowResponse();
            }
            else{
                 response = new FollowResponse(result);
            }
            userDAO.incrementFollowerCount(request.getFolloweeAlias());
            userDAO.incrementFolloweeCount(request.getFollowerAlias());
            return response;
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        if(request.getUserAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authentication token");
        }
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            return new FollowersCountResponse(userDAO.getFollowersCount(request.getUserAlias()));
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if(request.getUserAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a user alias");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authentication token");
        }
        //return new FollowingCountResponse(20);
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            return new FollowingCountResponse(userDAO.getFollowingCount(request.getUserAlias()));
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if(request.getFollowerAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        }
        else if(request.getFolloweeAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }
        else if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing authentication token");
        }
        //Actually unfollow in the future
        //return new UnfollowResponse();
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            followDAO.unfollow(request.getFollowerAlias(), request.getFolloweeAlias());
            userDAO.decrementFollowerCount(request.getFolloweeAlias());
            userDAO.decrementFolloweeCount(request.getFollowerAlias());
            return new UnfollowResponse();

        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public DataPage<FollowRelation> getAllFollowers(Status status, String lastFollowerAlias) {
        return followDAO.getFollowers(status.getUser().getAlias(), 100, lastFollowerAlias);
    }
}
