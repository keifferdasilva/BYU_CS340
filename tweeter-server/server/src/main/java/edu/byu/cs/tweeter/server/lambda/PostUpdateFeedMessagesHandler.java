package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;
import edu.byu.cs.tweeter.server.service.FollowService;
import edu.byu.cs.tweeter.server.sqs.SQSStatusClient;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        Status status = new Status();
        for (SQSEvent.SQSMessage msg : input.getRecords()) {
            Gson gson = new Gson();
            status = gson.fromJson(msg.getBody(), Status.class);
        }
        FollowService followService = new FollowService(new DynamoDAOFactory());
        DataPage<FollowRelation> batchOfFollowers;
        FollowRelation lastItem = null;
        do {
            String lastFollowerAlias = lastItem != null ? lastItem.getFollower_handle() : null;
            batchOfFollowers = followService.getAllFollowers(status, lastFollowerAlias);
            lastItem = batchOfFollowers.getValues().get(batchOfFollowers.getValues().size() - 1);
            List<FollowRelation> followers = batchOfFollowers.getValues();
            SQSStatusClient.postToFeedMessage(followers, status);
        }
        while (batchOfFollowers.isHasMorePages());
        return null;
    }
}
