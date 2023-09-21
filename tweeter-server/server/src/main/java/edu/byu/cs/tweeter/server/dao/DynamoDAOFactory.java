package edu.byu.cs.tweeter.server.dao;

public class DynamoDAOFactory implements DAOFactory{

    @Override
    public AuthTokenDAO getAuthTokenDAO() {
        return new DynamoAuthTokenDAO();
    }

    @Override
    public FeedDAO getFeedDAO() {
        return new DynamoFeedDAO();
    }

    @Override
    public FollowsDAO getFollowsDAO() {
        return new DynamoFollowDAO();
    }

    @Override
    public StoryDAO getStoryDAO() {
        return new DynamoStoryDAO();
    }

    @Override
    public UserDAO getUserDAO() {
        return new DynamoUserDAO();
    }

    @Override
    public ImageDAO getImageDAO() {
        return new S3DAO();
    }
}
