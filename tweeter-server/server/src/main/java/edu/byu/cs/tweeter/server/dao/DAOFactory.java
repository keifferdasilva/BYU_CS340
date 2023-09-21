package edu.byu.cs.tweeter.server.dao;

public interface DAOFactory {

    AuthTokenDAO getAuthTokenDAO();
    FeedDAO getFeedDAO();
    FollowsDAO getFollowsDAO();
    StoryDAO getStoryDAO();
    UserDAO getUserDAO();

    ImageDAO getImageDAO();
}
