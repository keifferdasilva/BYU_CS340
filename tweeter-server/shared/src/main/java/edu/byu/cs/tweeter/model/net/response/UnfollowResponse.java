package edu.byu.cs.tweeter.model.net.response;

public class UnfollowResponse extends Response{

    public UnfollowResponse(){
        super(true, null);
    }

    public UnfollowResponse(String message){
        super(false, message);
    }
}
