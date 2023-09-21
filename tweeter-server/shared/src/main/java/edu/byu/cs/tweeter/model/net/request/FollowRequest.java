package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowRequest {

    private String followerAlias;

    private String followeeAlias;

    private AuthToken authToken;

    private FollowRequest(){}

    public FollowRequest(String followerAlias, String followeeAlias, AuthToken authToken) {
        this.followerAlias = followerAlias;
        this.followeeAlias = followeeAlias;
        this.authToken = authToken;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
