package edu.byu.cs.tweeter.server.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.FeedRelation;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class DynamoFeedDAO extends DynamoDAO implements FeedDAO{

    private static final String TableName = "feed";
    private static final String AliasAttr = "alias";
    private static final String TimestampAttr = "timestamp";




    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    @Override
    public DataPage<FeedRelation> getFeed(String userAlias, int limit, Status lastStatus) {
        DynamoDbTable<FeedRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FeedRelation.class));
        Key key = Key.builder().partitionValue(userAlias)
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit)
                .scanIndexForward(false);

        if(lastStatus != null){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(AliasAttr, AttributeValue.builder().s(userAlias).build());
            startKey.put(TimestampAttr, AttributeValue.builder().n(lastStatus.getTimestamp().toString()).build());
            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();

        DataPage<FeedRelation> result = new DataPage<>();
        SdkIterable<Page<FeedRelation>> sdkIterable = table.query(queryEnhancedRequest);
        PageIterable<FeedRelation> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FeedRelation> page) ->{
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });
        return result;
    }


    @Override
    public void postToFeed(List<FollowRelation> followers, Status status) {
        DynamoDbTable<FeedRelation> feedTable = enhancedClient.table(TableName, TableSchema.fromBean(FeedRelation.class));

        for(FollowRelation followRelation : followers){
            FeedRelation feedRelation = new FeedRelation();
            feedRelation.setPost(status.getPost());
            feedRelation.setMentions(status.getMentions());
            feedRelation.setUrls(status.getUrls());
            feedRelation.setPosterAlias(status.getUser().getAlias());
            feedRelation.setTimestamp(status.getTimestamp());
            feedRelation.setAlias(followRelation.getFollower_handle());
            feedTable.putItem(feedRelation);
        }
    }
}
