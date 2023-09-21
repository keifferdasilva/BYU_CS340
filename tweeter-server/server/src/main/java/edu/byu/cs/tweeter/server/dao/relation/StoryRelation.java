package edu.byu.cs.tweeter.server.dao.relation;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class StoryRelation {

    private String post;
    private String alias;
    private long timestamp;
    private List<String> urls;
    private List<String> mentions;

    public String getPost() {
        return post;
    }

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    @DynamoDbSortKey
    public long getTimestamp() {
        return timestamp;
    }


    public List<String> getUrls() {
        return urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
}
