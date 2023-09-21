package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

public interface AuthTokenDAO {

    AuthToken createNewAuthToken(String alias);
    LogoutResponse removeAuthToken(AuthToken authToken);
    boolean verifyAuthToken(AuthToken authToken);
}
