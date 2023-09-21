package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.StoryRelation;

public interface StoryDAO {

    void postStatus(Status status);
    DataPage<StoryRelation> getStory(String userAlias, int limit, Status lastStatus);
}
