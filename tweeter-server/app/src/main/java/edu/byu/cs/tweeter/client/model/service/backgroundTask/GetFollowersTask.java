package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    private static final String LOG_TAG = "GET_FOLLOWERS_TASK";

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        try {
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            String lastFollowerAlias = getLastItem() == null ? null : getLastItem().getAlias();
            FollowersRequest request = new FollowersRequest(Cache.getInstance().getCurrUserAuthToken(), targetUserAlias, lastFollowerAlias, getLimit());
            FollowersResponse response = getServerFacade().getFollowers(request, FollowService.GET_FOLLOWERS_URL_PATH);
            if(response.isSuccess()){
                return new Pair<>(response.getFollowers(), response.getHasMorePages());
            }
            else{
                sendFailedMessage(response.getMessage());
                return null;
            }
        }catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followers", ex);
            sendExceptionMessage(ex);
            return null;
        }
        //return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
