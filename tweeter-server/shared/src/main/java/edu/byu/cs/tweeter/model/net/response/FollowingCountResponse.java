package edu.byu.cs.tweeter.model.net.response;

public class FollowingCountResponse extends Response{

    private int followersCount;

    public FollowingCountResponse(String message){
        super(false, message);
    }

    public FollowingCountResponse(int followersCount) {
        super(true, null);
        this.followersCount = followersCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }
}
