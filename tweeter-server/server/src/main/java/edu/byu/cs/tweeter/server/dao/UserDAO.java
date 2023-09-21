package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserDAO {

    User getUser(String userAlias);
    User createNewUser(String username, String firstName, String lastName, String password);
    User login(String username, String password);

    void incrementFollowerCount(String userAlias);
    void incrementFolloweeCount(String userAlias);

    void decrementFollowerCount(String userAlias);
    void decrementFolloweeCount(String userAlias);

    int getFollowersCount(String userAlias);
    int getFollowingCount(String userAlias);

    void addUserBatch(List<User> users);
}
