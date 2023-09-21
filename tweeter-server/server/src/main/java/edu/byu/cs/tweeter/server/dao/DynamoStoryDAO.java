package edu.byu.cs.tweeter.server.dao;

import java.util.HashMap;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.StoryRelation;
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

public class DynamoStoryDAO extends DynamoDAO implements StoryDAO{

    private static final String StoryTableName = "story";
    private static final String FeedTableName = "feed";
    private static final String AliasAttr = "alias";
    private static final String TimestampAttr = "timestamp";




    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();


    @Override
    public void postStatus(Status status) {
        DynamoDbTable<StoryRelation> table = enhancedClient.table(StoryTableName, TableSchema.fromBean(StoryRelation.class));

        StoryRelation storyRelation = new StoryRelation();
        storyRelation.setPost(status.getPost());
        storyRelation.setMentions(status.getMentions());
        storyRelation.setUrls(status.getUrls());
        storyRelation.setAlias(status.getUser().getAlias());
        storyRelation.setTimestamp(status.getTimestamp());

        table.putItem(storyRelation);

    }

    @Override
    public DataPage<StoryRelation> getStory(String userAlias, int limit, Status lastStatus) {
        DynamoDbTable<StoryRelation> table = enhancedClient.table(StoryTableName, TableSchema.fromBean(StoryRelation.class));
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

        DataPage<StoryRelation> result = new DataPage<>();
        SdkIterable<Page<StoryRelation>> sdkIterable = table.query(queryEnhancedRequest);
        PageIterable<StoryRelation> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<StoryRelation> page) ->{
                        result.setHasMorePages(page.lastEvaluatedKey() != null);
                        page.items().forEach(visit -> result.getValues().add(visit));
                });


        return result;
    }
}
