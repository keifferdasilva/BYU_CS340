package edu.byu.cs.tweeter.server.sqs;

import com.google.gson.Gson;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class SQSStatusClient {

    public static void postStatusMessage(Status status){
        Gson jsonifier = new Gson();
        String messageBody = jsonifier.toJson(status);
        String queueURL = "https://sqs.us-west-1.amazonaws.com/139061477864/PostStatus";

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .messageBody(messageBody)
                .queueUrl(queueURL)
                .build();
        try (SqsClient sqs = SqsClient.create()) {
            sqs.sendMessage(sendMessageRequest);
        }
    }

    public static void postToFeedMessage(List<FollowRelation> followers, Status status) {
        Gson jsonifier = new Gson();
        String statusString = jsonifier.toJson(status);
        String followersString = jsonifier.toJson(followers);
        String messageBody = statusString + "|" + followersString;
        String queueURL = "https://sqs.us-west-1.amazonaws.com/139061477864/UpdateFeed";

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .messageBody(messageBody)
                .queueUrl(queueURL)
                .build();
        try (SqsClient sqs = SqsClient.create()) {
            sqs.sendMessage(sendMessageRequest);
        }
    }
}
