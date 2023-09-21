package org.example;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FollowRelation {
    private String follower_handle;
    private String followee_handle;
    private String followerName;
    private String followeeName;

    @DynamoDbPartitionKey
    @DynamoDbSecondarySortKey(indexNames = FollowDAO.IndexName)
    public String getFollower_handle(){
        return follower_handle;
    }

    public void setFollower_handle(String follower_handle){
        this.follower_handle = follower_handle;
    }

    @DynamoDbSortKey
    @DynamoDbSecondaryPartitionKey(indexNames = FollowDAO.IndexName)
    public String getFollowee_handle() {
        return followee_handle;
    }

    public void setFollowee_handle(String followee_handle){
        this.followee_handle = followee_handle;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFolloweeName() {
        return followeeName;
    }

    public void setFolloweeName(String followeeName) {
        this.followeeName = followeeName;
    }

    @Override
    public String toString(){
        return "FollowRelation{" + "follower_handle='" + follower_handle + '\'' +
                ", followee_handle='" + followee_handle + '\'' + ", follower_name='"
                + followerName + '\'' + ", followee_name='" + followeeName + "\'}";
    }
}
