package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.DynamoDAOFactory;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeedsHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        for(SQSEvent.SQSMessage msg : input.getRecords()){
            String messageBody = msg.getBody();
            String[] message = messageBody.split("\\|");
            String statusString = message[0];
            String followersString = message[1];
            Gson deserializer = new Gson();
            Status status = deserializer.fromJson(statusString, Status.class);
            Type listType = new TypeToken<ArrayList<FollowRelation>>(){}.getType();
            List<FollowRelation> followers = deserializer.fromJson(followersString, listType);
            StatusService statusService = new StatusService(new DynamoDAOFactory());
            statusService.postToFeed(followers, status);
        }
        return null;
    }
}
