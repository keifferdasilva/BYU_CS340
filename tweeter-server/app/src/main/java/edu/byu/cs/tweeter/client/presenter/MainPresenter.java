package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetItemsObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter{


    public interface MainView extends View{

        void displayFollowingButton();

        void displayFollowButton();

        void unfollow();

        void follow();

        void enableFollowButton(boolean value);

        void logout();

        void postMessageSuccess();

        void getFollowers(int count);

        void getFollowees(int count);
    }

    public MainPresenter(MainView mainObserver){
        super(new UserService(), new FollowService(), new StatusService(), mainObserver);
    }

    public void logout() {
        userService.logout(new LogoutObserver());

    }
    public void createNewStatus(String post) {
        getStatusService().postNewStatus(post, getPostStatusObserver(), parseURLs(post), parseMentions(post));

    }

    public SimpleNotificationObserver getPostStatusObserver() {
        return new PostStatusObserver();
    }

    public void unfollow(User selectedUser) {
        followService.unfollow(selectedUser, new UnfollowObserver());
        observer.displayMessage("Removing " + selectedUser.getName() + "...");
    }
    public void isFollower(User selectedUser) {
        followService.isFollower(selectedUser, new MainObserver());

    }
    public void follow(User selectedUser) {
        followService.follow(selectedUser, new FollowObserver());
        observer.displayMessage("Adding " + selectedUser.getName() + "...");

    }
    public void updateSelectedUserFollowingAndFollowers(User selectedUser) {
        followService.getFollowersCount(selectedUser, new GetFollowersObserver());
        followService.getFollowingCount(selectedUser, new GetFollowingObserver());
    }



    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    private class FollowObserver extends Observer implements SimpleNotificationObserver{

        @Override
        public void handleSuccess() {
            ((MainView)observer).follow();
        }

        @Override
        protected String getPrefix() {
            return "Failed to follow";
        }

        @Override
        public void handleCleanup() {
            ((MainView)observer).enableFollowButton(true);
        }
    }

    private class UnfollowObserver extends Observer implements SimpleNotificationObserver{

        @Override
        protected String getPrefix() {
            return "Failed to unfollow";
        }

        @Override
        public void handleCleanup() {
            ((MainView)observer).enableFollowButton(true);
        }

        @Override
        public void handleSuccess() {
            ((MainView)observer).unfollow();
        }
    }

    private class LogoutObserver extends Observer implements SimpleNotificationObserver {
        @Override
        protected String getPrefix() {
            return "Failed to logout";
        }

        @Override
        public void handleSuccess() {
            ((MainView)observer).logout();
        }
    }

    private class PostStatusObserver extends Observer implements SimpleNotificationObserver{

        @Override
        protected String getPrefix() {
            return "Failed to post status";
        }

        @Override
        public void handleSuccess() {
            ((MainView)observer).postMessageSuccess();
            observer.displayMessage("Successfully Posted!");
        }
    }

    private class GetFollowersObserver extends Observer implements GetItemsObserver {
        @Override
        protected String getPrefix() {
            return "Failed to get followers";
        }

        @Override
        public void handleSuccess(int count) {
            ((MainView)observer).getFollowers(count);
        }
    }

    private class GetFollowingObserver extends Observer implements GetItemsObserver{
        @Override
        protected String getPrefix() {
            return "Failed to get people you follow";
        }

        @Override
        public void handleSuccess(int count) {
            ((MainView)observer).getFollowees(count);
        }
    }

    private class MainObserver extends Observer implements IsFollowerObserver {
        @Override
        protected String getPrefix() {
            return "Failed to determine if you follow this user";
        }

        @Override
        public void handleSuccess(boolean isFollower) {
            // If logged in user if a follower of the selected user, display the follow button as "following"
            if (isFollower) {
                ((MainView)observer).displayFollowingButton();

            } else {
                ((MainView)observer).displayFollowButton();

            }
        }
    }

}


