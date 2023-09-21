import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteResult;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;

public class DynamoUserDAO implements UserDAO{


    private static final String TableName = "users";


    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();


    @Override
    public void addUserBatch(List<User> users) {
        List<UserRelation> batchToWrite = new ArrayList<>();
        for (User user : users){
            UserRelation userRelation = new UserRelation();
            userRelation.setAlias(user.getAlias());
            userRelation.setFirstName(user.getFirstName());
            userRelation.setImageURL(user.getImageUrl());
            batchToWrite.add(userRelation);

            if(batchToWrite.size() == 25){
                writeChunkOfUsers(batchToWrite);
                batchToWrite = new ArrayList<>();
            }
        }

        if (batchToWrite.size() > 0) {
            writeChunkOfUsers(batchToWrite);
        }
    }

    private void writeChunkOfUsers(List<UserRelation> batchToWrite) {
        if(batchToWrite.size() > 25){
            throw new RuntimeException("Too many users to write");
        }

        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        WriteBatch.Builder<UserRelation> writeBuilder = WriteBatch.builder(UserRelation.class).mappedTableResource(table);
        for(UserRelation item : batchToWrite){
            writeBuilder.addPutItem(builder -> builder.item(item));
        }
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches(writeBuilder.build()).build();

        try{
            BatchWriteResult result = enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

            if(result.unprocessedPutItemsForTable(table).size() > 0){
                writeChunkOfUsers(result.unprocessedPutItemsForTable(table));
            }
        } catch (DynamoDbException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
