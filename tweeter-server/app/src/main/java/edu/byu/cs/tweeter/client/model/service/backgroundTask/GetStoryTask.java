package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    private static final String LOG_TAG = "GET_STORY_TASK";

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        try{
            StoryRequest request = new StoryRequest(getTargetUser().getAlias(), authToken, getLimit(), getLastItem());
            StoryResponse response = getServerFacade().getStory(request, StatusService.GET_STORY_URL_PATH);
            if(response.isSuccess()){
                return new Pair<>(response.getStatuses(), response.getHasMorePages());
            }
            else{
                sendFailedMessage(response.getMessage());
                return null;
            }
        }
        catch(IOException | TweeterRemoteException ex){
            Log.e(LOG_TAG, "Failed to get story", ex);
            sendExceptionMessage(ex);
            return null;
        }
        //return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }
}
