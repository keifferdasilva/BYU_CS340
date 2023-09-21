import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class DynamoFollowDAO implements FollowsDAO{

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


    @Override
    public void addFollowersBatch(List<String> followers, String followTarget) {
        List<FollowRelation> batchToWrite = new ArrayList<>();
        for (String follower : followers){
            FollowRelation followRelation = new FollowRelation();
            followRelation.setFollower_handle(follower);
            followRelation.setFollowee_handle(followTarget);
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
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
