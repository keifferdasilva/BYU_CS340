package edu.byu.cs.tweeter.server.dao.relation;

import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedRelation {

    private String post;
    //This is the alias of the follower, i.e. the person whose feed will contain the status
    private String alias;
    private String posterAlias;
    private long timestamp;
    private List<String> urls;
    private List<String> mentions;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPosterAlias() {
        return posterAlias;
    }

    public void setPosterAlias(String posterAlias) {
        this.posterAlias = posterAlias;
    }

    @DynamoDbSortKey
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
}
