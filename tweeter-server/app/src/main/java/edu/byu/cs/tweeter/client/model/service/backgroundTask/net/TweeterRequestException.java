package edu.byu.cs.tweeter.client.model.service.backgroundTask.net;
import java.util.List;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;

public class TweeterRequestException extends TweeterRemoteException {

    public TweeterRequestException(String message, String remoteExceptionType, List<String> remoteStakeTrace) {
        super(message, remoteExceptionType, remoteStakeTrace);
    }
}