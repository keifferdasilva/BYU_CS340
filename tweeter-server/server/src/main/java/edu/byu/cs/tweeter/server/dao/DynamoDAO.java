package edu.byu.cs.tweeter.server.dao;

public abstract class DynamoDAO {

    protected static boolean isNonEmptyString(String value){
        return (value != null && value.length() > 0);
    }
}
