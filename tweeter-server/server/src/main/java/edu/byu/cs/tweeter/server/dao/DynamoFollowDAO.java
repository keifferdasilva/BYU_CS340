package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.server.dao.relation.DataPage;
import edu.byu.cs.tweeter.server.dao.relation.FollowRelation;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class DynamoFollowDAO extends DynamoDAO implements FollowsDAO{

    private static final String TableName = "follows";

    public static final String IndexName = "follows_index";

    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";

    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_1)
            .build();

    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();






    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param followerAlias the person who is sending the request
     * @param limit the number of followees to get at once
     * @param lastFolloweeAlias the last followee found if there are more followees to grab
     * @return the followees.
     */
    @Override
    public DataPage<FollowRelation> getFollowees(String followerAlias, int limit, String lastFolloweeAlias) {
        assert limit > 0;
        assert followerAlias != null;

        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder().partitionValue(followerAlias)
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit)
                .scanIndexForward(true);

        if(isNonEmptyString(lastFolloweeAlias)){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FollowerAttr, AttributeValue.builder().s(followerAlias).build());
            startKey.put(FolloweeAttr, AttributeValue.builder().s(lastFolloweeAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();

        DataPage<FollowRelation> result = new DataPage<>();
        PageIterable<FollowRelation> pages = table.query(queryRequest);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowRelation> page) ->{
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return result;
    }

    @Override
    public DataPage<FollowRelation> getFollowers(String followeeAlias, int limit, String lastFollowerAlias) {
        assert limit > 0;
        assert followeeAlias != null;

        DynamoDbIndex<FollowRelation> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class)).index(IndexName);
        Key key = Key.builder().partitionValue(followeeAlias)
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(limit)
                .scanIndexForward(true);

        if(isNonEmptyString(lastFollowerAlias)){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FolloweeAttr, AttributeValue.builder().s(followeeAlias).build());
            startKey.put(FollowerAttr, AttributeValue.builder().s(lastFollowerAlias).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest queryRequest = requestBuilder.build();

        DataPage<FollowRelation> result = new DataPage<>();
        SdkIterable<Page<FollowRelation>> sdkIterable = index.query(queryRequest);
        PageIterable<FollowRelation> pages = PageIterable.create(sdkIterable);
        pages.stream()
                .limit(1)
                .forEach((Page<FollowRelation> page) ->{
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(visit -> result.getValues().add(visit));
                });

        return result;
    }


    @Override
    public String follow(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder()
                .partitionValue(followeeAlias).sortValue(followeeAlias)
                .build();
        FollowRelation followRelation = table.getItem(key);
        if(followRelation != null){
            return "You already follow this user";
        }
        else{
            FollowRelation newFollowRelation = new FollowRelation();
            newFollowRelation.setFollower_handle(followerAlias);
            newFollowRelation.setFollowee_handle(followeeAlias);
            table.putItem(newFollowRelation);
            return null;
        }
    }


    @Override
    public void unfollow(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder()
                .partitionValue(followerAlias).sortValue(followeeAlias)
                .build();
        table.deleteItem(key);
    }

    @Override
    public boolean isFollower(String followerAlias, String followeeAlias) {
        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        Key key = Key.builder()
                .partitionValue(followerAlias).sortValue(followeeAlias)
                .build();
        FollowRelation followRelation = table.getItem(key);
        if (followRelation == null) {
            return false;
        } else {
            return followRelation.getFollower_handle() != null && followRelation.getFollowee_handle() != null;
        }
    }

    @Override
    public List<FollowRelation> getAllFollowers(String posterAlias){
        DynamoDbTable<FollowRelation> followTable = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));

        Key followKey = Key.builder().partitionValue(posterAlias)
                .build();
        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(followKey))
                .scanIndexForward(true);

        QueryEnhancedRequest queryEnhancedRequest = requestBuilder.build();
        List<FollowRelation> followees = new ArrayList<>();
        SdkIterable<Page<FollowRelation>> sdkIterable = followTable.query(queryEnhancedRequest);
        PageIterable<FollowRelation> pages = PageIterable.create(sdkIterable);
        pages.items().stream().forEach(followees::add);
        return followees;
    }

    @Override
    public void addFollowersBatch(List<String> followers, String followTarget) {
        List<FollowRelation> batchToWrite = new ArrayList<>();
        for (String follower : followers){
            FollowRelation followRelation = new FollowRelation();
            followRelation.setFollower_handle(follower);
            followRelation.setFollower_handle(followTarget);
            batchToWrite.add(followRelation);

            if(batchToWrite.size() == 25){
                writeChunkOfFollowers(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        if (batchToWrite.size() > 0) {
            writeChunkOfFollowers(batchToWrite);
        }
    }

    private void writeChunkOfFollowers(List<FollowRelation> batchToWrite) {
        if(batchToWrite.size() > 25){
            throw new RuntimeException("Too many followers to write");
        }

        DynamoDbTable<FollowRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowRelation.class));
        WriteBatch.Builder<FollowRelation> writeBuilder = WriteBatch.builder(FollowRelation.class).mappedTableResource(table);
        for(FollowRelation item : batchToWrite){
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try{
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            if(result.unprocessedPutItemsForTable(table).size() > 0){
                writeChunkOfFollowers(result.unprocessedPutItemsForTable(table));
            }
        } catch (DynamoDbException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
