package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowersRequest {

    private AuthToken authToken;
    private String followeeAlias;
    private String lastFollowerAlias;
    private int limit;

    private FollowersRequest(){}

    public FollowersRequest(AuthToken authToken, String followeeAlias, String lastFollowerAlias, int limit) {
        this.authToken = authToken;
        this.followeeAlias = followeeAlias;
        this.lastFollowerAlias = lastFollowerAlias;
        this.limit = limit;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }

    public void setLastFollowerAlias(String lastFollowerAlias) {
        this.lastFollowerAlias = lastFollowerAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
