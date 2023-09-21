package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.OpenProfileObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter <T> extends Presenter{

    public PagedPresenter(UserService userService, FollowService followService, StatusService statusService, View observer) {
        super(userService, followService, statusService, observer);
    }


    protected boolean isLoading = false;
    private boolean hasMorePages;

    protected T lastItem;

    protected User targetUser;

    public interface PagedView<T> extends View{

        void initializeMainActivity(User user);

        void removeLoadingFooter();

        void addLoadingFooter();

        void addItems(List<T> items);
    }



    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void loadMoreItems(User user){
        if (!isLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            ((PagedView)observer).addLoadingFooter();
            targetUser = user;
            getItems();
        }
    }

    public void getUser(String userAlias) {
        userService.getUser(userAlias, new GetUserObserver());
    }

    protected abstract void getItems();

    protected abstract class PagedObserver extends Observer implements PagedTaskObserver<T> {

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            ((PagedView)observer).removeLoadingFooter();
            observer.displayMessage(getPrefix() + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            ((PagedView)observer).removeLoadingFooter();
            observer.displayMessage(getPrefix() + " because of: " + exception.getMessage());
        }

        @Override
        public void addItems(List<T> items, boolean hasMorePages) {
            isLoading = false;
            ((PagedView)observer).removeLoadingFooter();
            lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
            setHasMorePages(hasMorePages);

            ((PagedView)observer).addItems(items);
        }

        @Override
        public void handleCleanup() {

        }

    }

    private class GetUserObserver extends Observer implements OpenProfileObserver {
        @Override
        public void goToUserProfile(User user) {
            ((PagedView)observer).initializeMainActivity(user);
        }

        @Override
        protected String getPrefix() {
            return "Failed to get user";
        }
    }
}
