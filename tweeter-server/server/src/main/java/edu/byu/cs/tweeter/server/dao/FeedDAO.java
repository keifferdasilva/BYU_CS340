package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.FeedRelation;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;

public interface FeedDAO {

    DataPage<FeedRelation> getFeed(String userAlias, int limit, Status lastStatus);
    void postToFeed(List<FollowRelation> followers, Status status);
}
