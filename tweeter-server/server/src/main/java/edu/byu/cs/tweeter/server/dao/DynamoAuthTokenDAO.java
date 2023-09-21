package edu.byu.cs.tweeter.server.dao;

import org.apache.commons.text.RandomStringGenerator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.relation.AuthTokenRelation;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


public class DynamoAuthTokenDAO implements AuthTokenDAO{

    private static final String TableName = "authtoken";

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_WEST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();


    @Override
    public AuthToken createNewAuthToken(String alias) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('A', 'z').build();
        AuthToken authToken = new AuthToken(generator.generate(15));
        DynamoDbTable<AuthTokenRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenRelation.class));
        Key key = Key.builder().partitionValue(authToken.getToken())
                .build();
        AuthTokenRelation authTokenRelation = table.getItem(key);
        while(authTokenRelation != null){
            authToken = new AuthToken(generator.generate(15));
            key = Key.builder().partitionValue(authToken.getToken())
                    .build();
            authTokenRelation = table.getItem(key);
        }
        AuthTokenRelation newAuthTokenRelation = new AuthTokenRelation();
        newAuthTokenRelation.setAuthtoken(authToken.getToken());
        newAuthTokenRelation.setLastTimeUsed(System.currentTimeMillis());
        newAuthTokenRelation.setUserAlias(alias);
        table.putItem(newAuthTokenRelation);
        return authToken;
    }

    @Override
    public LogoutResponse removeAuthToken(AuthToken authToken) {
        DynamoDbTable<AuthTokenRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenRelation.class));
        Key key = Key.builder().partitionValue(authToken.getToken())
                .build();
        table.deleteItem(key);
        return new LogoutResponse();
    }

    @Override
    public boolean verifyAuthToken(AuthToken authToken) {
        DynamoDbTable<AuthTokenRelation> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenRelation.class));
        Key key = Key.builder().partitionValue(authToken.getToken())
                .build();
        AuthTokenRelation authTokenRelation = table.getItem(key);

        if(authTokenRelation == null){
            return false;
        }
        else if (System.currentTimeMillis() - authTokenRelation.getLastTimeUsed() > 14400000){
            //Make this remove all expired tokens if I want to expand on this
            table.deleteItem(key);
            return false;
        }
        else{
            AuthTokenRelation newAuthTokenRelation = new AuthTokenRelation();
            newAuthTokenRelation.setAuthtoken(authTokenRelation.getAuthtoken());
            newAuthTokenRelation.setUserAlias(authTokenRelation.getUserAlias());
            newAuthTokenRelation.setLastTimeUsed(System.currentTimeMillis());
            table.updateItem(newAuthTokenRelation);
            return true;
        }
    }
}
