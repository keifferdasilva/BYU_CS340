package edu.byu.cs.tweeter.server.dao;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.relation.UserRelation;
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

public class DynamoUserDAO implements UserDAO{


    private static final String TableName = "users";


    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();


    @Override
    public User getUser(String userAlias) {
        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));

        Key key = Key.builder()
                .partitionValue(userAlias)
                .build();

        UserRelation userRelation = table.getItem(key);
        if(userRelation == null){
            throw new RuntimeException("[Server Error] User not found");
        }
        else{
            return new User(userRelation.getFirstName(), userRelation.getLastName(), userRelation.getAlias(), userRelation.getImageURL());
        }
    }

    @Override
    public User createNewUser(String username, String firstName, String lastName, String password) {

        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        Key key = Key.builder().partitionValue(username)
                .build();
        UserRelation userRelation = table.getItem(key);
        if(userRelation != null){
            throw new RuntimeException("[Server Error] This alias already exists");
        }
        else{
            UserRelation newUserRelation = new UserRelation();
            newUserRelation.setAlias(username);
            newUserRelation.setFirstName(firstName);
            newUserRelation.setLastName(lastName);
            newUserRelation.setFolloweeCount(0);
            newUserRelation.setFollowerCount(0);
            newUserRelation.setPassword(PasswordHashing.getSecurePassword(password));
            table.putItem(newUserRelation);
            userRelation = table.getItem(key);
            return new User(userRelation.getFirstName(), userRelation.getLastName(), userRelation.getAlias(), userRelation.getImageURL());
        }
    }

    @Override
    public User login(String username, String password) {
        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        Key key = Key.builder().partitionValue(username)
                .build();
        UserRelation userRelation = table.getItem(key);
        if(userRelation == null){
            throw new RuntimeException("[Server Error] This alias does not exist");
        }
        else{
            if(userRelation.getPassword().equals(PasswordHashing.getSecurePassword(password))) {
                return new User(userRelation.getFirstName(), userRelation.getLastName(), userRelation.getAlias(), userRelation.getImageURL());
            }
            else{
                throw new RuntimeException("[Bad Request] Incorrect password");
            }
        }
    }

    @Override
    public void incrementFollowerCount(String userAlias) {
        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        Key key = Key.builder().partitionValue(userAlias)
                .build();
        UserRelation userRelation = table.getItem(key);
        if(userRelation == null){
            throw new RuntimeException("[Server Error] This alias does not exist");
        }
        else{
            userRelation.setFollowerCount(userRelation.getFollowerCount() + 1);
            table.updateItem(userRelation);
        }
    }

    @Override
    public void incrementFolloweeCount(String userAlias) {
        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        Key key = Key.builder().partitionValue(userAlias)
                .build();
        UserRelation userRelation = table.getItem(key);
        if(userRelation == null){
            throw new RuntimeException("[Server Error] This alias does not exist");
        }
        else{
            userRelation.setFolloweeCount(userRelation.getFolloweeCount() + 1);
            table.updateItem(userRelation);
        }
    }

    @Override
    public void decrementFollowerCount(String userAlias) {
        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        Key key = Key.builder().partitionValue(userAlias)
                .build();
        UserRelation userRelation = table.getItem(key);
        if(userRelation == null){
            throw new RuntimeException("[Server Error] This alias does not exist");
        }
        else{
            userRelation.setFollowerCount(userRelation.getFollowerCount() - 1);
            table.updateItem(userRelation);
        }
    }

    @Override
    public void decrementFolloweeCount(String userAlias) {
        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        Key key = Key.builder().partitionValue(userAlias)
                .build();
        UserRelation userRelation = table.getItem(key);
        if(userRelation == null){
            throw new RuntimeException("[Server Error] This alias does not exist");
        }
        else{
            userRelation.setFolloweeCount(userRelation.getFolloweeCount() - 1);
            table.updateItem(userRelation);
        }
    }

    @Override
    public int getFollowersCount(String userAlias) {
        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        Key key = Key.builder().partitionValue(userAlias)
                .build();
        UserRelation userRelation = table.getItem(key);
        if(userRelation == null){
            throw new RuntimeException("[Server Error] This alias does not exist");
        }
        else{
            return userRelation.getFollowerCount();
        }
    }

    @Override
    public int getFollowingCount(String userAlias) {
        DynamoDbTable<UserRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(UserRelation.class));
        Key key = Key.builder().partitionValue(userAlias)
                .build();
        UserRelation userRelation = table.getItem(key);
        if(userRelation == null){
            throw new RuntimeException("[Server Error] This alias does not exist");
        }
        else{
            return userRelation.getFolloweeCount();
        }
    }

    @Override
    public void addUserBatch(List<User> users) {
        List<UserRelation> batchToWrite = new ArrayList<>();
        for (User user : users){
            UserRelation userRelation = new UserRelation();
            userRelation.setAlias(user.getAlias());
            userRelation.setFirstName(user.getFirstName());
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
