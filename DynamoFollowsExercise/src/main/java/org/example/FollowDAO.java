package org.example;

import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public class FollowDAO {

    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    private static boolean isNonEmptyString(String value){
        return (value != null && value.length() > 0);
    }

    public FollowRelation follows(String follower, String followee){
        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();

        FollowRelation followRelation = table.getItem(key);
        return followRelation;
    }

    public void followUser(String follower, String followerName, String followee, String followeeName){
        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();
        FollowRelation followRelation = table.getItem(key);
        if(followRelation != null){System.out.println("You already follow this user");}
        else{
            FollowRelation newFollowRelation = new FollowRelation();
            newFollowRelation.setFollower_handle(follower);
            newFollowRelation.setFollowee_handle(followee);
            newFollowRelation.setFollowerName(followerName);
            newFollowRelation.setFolloweeName(followeeName);
            table.putItem(newFollowRelation);
        }
    }

    public void updateFollowing(String follower, String followee, String followerName, String followeeName){
        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();
        FollowRelation followRelation = table.getItem(key);
        if(followRelation == null){
            System.out.println("The first user does not follow the second");
        }
        else{
            followRelation.setFollowerName(followerName);
            followRelation.setFolloweeName(followeeName);
            table.updateItem(followRelation);
        }
    }

    public void unfollow(String follower, String followee){
        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder()
                .partitionValue(follower).sortValue(followee)
                .build();
        table.deleteItem(key);
    }

    public DataPage<FollowRelation> getPageOfFollowees(String targetUserAlias, int pageSize, String lastUserAlias){
        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder().partitionValue(targetUserAlias)
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize)
                .scanIndexForward(true);

        if(isNonEmptyString(lastUserAlias)){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowRelation> result = new DataPage<>();
        PageIterable<FollowRelation> pages = table.query(request);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowRelation> page) ->{
            result.setHasMorePages(page.lastEvaluatedKey() != null);
            page.items().forEach(visit -> result.getValues().add(visit));
        });
        return result;
    }

    public DataPage<FollowRelation> getPageOfFollowers(String targetUserAlias, int pageSize, String lastUserAlias){
        DynamoDbIndex<FollowRelation> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class)).index(IndexName);
        Key key = Key.builder().partitionValue(targetUserAlias)
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(pageSize)
                .scanIndexForward(true);

        if(isNonEmptyString(lastUserAlias)){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(targetUserAlias).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastUserAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowRelation> result = new DataPage<>();
        SdkIterable<Page<FollowRelation>> sdkIterable = index.query(request);
        PageIterable<FollowRelation> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowRelation> page) ->{
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });
        return result;
    }
}
