package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;

public interface FollowsDAO {


    DataPage<FollowRelation> getFollowees(String followerAlias, int limit, String lastFolloweeAlias);
    DataPage<FollowRelation> getFollowers(String followeeAlias, int limit, String lastFollowerAlias);
    String follow(String followerAlias, String followeeAlias);

    void unfollow(String followerAlias, String followeeAlias);

    boolean isFollower(String followerAlias, String followeeAlias);

    List<FollowRelation> getAllFollowers(String userAlias);

    void addFollowersBatch(List<String> followers, String followTarget);
}
