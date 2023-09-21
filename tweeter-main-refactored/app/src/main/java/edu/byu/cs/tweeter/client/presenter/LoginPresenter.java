package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class LoginPresenter extends AuthenticationPresenter{


    public LoginPresenter(AuthenticationView observer){
        super(new UserService(), null, null, observer);
    }


    public void validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public void login(String alias, String password) {
        userService.login(alias, password, new LoginObserver());

    }



    private class LoginObserver extends AuthenticationObserver {

        @Override
        protected String getPrefix() {
            return "Failed to login";
        }

    }
}
