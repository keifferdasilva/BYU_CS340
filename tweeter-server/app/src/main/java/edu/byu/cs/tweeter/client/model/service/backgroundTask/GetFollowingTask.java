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
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {

    private static final String LOG_TAG = "GET_FOLLOWING_TASK";

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        try {
            //System.out.println(getTargetUser().toString());
            String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
            String lastFolloweeAlias = getLastItem() == null ? null : getLastItem().getAlias();
            FollowingRequest request = new FollowingRequest(Cache.getInstance().getCurrUserAuthToken(), targetUserAlias, getLimit(), lastFolloweeAlias);
            FollowingResponse response = getServerFacade().getFollowees(request, FollowService.GET_FOLLOWING_URL_PATH);
            if(response.isSuccess()){
                Pair<List<User>, Boolean> pageOfUsers= new Pair<>(response.getFollowees(), response.getHasMorePages());
                return pageOfUsers;
            }
            else{
                sendFailedMessage(response.getMessage());
                return null;
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get followees", ex);
            sendExceptionMessage(ex);
            return null;
        }

        //return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }


}
