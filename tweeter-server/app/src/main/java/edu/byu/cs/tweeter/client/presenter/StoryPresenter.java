package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;

public class StoryPresenter extends PagedPresenter<Status>{

    public StoryPresenter(PagedView<Status> storyObserver) {
        super(new UserService(), null, new StatusService(), storyObserver);
    }


    @Override
    protected void getItems() {
        statusService.getStory(targetUser, lastItem, new StoryObserver());
    }

    public class StoryObserver extends PagedObserver{
        @Override
        protected String getPrefix() {
            return "Failed to get stories";
        }
    }
}
